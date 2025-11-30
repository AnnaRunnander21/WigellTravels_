package com.example.wigelltravels_.controllers;

import com.example.wigelltravels_.dto.TravelDto;
import com.example.wigelltravels_.services.TravelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wigelltravels/v1")
public class TravelController {

    private final TravelService service;

    public TravelController(TravelService service) {
        this.service = service;
    }

    @GetMapping("/travels")
    public ResponseEntity<List<TravelDto>> listActiveTravels() {
        return ResponseEntity.ok(service.listAllActive());
    }

    @GetMapping("/admin/travels")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TravelDto>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @PostMapping("/admin/travels")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TravelDto> add(@RequestBody TravelDto dto) {
        TravelDto created = service.addTravel(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/admin/travels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TravelDto> update(@PathVariable Long id,
                                            @RequestBody TravelDto dto) {
        TravelDto updated = service.updateTravel(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/admin/travels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        service.removeTravel(id);
        return ResponseEntity.noContent().build();
    }
}

