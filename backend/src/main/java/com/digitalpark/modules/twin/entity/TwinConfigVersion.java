package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置版本实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_config_version")
@Schema(description = "配置版本")
public class TwinConfigVersion extends BaseEntity {

    @Schema(description = "版本号")
    private String versionNo;

    @Schema(description = "版本状态(draft/published/rollback)")
    private String status;

    @Schema(description = "配置快照JSON")
    private String configSnapshot;

    @Schema(description = "变更说明")
    private String changeLog;

    @Schema(description = "发布时间")
    private java.time.LocalDateTime publishTime;

    @Schema(description = "发布人")
    private String publishBy;
}
