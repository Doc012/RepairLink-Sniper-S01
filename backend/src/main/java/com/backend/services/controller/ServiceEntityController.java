package com.backend.services.controller;


import com.backend.services.dtos.ServiceEntityDTO;
import com.backend.services.services.ServiceEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceEntityController {

    @Autowired
    private ServiceEntityService serviceEntityService;

    @GetMapping
    public List<ServiceEntityDTO> getAllServices() {
        return serviceEntityService.getAllServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntityDTO> getServiceById(@PathVariable Long id) {
        Optional<ServiceEntityDTO> serviceDTO = serviceEntityService.getServiceById(id);
        return serviceDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceEntityDTO createService(@RequestBody ServiceEntityDTO serviceDTO) {
        return serviceEntityService.createService(serviceDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntityDTO> updateService(@PathVariable Long id, @RequestBody ServiceEntityDTO serviceDTO) {
        return ResponseEntity.ok(serviceEntityService.updateService(id, serviceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceEntityService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
