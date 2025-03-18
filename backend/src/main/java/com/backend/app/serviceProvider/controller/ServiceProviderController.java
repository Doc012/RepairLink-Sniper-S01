package com.backend.app.serviceProvider.controller;

import com.backend.app.serviceProvider.dtos.ServiceProviderDTO;
import com.backend.app.serviceProvider.services.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/serviceProviders")
public class ServiceProviderController {

    @Autowired
    private ServiceProviderService serviceProviderService;

    @GetMapping
    public List<ServiceProviderDTO> getAllServiceProviders() {
        return serviceProviderService.getAllServiceProviders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderDTO> getServiceProviderById(@PathVariable Long id) {
        Optional<ServiceProviderDTO> serviceProviderDTO = serviceProviderService.getServiceProviderById(id);
        return serviceProviderDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/location/{locationId}")
    public List<ServiceProviderDTO> getServiceProvidersByLocation(@PathVariable Long locationId) {
        return serviceProviderService.getServiceProvidersByLocation(locationId);
    }

    @PostMapping
    public ServiceProviderDTO createServiceProvider(@RequestBody ServiceProviderDTO serviceProviderDTO) {
        return serviceProviderService.createServiceProvider(serviceProviderDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceProviderDTO> updateServiceProvider(@PathVariable Long id, @RequestBody ServiceProviderDTO serviceProviderDTO) {
        return ResponseEntity.ok(serviceProviderService.updateServiceProvider(id, serviceProviderDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable Long id) {
        serviceProviderService.deleteServiceProvider(id);
        return ResponseEntity.noContent().build();
    }
}