package com.example.wigelltravels_.services;

import com.example.wigelltravels_.dto.TravelDto;
import com.example.wigelltravels_.entities.Travel;
import com.example.wigelltravels_.exceptions.NotFoundException;
import com.example.wigelltravels_.repositories.TravelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelService {

    private final TravelRepository repo;

    public TravelService(TravelRepository repo) {
        this.repo = repo;
    }

    public List<TravelDto> listAllActive() {
        return repo.findByIsActiveTrue()
                .stream()
                .map(TravelDto::from)
                .toList();
    }

    public List<TravelDto> listAll() {
        return repo.findAll()
                .stream()
                .map(TravelDto::from)
                .toList();
    }

    public TravelDto addTravel(TravelDto dto) {
        Travel t = new Travel();
        t.setDestinationCity(dto.getDestinationCity());
        t.setDestinationCountry(dto.getDestinationCountry());
        t.setHotelName(dto.getHotelName());
        t.setPricePerWeekSek(dto.getPricePerWeekSek());
        t.setActive(dto.isActive());

        Travel saved = repo.save(t);
        return TravelDto.from(saved);
    }

    public TravelDto updateTravel(Long id, TravelDto dto) {
        Travel t = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Travel with id " + id + " not found"));

        t.setDestinationCity(dto.getDestinationCity());
        t.setDestinationCountry(dto.getDestinationCountry());
        t.setHotelName(dto.getHotelName());
        t.setPricePerWeekSek(dto.getPricePerWeekSek());
        t.setActive(dto.isActive());

        Travel saved = repo.save(t);
        return TravelDto.from(saved);
    }

    public void removeTravel(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Travel with id " + id + " not found");
        }
        repo.deleteById(id);
    }

}
