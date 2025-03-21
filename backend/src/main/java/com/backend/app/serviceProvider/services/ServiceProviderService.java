package com.backend.app.serviceProvider.services;

import com.backend.app.locations.entities.Location;
import com.backend.app.locations.repositories.LocationRepository;
import com.backend.app.serviceProvider.dtos.ServiceProviderDTO;
import com.backend.app.serviceProvider.entities.ServiceProvider;
import com.backend.app.serviceProvider.repositories.ServiceProviderRepository;
import com.backend.app.user.enums.Role;
import com.backend.app.user.models.User;
import com.backend.app.user.repositories.UserRepository;
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

    @Autowired
    private LocationRepository locationRepository;

    public List<ServiceProviderDTO> getAllServiceProviders() {
        return serviceProviderRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ServiceProviderDTO> getServiceProviderById(Long id) {
        return serviceProviderRepository.findById(id).map(this::convertToDTO);
    }

    public List<ServiceProviderDTO> getServiceProvidersByLocation(Long locationId) {
        return serviceProviderRepository.findByLocationId(locationId).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ServiceProviderDTO createServiceProvider(ServiceProviderDTO serviceProviderDTO) {
        ServiceProvider serviceProvider = convertToEntity(serviceProviderDTO);
        User user = userRepository.findById(serviceProviderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.SERVICE_PROVIDER) {
            throw new RuntimeException("User does not have the required role to create a service provider");
        }

        serviceProvider.setUser(user);
        Location location = locationRepository.findById(serviceProviderDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        serviceProvider.setLocation(location);
        return convertToDTO(serviceProviderRepository.save(serviceProvider));
    }

    public ServiceProviderDTO updateServiceProvider(Long id, ServiceProviderDTO serviceProviderDTO) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceProvider not found"));
        serviceProvider.setBusinessName(serviceProviderDTO.getBusinessName());
        serviceProvider.setServiceCategory(serviceProviderDTO.getServiceCategory());
        Location location = locationRepository.findById(serviceProviderDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        serviceProvider.setLocation(location);
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
        serviceProviderDTO.setLocationId(serviceProvider.getLocation().getId());
        serviceProviderDTO.setLocationName(serviceProvider.getLocation().getName());
        serviceProviderDTO.setLocationRegion(serviceProvider.getLocation().getRegion());
        serviceProviderDTO.setVerified(serviceProvider.getVerified());
        serviceProviderDTO.setCreatedAt(serviceProvider.getCreatedAt());
        return serviceProviderDTO;
    }

    private ServiceProvider convertToEntity(ServiceProviderDTO serviceProviderDTO) {
        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.setBusinessName(serviceProviderDTO.getBusinessName());
        serviceProvider.setServiceCategory(serviceProviderDTO.getServiceCategory());
        Location location = locationRepository.findById(serviceProviderDTO.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));
        serviceProvider.setLocation(location);
        serviceProvider.setVerified(serviceProviderDTO.getVerified());
        return serviceProvider;
    }
}