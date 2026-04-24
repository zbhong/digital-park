package com.digitalpark.modules.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.environment.dto.EnvMonitorDataDTO;
import com.digitalpark.modules.environment.entity.EnvMonitorData;
import com.digitalpark.modules.environment.entity.EnvMonitorPoint;
import com.digitalpark.modules.environment.mapper.EnvMonitorDataMapper;
import com.digitalpark.modules.environment.mapper.EnvMonitorPointMapper;
import com.digitalpark.modules.environment.service.EnvMonitorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 环境监测数据Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnvMonitorDataServiceImpl implements EnvMonitorDataService {

    private final EnvMonitorDataMapper envMonitorDataMapper;
    private final EnvMonitorPointMapper envMonitorPointMapper;

    /** 大气质量标准限值 */
    private static final Map<String, BigDecimal> AIR_STANDARDS = new HashMap<>();
    static {
        AIR_STANDARDS.put("pm25", new BigDecimal("75"));
        AIR_STANDARDS.put("pm10", new BigDecimal("150"));
        AIR_STANDARDS.put("so2", new BigDecimal("150"));
        AIR_STANDARDS.put("no2", new BigDecimal("80"));
        AIR_STANDARDS.put("co", new BigDecimal("4"));
        AIR_STANDARDS.put("o3", new BigDecimal("160"));
        AIR_STANDARDS.put("vocs", new BigDecimal("60"));
    }

    /** 噪音标准限值 */
    private static final BigDecimal NOISE_DAY_STANDARD = new BigDecimal("65");
    private static final BigDecimal NOISE_NIGHT_STANDARD = new BigDecimal("55");

    /** 扬尘标准限值 */
    private static final BigDecimal DUST_STANDARD = new BigDecimal("300");

    @Override
    public PageResult<EnvMonitorData> selectPage(EnvMonitorData query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EnvMonitorData> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EnvMonitorData::getRecordTime);
        Page<EnvMonitorData> page = envMonitorDataMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EnvMonitorData selectById(Long id) {
        return envMonitorDataMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EnvMonitorData data) {
        return envMonitorDataMapper.insert(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchInsert(List<EnvMonitorData> dataList) {
        int count = 0;
        for (EnvMonitorData data : dataList) {
            count += envMonitorDataMapper.insert(data);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EnvMonitorData data) {
        return envMonitorDataMapper.updateById(data);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return envMonitorDataMapper.deleteById(id);
    }

    @Override
    public Map<String, Object> selectStatisticsByTime(EnvMonitorDataDTO dto) {
        LambdaQueryWrapper<EnvMonitorData> wrapper = buildDtoQueryWrapper(dto);
        wrapper.orderByAsc(EnvMonitorData::getRecordTime);
        List<EnvMonitorData> dataList = envMonitorDataMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", dataList.size());

        // 按指标统计平均值、最大值、最小值
        Map<String, Object> indicatorStats = new HashMap<>();
        computeIndicatorStats(dataList, indicatorStats);
        result.put("indicatorStatistics", indicatorStats);

        // 按日期分组统计（取最近30天）
        Map<String, List<EnvMonitorData>> byDate = dataList.stream()
                .filter(d -> d.getRecordTime() != null)
                .collect(Collectors.groupingBy(d -> d.getRecordTime().toLocalDate().toString(),
                        TreeMap::new, Collectors.toList()));

        List<Map<String, Object>> timeSeries = new ArrayList<>();
        for (Map.Entry<String, List<EnvMonitorData>> entry : byDate.entrySet()) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", entry.getKey());
            dayData.put("count", entry.getValue().size());

            List<EnvMonitorData> dayList = entry.getValue();
            if (!dayList.isEmpty()) {
                dayData.put("avgPm25", calcAvg(dayList, EnvMonitorData::getPm25));
                dayData.put("avgPm10", calcAvg(dayList, EnvMonitorData::getPm10));
                dayData.put("avgNoiseDay", calcAvg(dayList, EnvMonitorData::getNoiseDay));
                dayData.put("avgDust", calcAvg(dayList, EnvMonitorData::getDustConcentration));
            }
            timeSeries.add(dayData);
        }
        result.put("timeSeries", timeSeries);

        return result;
    }

    @Override
    public Map<String, Object> selectStatisticsByArea(EnvMonitorDataDTO dto) {
        // 获取区域内所有监测点
        LambdaQueryWrapper<EnvMonitorPoint> pointWrapper = new LambdaQueryWrapper<>();
        if (dto.getAreaId() != null) {
            pointWrapper.eq(EnvMonitorPoint::getAreaId, dto.getAreaId());
        }
        if (StringUtils.hasText(dto.getPointType())) {
            pointWrapper.eq(EnvMonitorPoint::getType, dto.getPointType());
        }
        List<EnvMonitorPoint> points = envMonitorPointMapper.selectList(pointWrapper);
        List<Long> pointIds = points.stream().map(EnvMonitorPoint::getId).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("pointCount", points.size());

        if (pointIds.isEmpty()) {
            result.put("areaStatistics", new ArrayList<>());
            return result;
        }

        LambdaQueryWrapper<EnvMonitorData> dataWrapper = buildDtoQueryWrapper(dto);
        dataWrapper.in(EnvMonitorData::getPointId, pointIds);
        List<EnvMonitorData> dataList = envMonitorDataMapper.selectList(dataWrapper);

        // 按点位分组统计
        Map<Long, List<EnvMonitorData>> byPoint = dataList.stream()
                .filter(d -> d.getPointId() != null)
                .collect(Collectors.groupingBy(EnvMonitorData::getPointId));

        List<Map<String, Object>> areaStats = new ArrayList<>();
        for (EnvMonitorPoint point : points) {
            List<EnvMonitorData> pointDataList = byPoint.getOrDefault(point.getId(), Collections.emptyList());
            Map<String, Object> pointStat = new HashMap<>();
            pointStat.put("pointId", point.getId());
            pointStat.put("pointName", point.getName());
            pointStat.put("pointType", point.getType());
            pointStat.put("dataCount", pointDataList.size());

            if (!pointDataList.isEmpty()) {
                pointStat.put("avgPm25", calcAvg(pointDataList, EnvMonitorData::getPm25));
                pointStat.put("avgPm10", calcAvg(pointDataList, EnvMonitorData::getPm10));
                pointStat.put("avgNoiseDay", calcAvg(pointDataList, EnvMonitorData::getNoiseDay));
                pointStat.put("avgDust", calcAvg(pointDataList, EnvMonitorData::getDustConcentration));
            }
            areaStats.add(pointStat);
        }
        result.put("areaStatistics", areaStats);

        return result;
    }

    @Override
    public Map<String, Object> selectStatisticsByIndicator(EnvMonitorDataDTO dto) {
        LambdaQueryWrapper<EnvMonitorData> wrapper = buildDtoQueryWrapper(dto);
        List<EnvMonitorData> dataList = envMonitorDataMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", dataList.size());

        // 按指定指标统计
        String indicator = dto.getIndicator();
        if (StringUtils.hasText(indicator)) {
            Map<String, Object> indicatorStat = new HashMap<>();
            indicatorStat.put("indicator", indicator);

            List<BigDecimal> values = getIndicatorValues(dataList, indicator);
            if (!values.isEmpty()) {
                BigDecimal avg = values.stream()
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
                BigDecimal max = values.stream().max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
                BigDecimal min = values.stream().min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);

                indicatorStat.put("avg", avg);
                indicatorStat.put("max", max);
                indicatorStat.put("min", min);
                indicatorStat.put("count", values.size());

                // 超标次数
                BigDecimal standard = AIR_STANDARDS.get(indicator);
                if (standard != null) {
                    long overCount = values.stream().filter(v -> v != null && v.compareTo(standard) > 0).count();
                    indicatorStat.put("overStandardCount", overCount);
                    indicatorStat.put("standard", standard);
                }
            }
            result.put("indicatorDetail", indicatorStat);
        }

        // 所有指标概览
        Map<String, Object> allIndicators = new HashMap<>();
        computeIndicatorStats(dataList, allIndicators);
        result.put("allIndicators", allIndicators);

        return result;
    }

    @Override
    public List<Map<String, Object>> selectOverStandardWarnings(EnvMonitorDataDTO dto) {
        LambdaQueryWrapper<EnvMonitorData> wrapper = buildDtoQueryWrapper(dto);
        wrapper.orderByDesc(EnvMonitorData::getRecordTime);
        List<EnvMonitorData> dataList = envMonitorDataMapper.selectList(wrapper);

        List<Map<String, Object>> warnings = new ArrayList<>();

        for (EnvMonitorData data : dataList) {
            // 检查大气指标
            checkAirIndicators(data, warnings);
            // 检查噪音
            checkNoise(data, warnings);
            // 检查扬尘
            checkDust(data, warnings);
        }

        return warnings;
    }

    @Override
    public Map<String, Object> selectPollutionSourceAnalysis(EnvMonitorDataDTO dto) {
        LambdaQueryWrapper<EnvMonitorData> wrapper = buildDtoQueryWrapper(dto);
        List<EnvMonitorData> dataList = envMonitorDataMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();

        // 按点位统计超标情况
        Map<Long, List<Map<String, Object>>> pointWarnings = new HashMap<>();
        for (EnvMonitorData data : dataList) {
            List<Map<String, Object>> itemWarnings = new ArrayList<>();
            checkAirIndicators(data, itemWarnings);
            checkNoise(data, itemWarnings);
            checkDust(data, itemWarnings);

            if (!itemWarnings.isEmpty() && data.getPointId() != null) {
                pointWarnings.computeIfAbsent(data.getPointId(), k -> new ArrayList<>())
                        .addAll(itemWarnings);
            }
        }

        // 排序找出污染最严重的点位
        List<Map<String, Object>> sourceRanking = pointWarnings.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> source = new HashMap<>();
                    source.put("pointId", entry.getKey());
                    source.put("warningCount", entry.getValue().size());

                    // 获取点位信息
                    EnvMonitorPoint point = envMonitorPointMapper.selectById(entry.getKey());
                    if (point != null) {
                        source.put("pointName", point.getName());
                        source.put("pointType", point.getType());
                        source.put("location", point.getLocation());
                    }

                    // 统计各指标超标次数
                    Map<String, Long> indicatorCount = entry.getValue().stream()
                            .filter(w -> w.get("indicator") != null)
                            .collect(Collectors.groupingBy(
                                    w -> w.get("indicator").toString(),
                                    Collectors.counting()));
                    source.put("indicatorWarningCount", indicatorCount);

                    // 最近超标详情
                    source.put("recentWarnings", entry.getValue().stream()
                            .limit(5).collect(Collectors.toList()));

                    return source;
                })
                .sorted((a, b) -> Long.compare(
                        (Long) b.get("warningCount"),
                        (Long) a.get("warningCount")))
                .collect(Collectors.toList());

        result.put("totalWarnings", sourceRanking.stream()
                .mapToLong(s -> (Long) s.get("warningCount")).sum());
        result.put("pollutedPointCount", sourceRanking.size());
        result.put("sourceRanking", sourceRanking);

        // 污染趋势分析
        Map<String, Long> typeDistribution = sourceRanking.stream()
                .filter(s -> s.get("pointType") != null)
                .collect(Collectors.groupingBy(
                        s -> s.get("pointType").toString(),
                        Collectors.counting()));
        result.put("pollutionTypeDistribution", typeDistribution);

        return result;
    }

    private LambdaQueryWrapper<EnvMonitorData> buildQueryWrapper(EnvMonitorData query) {
        LambdaQueryWrapper<EnvMonitorData> wrapper = new LambdaQueryWrapper<>();
        if (query.getPointId() != null) {
            wrapper.eq(EnvMonitorData::getPointId, query.getPointId());
        }
        if (StringUtils.hasText(query.getPointName())) {
            wrapper.like(EnvMonitorData::getPointName, query.getPointName());
        }
        return wrapper;
    }

    private LambdaQueryWrapper<EnvMonitorData> buildDtoQueryWrapper(EnvMonitorDataDTO dto) {
        LambdaQueryWrapper<EnvMonitorData> wrapper = new LambdaQueryWrapper<>();
        if (!CollectionUtils.isEmpty(dto.getPointIds())) {
            wrapper.in(EnvMonitorData::getPointId, dto.getPointIds());
        }
        if (dto.getStartTime() != null) {
            wrapper.ge(EnvMonitorData::getRecordTime, dto.getStartTime());
        }
        if (dto.getEndTime() != null) {
            wrapper.le(EnvMonitorData::getRecordTime, dto.getEndTime());
        }
        return wrapper;
    }

    private void computeIndicatorStats(List<EnvMonitorData> dataList, Map<String, Object> stats) {
        stats.put("avgPm25", calcAvg(dataList, EnvMonitorData::getPm25));
        stats.put("avgPm10", calcAvg(dataList, EnvMonitorData::getPm10));
        stats.put("avgSo2", calcAvg(dataList, EnvMonitorData::getSo2));
        stats.put("avgNo2", calcAvg(dataList, EnvMonitorData::getNo2));
        stats.put("avgCo", calcAvg(dataList, EnvMonitorData::getCo));
        stats.put("avgO3", calcAvg(dataList, EnvMonitorData::getO3));
        stats.put("avgVocs", calcAvg(dataList, EnvMonitorData::getVocs));
        stats.put("avgNoiseDay", calcAvg(dataList, EnvMonitorData::getNoiseDay));
        stats.put("avgNoiseNight", calcAvg(dataList, EnvMonitorData::getNoiseNight));
        stats.put("avgDust", calcAvg(dataList, EnvMonitorData::getDustConcentration));
    }

    private BigDecimal calcAvg(List<EnvMonitorData> list, java.util.function.Function<EnvMonitorData, BigDecimal> getter) {
        List<BigDecimal> values = list.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return values.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
    }

    private List<BigDecimal> getIndicatorValues(List<EnvMonitorData> dataList, String indicator) {
        return dataList.stream()
                .map(d -> getIndicatorValue(d, indicator))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private BigDecimal getIndicatorValue(EnvMonitorData data, String indicator) {
        switch (indicator) {
            case "pm25": return data.getPm25();
            case "pm10": return data.getPm10();
            case "so2": return data.getSo2();
            case "no2": return data.getNo2();
            case "co": return data.getCo();
            case "o3": return data.getO3();
            case "vocs": return data.getVocs();
            case "noiseDay": return data.getNoiseDay();
            case "noiseNight": return data.getNoiseNight();
            case "dustConcentration": return data.getDustConcentration();
            case "waterPh": return data.getWaterPh();
            case "waterCod": return data.getWaterCod();
            default: return null;
        }
    }

    private void checkAirIndicators(EnvMonitorData data, List<Map<String, Object>> warnings) {
        checkIndicator(data, "pm25", data.getPm25(), AIR_STANDARDS.get("pm25"), "PM2.5", warnings);
        checkIndicator(data, "pm10", data.getPm10(), AIR_STANDARDS.get("pm10"), "PM10", warnings);
        checkIndicator(data, "so2", data.getSo2(), AIR_STANDARDS.get("so2"), "SO2", warnings);
        checkIndicator(data, "no2", data.getNo2(), AIR_STANDARDS.get("no2"), "NO2", warnings);
        checkIndicator(data, "co", data.getCo(), AIR_STANDARDS.get("co"), "CO", warnings);
        checkIndicator(data, "o3", data.getO3(), AIR_STANDARDS.get("o3"), "O3", warnings);
        checkIndicator(data, "vocs", data.getVocs(), AIR_STANDARDS.get("vocs"), "VOCs", warnings);
    }

    private void checkNoise(EnvMonitorData data, List<Map<String, Object>> warnings) {
        if (data.getNoiseDay() != null && data.getNoiseDay().compareTo(NOISE_DAY_STANDARD) > 0) {
            addWarning(data, "noiseDay", "昼间噪音", data.getNoiseDay(), NOISE_DAY_STANDARD, warnings);
        }
        if (data.getNoiseNight() != null && data.getNoiseNight().compareTo(NOISE_NIGHT_STANDARD) > 0) {
            addWarning(data, "noiseNight", "夜间噪音", data.getNoiseNight(), NOISE_NIGHT_STANDARD, warnings);
        }
    }

    private void checkDust(EnvMonitorData data, List<Map<String, Object>> warnings) {
        if (data.getDustConcentration() != null && data.getDustConcentration().compareTo(DUST_STANDARD) > 0) {
            addWarning(data, "dustConcentration", "扬尘浓度", data.getDustConcentration(), DUST_STANDARD, warnings);
        }
    }

    private void checkIndicator(EnvMonitorData data, String key, BigDecimal value,
                                BigDecimal standard, String label,
                                List<Map<String, Object>> warnings) {
        if (value != null && standard != null && value.compareTo(standard) > 0) {
            addWarning(data, key, label, value, standard, warnings);
        }
    }

    private void addWarning(EnvMonitorData data, String indicator, String label,
                            BigDecimal value, BigDecimal standard,
                            List<Map<String, Object>> warnings) {
        Map<String, Object> warning = new HashMap<>();
        warning.put("pointId", data.getPointId());
        warning.put("pointName", data.getPointName());
        warning.put("indicator", indicator);
        warning.put("indicatorLabel", label);
        warning.put("value", value);
        warning.put("standard", standard);
        warning.put("exceedRatio", value.subtract(standard)
                .divide(standard, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) + "%");
        warning.put("recordTime", data.getRecordTime());
        warnings.add(warning);
    }
}
