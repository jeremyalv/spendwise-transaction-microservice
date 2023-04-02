package com.spendwise.api.transactionmanagement.controller;

import com.spendwise.api.transactionmanagement.Util;
import com.spendwise.api.transactionmanagement.controller.EntryController;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.model.Entry;
import com.spendwise.api.transactionmanagement.model.EntryType;
import com.spendwise.api.transactionmanagement.service.EntryServiceImpl;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers = EntryController.class)
@AutoConfigureMockMvc
public class EntryControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EntryServiceImpl service;

    Entry entry;
    Object bodyContent;

    @BeforeEach
    void setUp() {
        entry = Entry.builder()
                .creatorId(1L)
                .type(EntryType.EXPENSE)
                .createdAt(Instant.parse("2022-12-03T10:15:30.00Z"))
                .updatedAt(Instant.parse("2022-12-03T10:15:30.00Z"))
                .amount(45000L)
                .title("Starbucks Latte")
                .description("My morning coffee")
                .build();
        bodyContent = new Object() {
          public final Long creatorId = 1L;
          public final String type = "EXPENSE";
          public final Instant createdAt = Instant.parse("2022-12-03T10:15:30.00Z");
          public final Instant updatedAt = Instant.parse("2022-12-03T10:15:30.00Z");
          public Long amount = 45000L;
          public String title = "Starbucks Latte";
          public String description = "My morning coffee";
        };
    }

    @Test
    void testGetAllEntries() throws Exception {
        List<Entry> allEntries = List.of(entry);

        when(service.findAllEntries()).thenReturn(allEntries);

        mvc.perform(get("/api/transactions/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllEntries"))
                .andExpect(jsonPath("$[0].title").value(entry.getTitle()));
        verify(service, atLeastOnce()).findById(any(Long.class));
    }

    @Test
    void testGetAllEntriesFromUser() throws Exception {
        List<Entry> allEntries = List.of(entry);

        when(service.findAllByCreatorId(any(Long.class))).thenReturn(allEntries);

        mvc.perform(get("/api/transactions/1/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllEntriesFromUser"))
                .andExpect(jsonPath("$[0].title").value(entry.getTitle()));
        verify(service, atLeastOnce()).findById(any(Long.class));
    }

    @Test
    void testAddEntry() throws Exception {
        when(service.create(any(EntryRequest.class))).thenReturn(entry);

        mvc.perform(post("/api/transactions/create").contentType(MediaType.APPLICATION_JSON)
                .content(Util.mapToJson(bodyContent))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addEntry"))
                .andExpect(jsonPath("$.title").value(entry.getTitle()));
        verify(service, atLeastOnce()).create(any(EntryRequest.class));

    }
}
