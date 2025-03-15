package com.backend.services.services;

import com.backend.serviceProvider.entities.ServiceProvider;
import com.backend.serviceProvider.repositories.ServiceProviderRepository;
import com.backend.services.dtos.ServiceEntityDTO;
import com.backend.services.entities.ServiceEntity;
import com.backend.services.repositories.ServiceEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceEntityService {

    @Autowired
    private ServiceEntityRepository serviceRepository;

    @Autowired
    private ServiceProviderRepository providerRepository;

    public List<ServiceEntityDTO> getAllServices() {
        return serviceRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ServiceEntityDTO> getServiceById(Long id) {
        return serviceRepository.findById(id).map(this::convertToDTO);
    }

    public ServiceEntityDTO createService(ServiceEntityDTO serviceDTO) {
        ServiceEntity service = convertToEntity(serviceDTO);
        return convertToDTO(serviceRepository.save(service));
    }

    public ServiceEntityDTO updateService(Long id, ServiceEntityDTO serviceDTO) {
        ServiceEntity service = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        service.setServiceName(serviceDTO.getServiceName());
        service.setDescription(serviceDTO.getDescription());
        service.setPrice(serviceDTO.getPrice());
        service.setDuration(serviceDTO.getDuration());
        return convertToDTO(serviceRepository.save(service));
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }

    private ServiceEntityDTO convertToDTO(ServiceEntity service) {
        ServiceEntityDTO serviceDTO = new ServiceEntityDTO();
        serviceDTO.setId(service.getId());
        serviceDTO.setProviderId(service.getProvider().getId());
        serviceDTO.setServiceName(service.getServiceName());
        serviceDTO.setDescription(service.getDescription());
        serviceDTO.setPrice(service.getPrice());
        serviceDTO.setDuration(service.getDuration());
        serviceDTO.setCreatedAt(service.getCreatedAt());
        return serviceDTO;
    }

    private ServiceEntity convertToEntity(ServiceEntityDTO serviceDTO) {
        ServiceEntity service = new ServiceEntity();
        service.setServiceName(serviceDTO.getServiceName());
        service.setDescription(serviceDTO.getDescription());
        service.setPrice(serviceDTO.getPrice());
        service.setDuration(serviceDTO.getDuration());
        service.setCreatedAt(new java.sql.Timestamp(new java.util.Date().getTime())); // Set the createdAt field
        // Assuming you have a method to fetch the provider by ID
        ServiceProvider provider = providerRepository.findById(serviceDTO.getProviderId())
                .orElseThrow(() -> new RuntimeException("Provider not found"));
        service.setProvider(provider);
        return service;
    }
}