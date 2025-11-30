package com.example.wigelltravels_.dto;

import com.example.wigelltravels_.entities.Booking;
import com.example.wigelltravels_.entities.BookingStatus;

import java.time.LocalDate;

public class BookingResponse {

    private Long id;
    private String customerName;
    private String travelDestination;
    private String hotel;
    private LocalDate departureDate;
    private int weeks;
    private int totalPriceSek;
    private double totalPriceEur;
    private BookingStatus status;

    public BookingResponse() {
    }

    public BookingResponse(Long id, String customerName, String travelDestination,
                           String hotel, LocalDate departureDate, int weeks,
                           int totalPriceSek, double totalPriceEur, BookingStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.travelDestination = travelDestination;
        this.hotel = hotel;
        this.departureDate = departureDate;
        this.weeks = weeks;
        this.totalPriceSek = totalPriceSek;
        this.totalPriceEur = totalPriceEur;
        this.status = status;
    }

    public static BookingResponse from(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getCustomer().getFirstName() + " " + b.getCustomer().getLastName(),
                b.getTravel().getDestinationCity() + ", " + b.getTravel().getDestinationCountry(),
                b.getTravel().getHotelName(),
                b.getDepartureDate(),
                b.getWeeks(),
                b.getTotalPriceSek(),
                b.getTotalPriceEur(),
                b.getStatus()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getTravelDestination() { return travelDestination; }
    public void setTravelDestination(String travelDestination) { this.travelDestination = travelDestination; }

    public String getHotel() { return hotel; }
    public void setHotel(String hotel) { this.hotel = hotel; }

    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }

    public int getWeeks() { return weeks; }
    public void setWeeks(int weeks) { this.weeks = weeks; }

    public int getTotalPriceSek() { return totalPriceSek; }
    public void setTotalPriceSek(int totalPriceSek) { this.totalPriceSek = totalPriceSek; }

    public double getTotalPriceEur() { return totalPriceEur; }
    public void setTotalPriceEur(double totalPriceEur) { this.totalPriceEur = totalPriceEur; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "BookingResponse{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", travelDestination='" + travelDestination + '\'' +
                ", hotel='" + hotel + '\'' +
                ", departureDate=" + departureDate +
                ", weeks=" + weeks +
                ", totalPriceSek=" + totalPriceSek +
                ", totalPriceEur=" + totalPriceEur +
                ", status=" + status +
                '}';
    }
}
