package com.backend.serviceProvider.services;

import com.backend.serviceProvider.dtos.ServiceProviderDTO;
import com.backend.serviceProvider.entities.ServiceProvider;
import com.backend.serviceProvider.repositories.ServiceProviderRepository;
import com.backend.user.models.User;
import com.backend.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceProviderService {

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ServiceProviderDTO> getAllServiceProviders() {
        return serviceProviderRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ServiceProviderDTO> getServiceProviderById(Long id) {
        return serviceProviderRepository.findById(id).map(this::convertToDTO);
    }


    public ServiceProviderDTO createServiceProvider(ServiceProviderDTO serviceProviderDTO) {
        ServiceProvider serviceProvider = convertToEntity(serviceProviderDTO);
        User user = userRepository.findById(serviceProviderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        serviceProvider.setUser(user);
        return convertToDTO(serviceProviderRepository.save(serviceProvider));
    }

    public ServiceProviderDTO updateServiceProvider(Long id, ServiceProviderDTO serviceProviderDTO) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceProvider not found"));
        serviceProvider.setBusinessName(serviceProviderDTO.getBusinessName());
        serviceProvider.setServiceCategory(serviceProviderDTO.getServiceCategory());
        serviceProvider.setLocation(serviceProviderDTO.getLocation());
        serviceProvider.setVerified(serviceProviderDTO.getVerified());
        return convertToDTO(serviceProviderRepository.save(serviceProvider));
    }

    public void deleteServiceProvider(Long id) {
        serviceProviderRepository.deleteById(id);
    }

    private ServiceProviderDTO convertToDTO(ServiceProvider serviceProvider) {
        ServiceProviderDTO serviceProviderDTO = new ServiceProviderDTO();
        serviceProviderDTO.setId(serviceProvider.getId());
        serviceProviderDTO.setUserId(serviceProvider.getUser().getId());
        serviceProviderDTO.setBusinessName(serviceProvider.getBusinessName());
        serviceProviderDTO.setServiceCategory(serviceProvider.getServiceCategory());
        serviceProviderDTO.setLocation(serviceProvider.getLocation());
        serviceProviderDTO.setVerified(serviceProvider.getVerified());
        serviceProviderDTO.setCreatedAt(serviceProvider.getCreatedAt());
        return serviceProviderDTO;
    }

    private ServiceProvider convertToEntity(ServiceProviderDTO serviceProviderDTO) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setBusinessName(serviceProviderDTO.getBusinessName());
        serviceProvider.setServiceCategory(serviceProviderDTO.getServiceCategory());
        serviceProvider.setLocation(serviceProviderDTO.getLocation());
        serviceProvider.setVerified(serviceProviderDTO.getVerified());
        return serviceProvider;
    }
}
