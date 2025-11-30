package com.example.wigelltravels_.repositories;

import com.example.wigelltravels_.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
