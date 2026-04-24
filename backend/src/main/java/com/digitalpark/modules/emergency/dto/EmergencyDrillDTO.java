package com.digitalpark.modules.emergency.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 应急演练DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "应急演练请求")
public class EmergencyDrillDTO {

    @Schema(description = "演练ID")
    private Long id;

    /** 预案ID */
    @NotNull(message = "预案不能为空")
    @Schema(description = "预案ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long planId;

    /** 演练名称 */
    @NotBlank(message = "演练名称不能为空")
    @Schema(description = "演练名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /** 演练日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "演练日期")
    private LocalDate drillDate;

    /** 演练地点 */
    @Schema(description = "演练地点")
    private String location;

    /** 参与人数 */
    @Schema(description = "参与人数")
    private Integer participants;

    /** 演练结果 */
    @Schema(description = "演练结果")
    private String result;

    /** 演练总结 */
    @Schema(description = "演练总结")
    private String summary;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
