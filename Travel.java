package com.example.wigelltravels_.entities;

import jakarta.persistence.*;

@Entity
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String destinationCity;

    @Column(length = 100, nullable = false)
    private String destinationCountry;

    @Column(length = 150, nullable = false)
    private String hotelName;

    @Column(nullable = false)
    private Integer pricePerWeekSek;

    @Column(nullable = false)
    private boolean isActive = true;

    public Travel() {}

    public Travel(String destinationCity, String destinationCountry,
                  String hotelName, Integer pricePerWeekSek, boolean isActive) {
        this.destinationCity = destinationCity;
        this.destinationCountry = destinationCountry;
        this.hotelName = hotelName;
        this.pricePerWeekSek = pricePerWeekSek;
        this.isActive = isActive;
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

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "Travel{" +
                "id=" + id +
                ", destinationCity='" + destinationCity + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", hotelName='" + hotelName + '\'' +
                ", pricePerWeekSek=" + pricePerWeekSek +
                ", isActive=" + isActive +
                '}';
    }
}
