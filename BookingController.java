package com.example.wigelltravels_.controllers;

import com.example.wigelltravels_.dto.BookingRequest;
import com.example.wigelltravels_.dto.BookingResponse;
import com.example.wigelltravels_.entities.Booking;
import com.example.wigelltravels_.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/wigelltravels/v1")
public class BookingController {

    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/booktrip")
    public ResponseEntity<BookingResponse> book(@RequestBody BookingRequest req,
                                                Principal principal) {
        Booking booking = service.createBooking(
                req.getCustomerId(),
                req.getTravelId(),
                req.getDepartureDate(),
                req.getWeeks(),
                principal.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BookingResponse.from(booking));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/canceltrip")
    public ResponseEntity<BookingResponse> cancel(@RequestParam Long bookingId,
                                                  Principal principal) {
        Booking booking = service.cancel(bookingId, principal.getName());
        return ResponseEntity.ok(BookingResponse.from(booking));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/mybookings")
    public ResponseEntity<List<BookingResponse>> myBookings(@RequestParam Long customerId,
                                                            Principal principal) {
        List<Booking> bookings = service.bookingsByCustomer(customerId, principal.getName());

        List<BookingResponse> responses = bookings.stream()
                .map(BookingResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listcanceled")
    public ResponseEntity<List<BookingResponse>> listCanceled() {
        List<BookingResponse> responses = service.canceled().stream()
                .map(BookingResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listupcoming")
    public ResponseEntity<List<BookingResponse>> upcoming() {
        List<BookingResponse> responses = service.upcoming().stream()
                .map(BookingResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listpast")
    public ResponseEntity<List<BookingResponse>> past() {
        List<BookingResponse> responses = service.past().stream()
                .map(BookingResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }
}
