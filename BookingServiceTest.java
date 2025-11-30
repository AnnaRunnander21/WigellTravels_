package com.example.wigelltravels_.services;

import com.example.wigelltravels_.entities.*;
import com.example.wigelltravels_.exceptions.BusinessRuleException;
import com.example.wigelltravels_.exceptions.NotFoundException;
import com.example.wigelltravels_.repositories.BookingRepository;
import com.example.wigelltravels_.repositories.CustomerRepository;
import com.example.wigelltravels_.repositories.TravelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    BookingRepository bookingRepo;

    @Mock
    TravelRepository travelRepo;

    @Mock
    CustomerRepository customerRepo;

    @Mock
    PriceCalculatorClient priceClient;

    @InjectMocks
    BookingService service;

    private Customer customer;
    private Travel travel;
    private Booking booking;

    private final String OWNER_EMAIL = "user@example.com";
    private final String OTHER_EMAIL = "other@example.com";

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Anna");
        customer.setLastName("Runnander");
        customer.setEmail(OWNER_EMAIL);
        customer.setPhone("0701234567");

        travel = new Travel();
        travel.setId(10L);
        travel.setDestinationCity("Barcelona");
        travel.setDestinationCountry("Spain");
        travel.setHotelName("Hotel Sol");
        travel.setPricePerWeekSek(9500);
        travel.setActive(true);

        booking = new Booking();
        booking.setId(100L);
        booking.setCustomer(customer);
        booking.setTravel(travel);
        booking.setDepartureDate(LocalDate.now().plusDays(20));
        booking.setWeeks(2);
        booking.setTotalPriceSek(19000);
        booking.setTotalPriceEur(1700.0);
        booking.setStatus(BookingStatus.ACTIVE);

        setUserContext(OWNER_EMAIL, false);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private void setUserContext(String username, boolean admin) {
        var authorities = admin
                ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                : List.of(new SimpleGrantedAuthority("ROLE_USER"));

        var auth = new UsernamePasswordAuthenticationToken(username, "password", authorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    void createBooking_asOwner_success() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(travelRepo.findById(10L)).thenReturn(Optional.of(travel));
        when(priceClient.convertSekToEur(9500 * 2)).thenReturn(1700.0);
        when(bookingRepo.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0, Booking.class);
            b.setId(200L);
            return b;
        });

        LocalDate departure = LocalDate.now().plusDays(30);

        Booking result = service.createBooking(1L, 10L, departure, 2, OWNER_EMAIL);

        assertNotNull(result.getId());
        assertEquals(1L, result.getCustomer().getId());
        assertEquals(10L, result.getTravel().getId());
        assertEquals(19000, result.getTotalPriceSek());
        assertEquals(1700.0, result.getTotalPriceEur());
        assertEquals(BookingStatus.ACTIVE, result.getStatus());

        verify(customerRepo).findById(1L);
        verify(travelRepo).findById(10L);
        verify(priceClient).convertSekToEur(19000);
        verify(bookingRepo).save(any(Booking.class));
    }

    @Test
    void createBooking_throwsNotFound_whenCustomerMissing() {
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createBooking(1L, 10L, LocalDate.now().plusDays(10), 1, OWNER_EMAIL));

        verify(customerRepo).findById(1L);
        verifyNoInteractions(travelRepo);
        verifyNoInteractions(priceClient);
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void createBooking_throwsNotFound_whenTravelMissing() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(travelRepo.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createBooking(1L, 10L, LocalDate.now().plusDays(10), 1, OWNER_EMAIL));

        verify(customerRepo).findById(1L);
        verify(travelRepo).findById(10L);
        verifyNoInteractions(priceClient);
        verifyNoMoreInteractions(bookingRepo);
    }

    @Test
    void createBooking_throwsAccessDenied_whenUserIsNotOwner_andNotAdmin() {
        setUserContext(OTHER_EMAIL, false);

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(travelRepo.findById(10L)).thenReturn(Optional.of(travel));

        assertThrows(AccessDeniedException.class,
                () -> service.createBooking(1L, 10L, LocalDate.now().plusDays(10), 1, OTHER_EMAIL));

        verify(customerRepo).findById(1L);
        verify(travelRepo).findById(10L);
        verifyNoInteractions(priceClient);
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void createBooking_throwsBusinessRule_whenTravelInactive() {
        travel.setActive(false);

        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(travelRepo.findById(10L)).thenReturn(Optional.of(travel));

        assertThrows(BusinessRuleException.class,
                () -> service.createBooking(1L, 10L, LocalDate.now().plusDays(10), 1, OWNER_EMAIL));

        verify(customerRepo).findById(1L);
        verify(travelRepo).findById(10L);
        verifyNoInteractions(priceClient);
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void createBooking_throwsBusinessRule_whenWeeksIsZeroOrNegative() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(travelRepo.findById(10L)).thenReturn(Optional.of(travel));

        assertThrows(BusinessRuleException.class,
                () -> service.createBooking(1L, 10L, LocalDate.now().plusDays(10), 0, OWNER_EMAIL));

        verify(customerRepo).findById(1L);
        verify(travelRepo).findById(10L);
        verifyNoInteractions(priceClient);
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void cancel_asOwner_success_whenMoreThan7Days() {
        booking.setDepartureDate(LocalDate.now().plusDays(20));
        when(bookingRepo.findById(100L)).thenReturn(Optional.of(booking));

        Booking result = service.cancel(100L, OWNER_EMAIL);

        assertEquals(BookingStatus.CANCELED, result.getStatus());
        verify(bookingRepo).findById(100L);
    }

    @Test
    void cancel_throwsNotFound_whenBookingMissing() {
        when(bookingRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.cancel(999L, OWNER_EMAIL));

        verify(bookingRepo).findById(999L);
    }

    @Test
    void cancel_throwsAccessDenied_whenNotOwner_andNotAdmin() {
        setUserContext(OTHER_EMAIL, false);
        when(bookingRepo.findById(100L)).thenReturn(Optional.of(booking));

        assertThrows(AccessDeniedException.class,
                () -> service.cancel(100L, OTHER_EMAIL));

        verify(bookingRepo).findById(100L);
    }

    @Test
    void cancel_throwsBusinessRule_whenTooCloseToDeparture() {
        booking.setDepartureDate(LocalDate.now().plusDays(5));
        when(bookingRepo.findById(100L)).thenReturn(Optional.of(booking));

        assertThrows(BusinessRuleException.class,
                () -> service.cancel(100L, OWNER_EMAIL));

        verify(bookingRepo).findById(100L);
    }

    @Test
    void bookingsByCustomer_asOwner_success() {
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepo.findByCustomerId(1L)).thenReturn(List.of(booking));

        List<Booking> result = service.bookingsByCustomer(1L, OWNER_EMAIL);

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
        verify(customerRepo).findById(1L);
        verify(bookingRepo).findByCustomerId(1L);
    }

    @Test
    void bookingsByCustomer_throwsNotFound_whenCustomerMissing() {
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.bookingsByCustomer(1L, OWNER_EMAIL));

        verify(customerRepo).findById(1L);
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void bookingsByCustomer_throwsAccessDenied_whenNotOwner_andNotAdmin() {
        setUserContext(OTHER_EMAIL, false);
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(AccessDeniedException.class,
                () -> service.bookingsByCustomer(1L, OTHER_EMAIL));

        verify(customerRepo).findById(1L);
        verifyNoInteractions(bookingRepo);
    }

    @Test
    void canceled_returnsFromRepo() {
        when(bookingRepo.findByStatus(BookingStatus.CANCELED)).thenReturn(List.of(booking));

        List<Booking> result = service.canceled();

        assertEquals(1, result.size());
        verify(bookingRepo).findByStatus(BookingStatus.CANCELED);
    }

    @Test
    void upcoming_returnsFromRepo() {
        when(bookingRepo.findByDepartureDateAfter(any(LocalDate.class))).thenReturn(List.of(booking));

        List<Booking> result = service.upcoming();

        assertEquals(1, result.size());
        verify(bookingRepo).findByDepartureDateAfter(any(LocalDate.class));
    }

    @Test
    void past_returnsFromRepo() {
        when(bookingRepo.findByDepartureDateBefore(any(LocalDate.class))).thenReturn(List.of(booking));

        List<Booking> result = service.past();

        assertEquals(1, result.size());
        verify(bookingRepo).findByDepartureDateBefore(any(LocalDate.class));
    }
}

