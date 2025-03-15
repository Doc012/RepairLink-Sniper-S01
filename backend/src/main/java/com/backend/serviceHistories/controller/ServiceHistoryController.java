package com.backend.serviceHistories.controller;

import com.backend.serviceHistories.dtos.ServiceHistoryDTO;
import com.backend.serviceHistories.services.ServiceHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/serviceHistories")
public class ServiceHistoryController {

    @Autowired
    private ServiceHistoryService serviceHistoryService;

    @GetMapping
    public List<ServiceHistoryDTO> getAllServiceHistories() {
        return serviceHistoryService.getAllServiceHistories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceHistoryDTO> getServiceHistoryById(@PathVariable Long id) {
        Optional<ServiceHistoryDTO> serviceHistoryDTO = serviceHistoryService.getServiceHistoryById(id);
        return serviceHistoryDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceHistoryDTO createServiceHistory(@RequestBody ServiceHistoryDTO serviceHistoryDTO) {
        return serviceHistoryService.createServiceHistory(serviceHistoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceHistoryDTO> updateServiceHistory(@PathVariable Long id, @RequestBody ServiceHistoryDTO serviceHistoryDTO) {
        return ResponseEntity.ok(serviceHistoryService.updateServiceHistory(id, serviceHistoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceHistory(@PathVariable Long id) {
        serviceHistoryService.deleteServiceHistory(id);
        return ResponseEntity.noContent().build();
    }
}
