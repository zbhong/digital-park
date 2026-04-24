package com.digitalpark.modules.safety.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 生产安全实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("safety_production")
@Schema(description = "生产安全")
public class SafetyProduction extends BaseEntity {

    /** 类型：操作违规/设备异常/环境异常/物料异常 */
    @Schema(description = "类型")
    private String type;

    /** 企业ID */
    @Schema(description = "企业ID")
    private Long enterpriseId;

    /** 区域ID */
    @Schema(description = "区域ID")
    private Long areaId;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 严重程度 */
    @Schema(description = "严重程度")
    private String severity;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 处理人ID */
    @Schema(description = "处理人ID")
    private Long handlerId;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "处理时间")
    private LocalDateTime handleTime;
}
