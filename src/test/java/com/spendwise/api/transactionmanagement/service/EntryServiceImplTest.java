package com.spendwise.api.transactionmanagement.service;

import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EHCDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;

import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.model.category.ExpenseCategory;
import com.spendwise.api.transactionmanagement.model.category.IncomeCategory;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.model.entry.EntryTypeEnum;
import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import com.spendwise.api.transactionmanagement.repository.EntryHasCategoryRepository;
import com.spendwise.api.transactionmanagement.repository.EntryRepository;

import com.spendwise.api.transactionmanagement.service.entry.EntryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private EntryHasCategoryRepository entryHasCategoryRepository;

    Entry entry;
    Entry newEntry;
    Entry travelEntry;
    EntryRequest createRequest;
    EntryRequest updateRequest;

    Category fnb;
    Category travel;

    EntryHasCategory EHCFnB;
    EntryHasCategory EHCTravel;


    @BeforeEach
    void setUp() {
        fnb = Category.builder()
                .categoryId(1L)
                .entryType(EntryTypeEnum.EXPENSE)
                .name("FnB")
                .isExpense(true)
                .expenseCategory(ExpenseCategory.FNB)
                .incomeCategory(IncomeCategory.NONE)
                .build();

        travel = Category.builder()
                .categoryId(2L)
                .entryType(EntryTypeEnum.EXPENSE)
                .name("Travel")
                .isExpense(true)
                .expenseCategory(ExpenseCategory.TRAVEL)
                .incomeCategory(IncomeCategory.NONE)
                .build();

        EHCFnB = EntryHasCategory.builder()
                .entryId(1L)
                .categoryId(1L)
                .build();

        EHCTravel = EntryHasCategory.builder()
                .entryId(2L)
                .categoryId(2L)
                .build();

        createRequest = EntryRequest.builder()
                .creatorId(1L)
                .amount(60000L)
                .title("Starbucks Latte")
                .description("Matraman")
                .entryType("EXPENSE")
                .categoryName("FnB")
                .build();

        updateRequest = EntryRequest.builder()
                .creatorId(1L)
                .amount(2000000L)
                .title("Plane to Bali")
                .description("Fun!")
                .entryType("EXPENSE")
                .categoryName("Travel")
                .build();

        entry = Entry.builder()
                .entryId(1L)
                .creatorId(1L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(60000L)
                .title("Starbucks Latte")
                .description("Matraman")
                .type(EntryTypeEnum.EXPENSE)
                .build();

        newEntry = Entry.builder()
                .entryId(1L)
                .creatorId(1L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(2000000L)
                .title("Plane to Bali")
                .description("Fun!")
                .type(EntryTypeEnum.EXPENSE)
                .build();

        travelEntry = Entry.builder()
                .entryId(2L)
                .creatorId(1L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(9000000L)
                .title("Plane to Japan")
                .description("Excited!")
                .type(EntryTypeEnum.EXPENSE)
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
            service.findById(999L);
        });
    }

    @Test
    void whenFindCategoryByNameAndFoundShouldReturnCategory() {
        when(categoryRepository.findByName(any(String.class)))
                .thenReturn(Optional.of(fnb));
        Category result = service.findCategoryByName("FnB");
        Assertions.assertEquals(fnb, result);
    }

    @Test
    void whenFindCategoryByNameAndNotFoundShouldThrowException() {
        when(categoryRepository.findByName(any(String.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(CategoryDoesNotExistException.class, () -> {
            service.findCategoryByName("ZZZ");
        });
    }

    @Test
    void whenFindEHCByEntryIdAndFoundShouldReturnEHC() {
        when(entryHasCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(EHCFnB));
        EntryHasCategory result = service.findEHCByEntryId(1L);
        Assertions.assertEquals(EHCFnB, result);
    }

    @Test
    void whenFindEHCByEntryIdAndNotFoundShouldReturnThrowException() {
        when(entryHasCategoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EHCDoesNotExistException.class, () -> {
            service.findEHCByEntryId(999L);
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
        verify(entryHasCategoryRepository, atLeastOnce()).save(any(EntryHasCategory.class));

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
        when(entryHasCategoryRepository.save(any(EntryHasCategory.class))).thenAnswer(invocation -> invocation.getArgument(0, EntryHasCategory.class));

        Entry result = service.update(1L, updateRequest);

        verify(repository, atLeastOnce()).save(any(Entry.class));
        verify(entryHasCategoryRepository, atLeastOnce()).save(any(EntryHasCategory.class));

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
