package com.backend.adminLogs.controller;

import com.backend.adminLogs.dtos.AdminLogDTO;
import com.backend.adminLogs.services.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adminLogs")
public class AdminLogController {

    @Autowired
    private AdminLogService adminLogService;

    @GetMapping
    public List<AdminLogDTO> getAllAdminLogs() {
        return adminLogService.getAllAdminLogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminLogDTO> getAdminLogById(@PathVariable Long id) {
        Optional<AdminLogDTO> adminLogDTO = adminLogService.getAdminLogById(id);
        return adminLogDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public AdminLogDTO createAdminLog(@RequestBody AdminLogDTO adminLogDTO) {
        return adminLogService.createAdminLog(adminLogDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminLogDTO> updateAdminLog(@PathVariable Long id, @RequestBody AdminLogDTO adminLogDTO) {
        return ResponseEntity.ok(adminLogService.updateAdminLog(id, adminLogDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdminLog(@PathVariable Long id) {
        adminLogService.deleteAdminLog(id);
        return ResponseEntity.noContent().build();
    }
}
