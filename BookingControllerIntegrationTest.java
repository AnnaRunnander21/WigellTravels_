package com.example.wigelltravels_.controllers;

import com.example.wigelltravels_.config.SecurityConfig;
import com.example.wigelltravels_.services.BookingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import({SecurityConfig.class, BookingControllerIntegrationTest.TestBeans.class})
class BookingControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookingService bookingService;

    @TestConfiguration
    static class TestBeans {
        @Bean
        BookingService bookingService() {
            return Mockito.mock(BookingService.class);
        }
    }

    @Test
    @DisplayName("GET /listcanceled som ADMIN: 200")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listCanceled_admin_ok_emptyList() throws Exception {
        when(bookingService.canceled()).thenReturn(List.of());

        mockMvc.perform(get("/api/wigelltravels/v1/listcanceled")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /listupcoming som ADMIN: 200")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listUpcoming_admin_ok_emptyList() throws Exception {
        when(bookingService.upcoming()).thenReturn(List.of());

        mockMvc.perform(get("/api/wigelltravels/v1/listupcoming")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /listpast som ADMIN: 200")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void listPast_admin_ok_emptyList() throws Exception {
        when(bookingService.past()).thenReturn(List.of());

        mockMvc.perform(get("/api/wigelltravels/v1/listpast")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("GET /listcanceled som USER: 403 Forbidden")
    @WithMockUser(username = "user", roles = {"USER"})
    void listCanceled_user_forbidden() throws Exception {
        mockMvc.perform(get("/api/wigelltravels/v1/listcanceled")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /listupcoming som USER: 403 Forbidden")
    @WithMockUser(username = "user", roles = {"USER"})
    void listUpcoming_user_forbidden() throws Exception {
        mockMvc.perform(get("/api/wigelltravels/v1/listupcoming")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /listpast som USER: 403 Forbidden")
    @WithMockUser(username = "user", roles = {"USER"})
    void listPast_user_forbidden() throws Exception {
        mockMvc.perform(get("/api/wigelltravels/v1/listpast")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /listcanceled utan login: 401 Unauthorized")
    void listCanceled_unauthenticated_unauthorized() throws Exception {
        mockMvc.perform(get("/api/wigelltravels/v1/listcanceled")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
