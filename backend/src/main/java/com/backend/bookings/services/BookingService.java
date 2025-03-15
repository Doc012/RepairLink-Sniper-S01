package com.backend.bookings.services;

import com.backend.bookings.dtos.BookingDTO;
import com.backend.bookings.entities.Booking;
import com.backend.bookings.enums.BookingStatus;
import com.backend.bookings.repositories.BookingRepository;
import com.backend.serviceProvider.entities.ServiceProvider;
import com.backend.services.entities.ServiceEntity;
import com.backend.user.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<BookingDTO> getBookingById(Long id) {
        return bookingRepository.findById(id).map(this::convertToDTO);
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Booking booking = convertToEntity(bookingDTO);
        if (booking.getCustomer() == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        return convertToDTO(bookingRepository.save(booking));
    }

    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setStatus(bookingDTO.getStatus());
        return convertToDTO(bookingRepository.save(booking));
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomer().getId());
        bookingDTO.setServiceId(booking.getService().getId());
        bookingDTO.setProviderId(booking.getProvider().getId());
        bookingDTO.setBookingDate(booking.getBookingDate());
        bookingDTO.setStatus(BookingStatus.valueOf(booking.getStatus().name()));
        bookingDTO.setCreatedAt(booking.getCreatedAt());
        return bookingDTO;
    }

    private Booking convertToEntity(BookingDTO bookingDTO) {
        Booking booking = new Booking();
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setStatus(bookingDTO.getStatus());
        User customer = new User();
        customer.setId(bookingDTO.getCustomerId());
        booking.setCreatedAt(new java.sql.Timestamp(new java.util.Date().getTime()));
        ServiceProvider provider = new ServiceProvider();
        provider.setId(bookingDTO.getProviderId());
        booking.setProvider(provider);
        booking.setCustomer(customer);
        ServiceEntity service = new ServiceEntity();
        service.setId(bookingDTO.getServiceId());
        booking.setService(service);
        return booking;
    }
}