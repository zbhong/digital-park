package com.digitalpark.modules.property.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.property.entity.PropertyVehicle;
import com.digitalpark.modules.property.service.PropertyVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 车辆Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/property/vehicle")
@RequiredArgsConstructor
public class PropertyVehicleController {

    private final PropertyVehicleService vehicleService;

    @PreAuthorize("@ss.hasPermi('property:vehicle:list')")
    @GetMapping("/page")
    public R<PageResult<PropertyVehicle>> page(PropertyVehicle query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(vehicleService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('property:vehicle:query')")
    @GetMapping("/{id}")
    public R<PropertyVehicle> getInfo(@PathVariable Long id) {
        return R.ok(vehicleService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('property:vehicle:query')")
    @GetMapping("/plate/{plateNumber}")
    public R<PropertyVehicle> getByPlateNumber(@PathVariable String plateNumber) {
        return R.ok(vehicleService.selectByPlateNumber(plateNumber));
    }

    @PreAuthorize("@ss.hasPermi('property:vehicle:add')")
    @PostMapping
    public R<PropertyVehicle> add(@RequestBody PropertyVehicle vehicle) {
        vehicleService.insert(vehicle);
        return R.ok(vehicle);
    }

    @PreAuthorize("@ss.hasPermi('property:vehicle:edit')")
    @PutMapping
    public R<PropertyVehicle> edit(@RequestBody PropertyVehicle vehicle) {
        vehicleService.update(vehicle);
        return R.ok(vehicle);
    }

    @PreAuthorize("@ss.hasPermi('property:vehicle:disable')")
    @PutMapping("/disable/{id}")
    public R<Void> disable(@PathVariable Long id) {
        vehicleService.disable(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:vehicle:enable')")
    @PutMapping("/enable/{id}")
    public R<Void> enable(@PathVariable Long id) {
        vehicleService.enable(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('property:vehicle:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        vehicleService.deleteById(id);
        return R.ok();
    }
}
