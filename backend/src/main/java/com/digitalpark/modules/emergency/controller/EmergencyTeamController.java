package com.digitalpark.modules.emergency.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.emergency.entity.EmergencyTeam;
import com.digitalpark.modules.emergency.service.EmergencyTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应急队伍Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/emergency/team")
@RequiredArgsConstructor
public class EmergencyTeamController {

    private final EmergencyTeamService emergencyTeamService;

    @PreAuthorize("@ss.hasPermi('emergency:team:list')")
    @GetMapping("/page")
    public R<PageResult<EmergencyTeam>> page(EmergencyTeam query,
                                              @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(emergencyTeamService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('emergency:team:list')")
    @GetMapping("/list")
    public R<List<EmergencyTeam>> list(EmergencyTeam query) {
        return R.ok(emergencyTeamService.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('emergency:team:query')")
    @GetMapping("/{id}")
    public R<EmergencyTeam> getInfo(@PathVariable Long id) {
        return R.ok(emergencyTeamService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('emergency:team:add')")
    @PostMapping
    public R<EmergencyTeam> add(@RequestBody EmergencyTeam team) {
        emergencyTeamService.insert(team);
        return R.ok(team);
    }

    @PreAuthorize("@ss.hasPermi('emergency:team:edit')")
    @PutMapping
    public R<EmergencyTeam> edit(@RequestBody EmergencyTeam team) {
        emergencyTeamService.update(team);
        return R.ok(team);
    }

    @PreAuthorize("@ss.hasPermi('emergency:team:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        emergencyTeamService.deleteById(id);
        return R.ok();
    }
}
