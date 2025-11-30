package com.example.wigelltravels_.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToOne(optional = false)
    private Travel travel;

    @Column(nullable = false)
    private LocalDate departureDate;

    @Column(nullable = false)
    private int weeks;

    @Column(nullable = false)
    private int totalPriceSek;

    @Column(nullable = false)
    private double totalPriceEur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status = BookingStatus.ACTIVE;

    public Booking() {}

    public Booking(Customer customer, Travel travel, LocalDate departureDate, int weeks,
                   int totalPriceSek, double totalPriceEur, BookingStatus status) {
        this.customer = customer;
        this.travel = travel;
        this.departureDate = departureDate;
        this.weeks = weeks;
        this.totalPriceSek = totalPriceSek;
        this.totalPriceEur = totalPriceEur;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Travel getTravel() { return travel; }
    public void setTravel(Travel travel) { this.travel = travel; }

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
        return "Booking{" +
                "id=" + id +
                ", customer=" + (customer != null ? customer.getId() : null) +
                ", travel=" + (travel != null ? travel.getId() : null) +
                ", departureDate=" + departureDate +
                ", weeks=" + weeks +
                ", totalPriceSek=" + totalPriceSek +
                ", totalPriceEur=" + totalPriceEur +
                ", status=" + status +
                '}';
    }
}
