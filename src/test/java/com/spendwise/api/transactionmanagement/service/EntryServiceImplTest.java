package com.spendwise.api.transactionmanagement.service;

import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.Entry;
import com.spendwise.api.transactionmanagement.model.EntryType;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;

import com.spendwise.api.transactionmanagement.repository.EntryRepository;
import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class EntryServiceImplTest {
    @InjectMocks
    private EntryServiceImpl service;

    @Mock
    private EntryRepository repository;

    Entry entry;
    Entry newEntry;
    EntryRequest createRequest;
    EntryRequest updateRequest;

    @BeforeEach
    void setUp() {
        createRequest = EntryRequest.builder()
                .creatorId(1L)
                .type("EXPENSE")
                .amount(60000L)
                .title("Starbucks Latte")
                .description("Sbux Tebet")
                .build();

        updateRequest = EntryRequest.builder()
                .creatorId(1L)
                .type("EXPENSE")
                .amount(30000L)
                .title("Janjiw Latte")
                .description("Janjiw Depok")
                .build();

        entry = Entry.builder()
                .entryId(1L)
                .creatorId(1L)
                .type(EntryType.EXPENSE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(60000L)
                .title("Starbucks Latte")
                .description("Sbux Tebet")
                .build();

        newEntry = Entry.builder()
                .entryId(1L)
                .creatorId(1L)
                .type(EntryType.EXPENSE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(30000L)
                .title("Janjiw Latte")
                .description("Janjiw Depok")
                .build();
    }

    @Test
    void whenFindAllEntryShouldReturnListOfEntries() {
        List<Entry> allEntries = List.of(entry);

        when(repository.findAll()).thenReturn(allEntries);

        List<Entry> result = service.findAllEntries();
        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(allEntries, result);
    }

    @Test
    void whenFindAllEntryByCreatorIdShouldReturnCreatorListOfEntries() {
        List<Entry> allEntries = List.of(entry);
        Long creatorId = 1L;

        when(repository.findAll().stream().filter(
                entry -> entry
                        .getCreatorId()
                        .equals(creatorId))
                .collect(Collectors.toList())
            ).thenReturn(allEntries);

        List<Entry> result = service.findAllByCreatorId(1L);
        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(allEntries, result);
    }

    @Test
    void whenFindByIdAndFoundShouldReturnEntry() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(entry));

        Entry result = service.findById(1L);
        Assertions.assertEquals(entry, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntryDoesNotExistException.class, () -> {
            service.findById(1L);
        });
    }

    @Test
    void whenCreateEntryShouldReturnTheCreatedEntry() {
        when(repository.save(any(Entry.class)))
                .thenAnswer(invocation -> {
                    Entry entry = invocation.getArgument(0, Entry.class);
                    entry.setEntryId(1L);

                    return entry;
                });

        Entry result = service.create(createRequest);
        verify(repository, atLeastOnce()).save(any(Entry.class));

        // Custom assertion ignoring the time fields
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(entry);
    }

    @Test
    void whenUpdateEntryAndFoundShouldReturnTheUpdatedEntry() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(entry));
        when(repository.save(any(Entry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Entry.class)
                );

        Entry result = service.update(1L, updateRequest);
        verify(repository, atLeastOnce()).save(any(Entry.class));
        // Custom assertion ignoring the time fields
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(entry);
    }

    @Test
    void whenUpdateEntryAndNotFoundShouldThrowException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntryDoesNotExistException.class, () -> {
            service.update(1L, createRequest);
        });
    }

    @Test
    void whenDeleteEntryAndFoundShouldCallDeleteByIdOnRepo() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.of(entry));

        service.delete(0L);
        verify(repository, atLeastOnce()).deleteById(any(Long.class));
    }

    @Test
    void whenDeleteEntryAndNotFoundShouldThrowException() {
        when(repository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntryDoesNotExistException.class, () -> {
            service.delete(1L);
        });
    }
}
