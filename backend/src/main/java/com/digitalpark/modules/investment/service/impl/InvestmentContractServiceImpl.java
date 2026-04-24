package com.digitalpark.modules.investment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentContract;
import com.digitalpark.modules.investment.mapper.InvestmentContractMapper;
import com.digitalpark.modules.investment.service.InvestmentContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class InvestmentContractServiceImpl implements InvestmentContractService {

    private final InvestmentContractMapper contractMapper;

    @Override
    public PageResult<InvestmentContract> selectPage(InvestmentContract query, int pageNum, int pageSize) {
        LambdaQueryWrapper<InvestmentContract> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(InvestmentContract::getCreateTime);
        Page<InvestmentContract> page = contractMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public InvestmentContract selectById(Long id) {
        return contractMapper.selectById(id);
    }

    @Override
    public InvestmentContract selectByContractNo(String contractNo) {
        LambdaQueryWrapper<InvestmentContract> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentContract::getContractNo, contractNo);
        return contractMapper.selectOne(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectStatusStatistics() {
        List<InvestmentContract> list = contractMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (InvestmentContract contract : list) {
            String status = StringUtils.hasText(contract.getStatus()) ? contract.getStatus() : "未知";
            stats.merge(status, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("status", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(InvestmentContract contract) {
        if (!StringUtils.hasText(contract.getStatus())) {
            contract.setStatus("草稿");
        }
        return contractMapper.insert(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(InvestmentContract contract) {
        return contractMapper.updateById(contract);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitForApproval(Long id) {
        LambdaUpdateWrapper<InvestmentContract> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentContract::getId, id)
               .set(InvestmentContract::getStatus, "审批中");
        return contractMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approve(Long id) {
        LambdaUpdateWrapper<InvestmentContract> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentContract::getId, id)
               .set(InvestmentContract::getStatus, "生效中");
        return contractMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int reject(Long id) {
        LambdaUpdateWrapper<InvestmentContract> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentContract::getId, id)
               .set(InvestmentContract::getStatus, "草稿");
        return contractMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int terminate(Long id) {
        LambdaUpdateWrapper<InvestmentContract> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentContract::getId, id)
               .set(InvestmentContract::getStatus, "已终止");
        return contractMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return contractMapper.deleteById(id);
    }

    private LambdaQueryWrapper<InvestmentContract> buildQueryWrapper(InvestmentContract query) {
        LambdaQueryWrapper<InvestmentContract> wrapper = new LambdaQueryWrapper<>();
        if (query.getCustomerId() != null) {
            wrapper.eq(InvestmentContract::getCustomerId, query.getCustomerId());
        }
        if (query.getEnterpriseId() != null) {
            wrapper.eq(InvestmentContract::getEnterpriseId, query.getEnterpriseId());
        }
        if (query.getAssetId() != null) {
            wrapper.eq(InvestmentContract::getAssetId, query.getAssetId());
        }
        if (StringUtils.hasText(query.getContractNo())) {
            wrapper.like(InvestmentContract::getContractNo, query.getContractNo());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(InvestmentContract::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
