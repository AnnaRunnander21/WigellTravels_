package com.example.wigelltravels_.dto;

import java.time.LocalDate;

public class BookingRequest {

    private Long customerId;
    private Long travelId;
    private LocalDate departureDate;
    private int weeks;

    public BookingRequest() {
    }

    public BookingRequest(Long customerId, Long travelId, LocalDate departureDate, int weeks) {
        this.customerId = customerId;
        this.travelId = travelId;
        this.departureDate = departureDate;
        this.weeks = weeks;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getTravelId() {
        return travelId;
    }

    public void setTravelId(Long travelId) {
        this.travelId = travelId;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    @Override
    public String toString() {
        return "BookingRequest{" +
                "customerId=" + customerId +
                ", travelId=" + travelId +
                ", departureDate=" + departureDate +
                ", weeks=" + weeks +
                '}';
    }
}
