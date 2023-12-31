package com.spendwise.api.transactionmanagement.controller;

import com.spendwise.api.transactionmanagement.Util;
import com.spendwise.api.transactionmanagement.config.ApplicationConfig;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.model.entry.EntryTypeEnum;
import com.spendwise.api.transactionmanagement.service.category.CategoryServiceImpl;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryServiceImpl;
import com.spendwise.api.transactionmanagement.service.entry.EntryServiceImpl;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EntryController.class)
@AutoConfigureMockMvc(addFilters = false)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = ApplicationConfig.class)
class EntryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private EntryServiceImpl entryService;
    @MockBean
    private CategoryServiceImpl categoryService;
    @MockBean
    private EntryHasCategoryServiceImpl ehcService;

    Entry entry;
    Object bodyContent;

    @BeforeEach
    void setUp() {
        entry = Entry.builder()
                .creatorId(1L)
                .entryType(EntryTypeEnum.EXPENSE)
                .createdAt(Instant.parse("2022-12-03T10:15:30.00Z"))
                .updatedAt(Instant.parse("2022-12-03T10:15:30.00Z"))
                .amount(45000D)
                .title("Starbucks Latte")
                .description("My morning coffee")
                .categoryName("FNB")
                .build();
        bodyContent = new Object() {
          public final Long creatorId = 1L;
          public final String type = "EXPENSE";
          public final Instant createdAt = Instant.parse("2022-12-03T10:15:30.00Z");
          public final Instant updatedAt = Instant.parse("2022-12-03T10:15:30.00Z");
          public Double amount = 45000D;
          public String title = "Starbucks Latte";
          public String description = "My morning coffee";
        };
    }

    @Test
    @Disabled("Need more time to make this a proper unit test because of other MS dependencies")
    void testGetAllEntries() throws Exception {
        List<Entry> allEntries = List.of(entry);

        when(entryService.findAllEntries()).thenReturn(allEntries);

        mvc.perform(get("/api/v1/transactions/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllEntries"))
                .andExpect(jsonPath("$[0].title").value(entry.getTitle()));
        verify(entryService, atLeastOnce()).findAllEntries();
    }

    @Test
    @Disabled("Need more time to make this a proper unit test because of other MS dependencies")
    void testGetAllEntriesFromUser() throws Exception {
        List<Entry> allEntries = List.of(entry);

        when(entryService.findAllByCreatorId(any(Long.class))).thenReturn(allEntries);

        mvc.perform(get("/api/v1/transactions/1/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllEntriesFromUser"))
                .andExpect(jsonPath("$[0].title").value(entry.getTitle()));
        verify(entryService, atLeastOnce()).findAllByCreatorId(any(Long.class));
    }

    @Test
    @Disabled("Need more time to make this a proper unit test because of other MS dependencies")
    void testGetEntryById() throws Exception {
        when(entryService.findById(any(Long.class)))
                .thenReturn(entry);

        mvc.perform(get("/api/v1/transactions/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getEntry"))
                .andExpect(jsonPath("$.entryId").value(entry.getEntryId()));

        verify(entryService, atLeastOnce()).findById(any(Long.class));
    }

    @Test
    @Disabled("Need more time to make this a proper unit test because of other MS dependencies")
    void testCreateEntry() throws Exception {
        when(entryService.create(any(EntryRequest.class))).thenReturn(entry);

        mvc.perform(post("/api/v1/transactions/create").contentType(MediaType.APPLICATION_JSON)
                .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createEntry"))
                .andExpect(jsonPath("$.title").value(entry.getTitle()));
        verify(entryService, atLeastOnce()).create(any(EntryRequest.class));
    }

    @Test
    @Disabled("Need more time to make this a proper unit test because of other MS dependencies")
    void testPutEntry() throws Exception {
        when(entryService.update(any(Long.class), any(EntryRequest.class))).thenReturn(entry);

        mvc.perform(put("/api/v1/transactions/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateEntry"))
                .andExpect(jsonPath("$.title").value(entry.getTitle()));
        verify(entryService, atLeastOnce()).update(any(Long.class), any(EntryRequest.class));
    }

    @Test
    @Disabled("Need more time to make this a proper unit test because of other MS dependencies")
    void testDeleteEntry() throws Exception {
        mvc.perform(delete("/api/v1/transactions/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteEntry"));
        verify(entryService, atLeastOnce()).delete(any(Long.class));
    }
}
