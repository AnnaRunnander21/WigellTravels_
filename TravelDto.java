package com.example.wigelltravels_.dto;

import com.example.wigelltravels_.entities.Travel;

public class TravelDto {

    private Long id;
    private String destinationCity;
    private String destinationCountry;
    private String hotelName;
    private Integer pricePerWeekSek;
    private boolean active;

    public TravelDto() {
    }

    public TravelDto(Long id, String destinationCity, String destinationCountry,
                     String hotelName, Integer pricePerWeekSek, boolean active) {
        this.id = id;
        this.destinationCity = destinationCity;
        this.destinationCountry = destinationCountry;
        this.hotelName = hotelName;
        this.pricePerWeekSek = pricePerWeekSek;
        this.active = active;
    }

    public static TravelDto from(Travel t) {
        return new TravelDto(
                t.getId(),
                t.getDestinationCity(),
                t.getDestinationCountry(),
                t.getHotelName(),
                t.getPricePerWeekSek(),
                t.isActive()
        );
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public String getDestinationCountry() { return destinationCountry; }
    public void setDestinationCountry(String destinationCountry) { this.destinationCountry = destinationCountry; }

    public String getHotelName() { return hotelName; }
    public void setHotelName(String hotelName) { this.hotelName = hotelName; }

    public Integer getPricePerWeekSek() { return pricePerWeekSek; }
    public void setPricePerWeekSek(Integer pricePerWeekSek) { this.pricePerWeekSek = pricePerWeekSek; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "TravelDto{" +
                "id=" + id +
                ", destinationCity='" + destinationCity + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", pricePerWeekSek=" + pricePerWeekSek +
                ", active=" + active +
                '}';
    }
}
