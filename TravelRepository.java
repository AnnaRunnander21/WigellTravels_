package com.example.wigelltravels_.repositories;

import com.example.wigelltravels_.entities.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelRepository extends JpaRepository<Travel, Long> {

    List<Travel> findByIsActiveTrue();


}
