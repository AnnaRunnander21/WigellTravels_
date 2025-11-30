package com.example.wigelltravels_.services;

import com.example.wigelltravels_.dto.CreateCustomerRequest;
import com.example.wigelltravels_.dto.CustomerDto;
import com.example.wigelltravels_.dto.UpdateCustomerRequest;
import com.example.wigelltravels_.entities.Customer;
import com.example.wigelltravels_.exceptions.NotFoundException;
import com.example.wigelltravels_.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }

    public List<CustomerDto> listAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public CustomerDto getById(Long id) {
        Customer c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
        return toDto(c);
    }

    public CustomerDto create(CreateCustomerRequest req) {
        Customer c = new Customer();
        c.setFirstName(req.getFirstName());
        c.setLastName(req.getLastName());
        c.setEmail(req.getEmail());
        c.setPhone(req.getPhone());

        Customer saved = repo.save(c);
        return toDto(saved);
    }

    public CustomerDto update(Long id, UpdateCustomerRequest req) {
        Customer c = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));

        c.setFirstName(req.getFirstName());
        c.setLastName(req.getLastName());
        c.setEmail(req.getEmail());
        c.setPhone(req.getPhone());

        Customer saved = repo.save(c);
        return toDto(saved);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Customer with id " + id + " not found");
        }
        repo.deleteById(id);
    }

    private CustomerDto toDto(Customer c) {
        CustomerDto dto = new CustomerDto();
        dto.setId(c.getId());
        dto.setFirstName(c.getFirstName());
        dto.setLastName(c.getLastName());
        dto.setEmail(c.getEmail());
        dto.setPhone(c.getPhone());
        return dto;
    }
}
