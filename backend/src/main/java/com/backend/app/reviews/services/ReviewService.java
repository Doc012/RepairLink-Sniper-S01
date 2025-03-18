package com.backend.app.reviews.services;

import com.backend.app.bookings.entities.Booking;
import com.backend.app.bookings.repositories.BookingRepository;
import com.backend.app.reviews.dtos.ReviewDTO;
import com.backend.app.reviews.entities.Review;
import com.backend.app.reviews.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<ReviewDTO> getReviewById(Long id) {
        return reviewRepository.findById(id).map(this::convertToDTO);
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = convertToEntity(reviewDTO);
        return convertToDTO(reviewRepository.save(review));
    }

    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        return convertToDTO(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setBookingId(review.getBooking().getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        return reviewDTO;
    }

    private Review convertToEntity(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(java.sql.Timestamp.valueOf(LocalDateTime.now())); // Set the createdAt field        // Assuming you have a method to get a Booking by its ID
        Booking booking = bookingRepository.findById(reviewDTO.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        review.setBooking(booking);
        return review;
    }
}
