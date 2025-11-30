package com.example.wigelltravels_.services;

import com.example.wigelltravels_.entities.*;
import com.example.wigelltravels_.exceptions.BusinessRuleException;
import com.example.wigelltravels_.exceptions.NotFoundException;
import com.example.wigelltravels_.repositories.BookingRepository;
import com.example.wigelltravels_.repositories.CustomerRepository;
import com.example.wigelltravels_.repositories.TravelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepo;
    private final TravelRepository travelRepo;
    private final CustomerRepository customerRepo;
    private final PriceCalculatorClient priceClient;

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepo,
                          TravelRepository travelRepo,
                          CustomerRepository customerRepo,
                          PriceCalculatorClient priceClient) {
        this.bookingRepo = bookingRepo;
        this.travelRepo = travelRepo;
        this.customerRepo = customerRepo;
        this.priceClient = priceClient;
    }

    @Transactional
    public Booking createBooking(Long customerId,
                                 Long travelId,
                                 LocalDate departure,
                                 int weeks,
                                 String username) {

        var customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        var travel = travelRepo.findById(travelId)
                .orElseThrow(() -> new NotFoundException("Travel not found"));

        if (!isAdmin() && !customer.getEmail().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("You are not allowed to create bookings for another customer");
        }

        if (!travel.isActive()) {
            throw new BusinessRuleException("Travel not available for booking");
        }
        if (weeks <= 0) {
            throw new BusinessRuleException("Weeks must be > 0");
        }

        int totalSek = travel.getPricePerWeekSek() * weeks;
        double totalEur = priceClient.convertSekToEur(totalSek);

        var booking = new Booking(customer, travel, departure, weeks, totalSek, totalEur, BookingStatus.ACTIVE);
        var saved = bookingRepo.save(booking);

        log.info("user {} booked {} weeks of travel to {} ({})",
                username, weeks, travel.getDestinationCity(), travel.getDestinationCountry());
        return saved;
    }

    @Transactional
    public Booking cancel(Long bookingId, String username) {
        var b = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!isAdmin() && !b.getCustomer().getEmail().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("You are not allowed to cancel this booking");
        }

        if (!b.getDepartureDate().isAfter(LocalDate.now().plusDays(7))) {
            throw new BusinessRuleException("Cancellation not allowed within 7 days before departure");
        }

        b.setStatus(BookingStatus.CANCELED);
        log.info("booking {} canceled by {}", bookingId, username);
        return b;
    }

    public List<Booking> bookingsByCustomer(Long customerId, String username) {
        var customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        if (!isAdmin() && !customer.getEmail().equalsIgnoreCase(username)) {
            throw new AccessDeniedException("You are not allowed to view bookings for another customer");
        }

        return bookingRepo.findByCustomerId(customerId);
    }

    public List<Booking> canceled() {
        return bookingRepo.findByStatus(BookingStatus.CANCELED);
    }

    public List<Booking> upcoming() {
        return bookingRepo.findByDepartureDateAfter(LocalDate.now());
    }

    public List<Booking> past() {
        return bookingRepo.findByDepartureDateBefore(LocalDate.now());
    }

    private boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}