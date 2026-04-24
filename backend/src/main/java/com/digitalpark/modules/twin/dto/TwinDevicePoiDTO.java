package com.digitalpark.modules.twin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 设备POI批量保存DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "设备POI批量保存请求")
public class TwinDevicePoiDTO {

    @Schema(description = "设备POI列表")
    @NotEmpty(message = "设备POI列表不能为空")
    @Valid
    private List<DevicePoiItem> items;

    @Data
    @Schema(description = "设备POI项")
    public static class DevicePoiItem {

        @Schema(description = "POI ID(更新时传入)")
        private Long id;

        @Schema(description = "设备ID")
        private Long deviceId;

        @Schema(description = "设备类型")
        private String deviceType;

        @Schema(description = "POI名称")
        private String name;

        @Schema(description = "X坐标")
        private Double x;

        @Schema(description = "Y坐标")
        private Double y;

        @Schema(description = "图标")
        private String icon;

        @Schema(description = "状态(在线/离线/故障)")
        private String status;

        @Schema(description = "弹窗配置JSON")
        private String popupConfigJson;
    }
}
