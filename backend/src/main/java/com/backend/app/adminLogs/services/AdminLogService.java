package com.backend.app.adminLogs.services;


import com.backend.app.adminLogs.dtos.AdminLogDTO;
import com.backend.app.adminLogs.entities.AdminLog;
import com.backend.app.adminLogs.repositories.AdminLogRepository;
import com.backend.app.user.models.User;
import com.backend.app.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminLogService {

    @Autowired
    private AdminLogRepository adminLogRepository;

    @Autowired
    private UserRepository userRepository;

    public List<AdminLogDTO> getAllAdminLogs() {
        return adminLogRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<AdminLogDTO> getAdminLogById(Long id) {
        return adminLogRepository.findById(id).map(this::convertToDTO);
    }

    public AdminLogDTO createAdminLog(AdminLogDTO adminLogDTO) {
        AdminLog adminLog = convertToEntity(adminLogDTO);
        return convertToDTO(adminLogRepository.save(adminLog));
    }

    public AdminLogDTO updateAdminLog(Long id, AdminLogDTO adminLogDTO) {
        AdminLog adminLog = adminLogRepository.findById(id).orElseThrow(() -> new RuntimeException("AdminLog not found"));
        adminLog.setAction(adminLogDTO.getAction());
        adminLog.setDetails(adminLogDTO.getDetails());
        return convertToDTO(adminLogRepository.save(adminLog));
    }

    public void deleteAdminLog(Long id) {
        adminLogRepository.deleteById(id);
    }

    private AdminLogDTO convertToDTO(AdminLog adminLog) {
        AdminLogDTO adminLogDTO = new AdminLogDTO();
        adminLogDTO.setId(adminLog.getId());
        adminLogDTO.setAdminId(adminLog.getAdmin().getId());
        adminLogDTO.setAction(adminLog.getAction());
        adminLogDTO.setDetails(adminLog.getDetails());
        adminLogDTO.setTimestamp(adminLog.getTimestamp());
        return adminLogDTO;
    }

    private AdminLog convertToEntity(AdminLogDTO adminLogDTO) {
        AdminLog adminLog = new AdminLog();
        adminLog.setAction(adminLogDTO.getAction());
        adminLog.setDetails(adminLogDTO.getDetails());
        // Assuming you have a method to get Admin by ID
        User admin = userRepository.findById(adminLogDTO.getAdminId()).orElseThrow(() -> new RuntimeException("Admin not found"));
        adminLog.setAdmin(admin);
        adminLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return adminLog;
    }
}
