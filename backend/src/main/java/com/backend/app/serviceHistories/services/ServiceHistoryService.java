package com.backend.app.serviceHistories.services;


import com.backend.app.bookings.entities.Booking;
import com.backend.app.bookings.repositories.BookingRepository;
import com.backend.app.serviceHistories.dtos.ServiceHistoryDTO;
import com.backend.app.serviceHistories.entities.ServiceHistory;
import com.backend.app.serviceHistories.repositories.ServiceHistoryRepository;
import com.backend.app.serviceProvider.entities.ServiceProvider;
import com.backend.app.serviceProvider.repositories.ServiceProviderRepository;
import com.backend.app.services.entities.ServiceEntity;
import com.backend.app.services.repositories.ServiceEntityRepository;
import com.backend.app.user.models.User;
import com.backend.app.user.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceHistoryService {

    @Autowired
    private ServiceHistoryRepository serviceHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private ServiceEntityRepository serviceRepository;

    public List<ServiceHistoryDTO> getAllServiceHistories() {
        return serviceHistoryRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ServiceHistoryDTO> getServiceHistoryById(Long id) {
        return serviceHistoryRepository.findById(id).map(this::convertToDTO);
    }

    public ServiceHistoryDTO createServiceHistory(ServiceHistoryDTO serviceHistoryDTO) {
        ServiceHistory serviceHistory = convertToEntity(serviceHistoryDTO);
        Booking booking = bookingRepository.findById(serviceHistoryDTO.getBookingId())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found"));
        serviceHistory.setBooking(booking);
        User customer = userRepository.findById(serviceHistoryDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        serviceHistory.setCustomer(customer);
        ServiceProvider provider = serviceProviderRepository.findById(serviceHistoryDTO.getProviderId())
                .orElseThrow(() -> new EntityNotFoundException("Provider not found"));
        serviceHistory.setProvider(provider);
        ServiceEntity service = serviceRepository.findById(serviceHistoryDTO.getServiceId())
                .orElseThrow(() -> new EntityNotFoundException("Service not found"));
        serviceHistory.setService(service);
        return convertToDTO(serviceHistoryRepository.save(serviceHistory));
    }

    public ServiceHistoryDTO updateServiceHistory(Long id, ServiceHistoryDTO serviceHistoryDTO) {
        ServiceHistory serviceHistory = serviceHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("ServiceHistory not found"));
        serviceHistory.setServiceDate(serviceHistoryDTO.getServiceDate());
        return convertToDTO(serviceHistoryRepository.save(serviceHistory));
    }

    public void deleteServiceHistory(Long id) {
        serviceHistoryRepository.deleteById(id);
    }

    private ServiceHistoryDTO convertToDTO(ServiceHistory serviceHistory) {
        ServiceHistoryDTO serviceHistoryDTO = new ServiceHistoryDTO();
        serviceHistoryDTO.setId(serviceHistory.getId());
        serviceHistoryDTO.setCustomerId(serviceHistory.getCustomer().getId());
        serviceHistoryDTO.setProviderId(serviceHistory.getProvider().getId());
        serviceHistoryDTO.setServiceId(serviceHistory.getService().getId());
        serviceHistoryDTO.setBookingId(serviceHistory.getBooking().getId());
        serviceHistoryDTO.setServiceDate(serviceHistory.getServiceDate());
        return serviceHistoryDTO;
    }

    private ServiceHistory convertToEntity(ServiceHistoryDTO serviceHistoryDTO) {
        ServiceHistory serviceHistory = new ServiceHistory();
        serviceHistory.setServiceDate(serviceHistoryDTO.getServiceDate());
        return serviceHistory;
    }
}
