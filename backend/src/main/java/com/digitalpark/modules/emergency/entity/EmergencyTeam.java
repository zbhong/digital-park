package com.digitalpark.modules.emergency.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应急队伍实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_team")
@Schema(description = "应急队伍")
public class EmergencyTeam extends BaseEntity {

    /** 队伍名称 */
    @Schema(description = "队伍名称")
    private String name;

    /** 负责人 */
    @Schema(description = "负责人")
    private String leader;

    /** 成员JSON */
    @Schema(description = "成员JSON")
    private String membersJson;

    /** 联系电话 */
    @Schema(description = "联系电话")
    private String phone;

    /** 职责 */
    @Schema(description = "职责")
    private String duty;
}
