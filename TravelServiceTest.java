package com.example.wigelltravels_.services;

import com.example.wigelltravels_.dto.TravelDto;
import com.example.wigelltravels_.entities.Travel;
import com.example.wigelltravels_.exceptions.NotFoundException;
import com.example.wigelltravels_.repositories.TravelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TravelServiceTest {

    @Mock
    private TravelRepository repo;

    @InjectMocks
    private TravelService service;

    private Travel barcelonaEntity;

    @BeforeEach
    void setup() {
        barcelonaEntity = new Travel();
        barcelonaEntity.setId(1L);
        barcelonaEntity.setDestinationCity("Barcelona");
        barcelonaEntity.setDestinationCountry("Spain");
        barcelonaEntity.setHotelName("Hotel Sol");
        barcelonaEntity.setPricePerWeekSek(9500);
        barcelonaEntity.setActive(true);
    }

    @Test
    void listAllActive_returnsFromRepo_asDtos() {
        when(repo.findByIsActiveTrue()).thenReturn(List.of(barcelonaEntity));

        var list = service.listAllActive();

        assertEquals(1, list.size());
        TravelDto dto = list.get(0);
        assertEquals("Barcelona", dto.getDestinationCity());
        assertEquals("Spain", dto.getDestinationCountry());
        assertTrue(dto.isActive());

        verify(repo).findByIsActiveTrue();
    }

    @Test
    void listAllActive_returnsEmptyList_whenNoActiveTravels() {
        when(repo.findByIsActiveTrue()).thenReturn(List.of());

        var list = service.listAllActive();

        assertNotNull(list);
        assertTrue(list.isEmpty());
        verify(repo).findByIsActiveTrue();
    }

    @Test
    void listAll_returnsFromRepo_asDtos() {
        when(repo.findAll()).thenReturn(List.of(barcelonaEntity));

        var list = service.listAll();

        assertEquals(1, list.size());
        assertEquals("Hotel Sol", list.get(0).getHotelName());
        verify(repo).findAll();
    }

    @Test
    void listAll_returnsEmptyList_whenNoTravels() {
        when(repo.findAll()).thenReturn(List.of());

        var list = service.listAll();

        assertNotNull(list);
        assertTrue(list.isEmpty());
        verify(repo).findAll();
    }

    @Test
    void addTravel_savesAndReturnsDto() {
        TravelDto request = new TravelDto(
                null,
                "Barcelona",
                "Spain",
                "Hotel Sol",
                9500,
                true
        );

        when(repo.save(any(Travel.class))).thenAnswer(inv -> {
            Travel t = inv.getArgument(0, Travel.class);
            t.setId(1L);
            return t;
        });

        TravelDto saved = service.addTravel(request);

        assertNotNull(saved.getId());
        assertEquals("Hotel Sol", saved.getHotelName());
        assertEquals("Barcelona", saved.getDestinationCity());
        verify(repo).save(any(Travel.class));
    }

    @Test
    void updateTravel_updatesFields_whenFound() {
        TravelDto request = new TravelDto(
                null,
                "Barcelona",
                "Spain",
                "Hotel Sol Deluxe",
                9900,
                false
        );

        when(repo.findById(1L)).thenReturn(Optional.of(barcelonaEntity));
        when(repo.save(any(Travel.class))).thenAnswer(inv -> inv.getArgument(0));

        TravelDto result = service.updateTravel(1L, request);

        assertEquals("Hotel Sol Deluxe", result.getHotelName());
        assertEquals(9900, result.getPricePerWeekSek());
        assertFalse(result.isActive());
        verify(repo).findById(1L);
        verify(repo).save(barcelonaEntity);
    }

    @Test
    void updateTravel_throws_whenNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        TravelDto request = new TravelDto();

        assertThrows(NotFoundException.class, () -> service.updateTravel(99L, request));

        verify(repo).findById(99L);
        verify(repo, never()).save(any());
    }

    @Test
    void removeTravel_deletesById_whenExists() {
        when(repo.existsById(1L)).thenReturn(true);

        service.removeTravel(1L);

        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void removeTravel_throws_whenNotFound() {
        when(repo.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.removeTravel(99L));

        verify(repo).existsById(99L);
        verify(repo, never()).deleteById(anyLong());
    }
}