package com.digitalpark.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 企业信息实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_info")
@Schema(description = "企业信息")
public class EnterpriseInfo extends BaseEntity {

    @Schema(description = "企业名称")
    private String name;

    @Schema(description = "企业编码")
    private String code;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "企业规模")
    private String scale;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "楼栋ID")
    private Long buildingId;

    @Schema(description = "楼层")
    private String floor;

    @Schema(description = "房间号")
    private String room;

    @Schema(description = "入驻日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate enterDate;

    @Schema(description = "合同到期日")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractExpire;

    @Schema(description = "状态(在营/注销/迁出)")
    private String status;
}
