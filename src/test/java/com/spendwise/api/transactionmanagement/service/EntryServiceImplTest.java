package com.spendwise.api.transactionmanagement.service;

import com.spendwise.api.transactionmanagement.dto.CategoryRequest;
import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EntryHasCategoryDoesNotExistException;
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

import com.spendwise.api.transactionmanagement.service.category.CategoryServiceImpl;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryServiceImpl;
import com.spendwise.api.transactionmanagement.service.entry.EntryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
    private EntryServiceImpl entryService;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @InjectMocks
    private EntryHasCategoryServiceImpl ehcService;

    @Mock
    private EntryRepository entryRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private EntryHasCategoryRepository ehcRepository;

    Entry entry;
    Entry newEntry;
    Entry travelEntry;
    EntryRequest entryCreateRequest;
    EntryRequest entryUpdateRequest;
    CategoryRequest fnbCategoryRequest;
    CategoryRequest travelCategoryRequest;

    Category fnb;
    Category travel;

    EntryHasCategory EHCFnB;
    EntryHasCategory EHCTravel;


    @BeforeEach
    void setUp() {
        fnb = Category.builder()
                .categoryId(1L)
                .entryType(EntryTypeEnum.EXPENSE)
                .name("FNB")
                .isExpense(true)
                .expenseCategory(ExpenseCategory.FNB)
                .incomeCategory(IncomeCategory.NONE)
                .build();

        travel = Category.builder()
                .categoryId(2L)
                .entryType(EntryTypeEnum.EXPENSE)
                .name("TRAVEL")
                .isExpense(true)
                .expenseCategory(ExpenseCategory.TRAVEL)
                .incomeCategory(IncomeCategory.NONE)
                .build();

        entryCreateRequest = EntryRequest
                .builder()
                .creatorId(1L)
                .amount(60000D)
                .title("Starbucks Latte")
                .description("Nice coffee")
                .entryType("EXPENSE")
                .categoryName("FNB")
                .build();

        entryUpdateRequest = EntryRequest.builder()
                .creatorId(1L)
                .amount(2000000D)
                .title("Plane to Bali")
                .description("Fun!")
                .entryType("EXPENSE")
                .categoryName("TRAVEL")
                .build();

        fnbCategoryRequest = CategoryRequest.builder()
                .entryType("EXPENSE")
                .name("FnB")
                .isExpense(true)
                .expenseCategory("FNB")
                .incomeCategory("NONE")
                .build();

        entry = Entry.builder()
                .entryId(1L)
                .creatorId(1L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(60000D)
                .title("Starbucks Latte")
                .description("Nice coffee")
                .entryType(EntryTypeEnum.EXPENSE)
                .build();

        newEntry = Entry.builder()
                .entryId(1L)
                .creatorId(1L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(2000000D)
                .title("Plane to Bali")
                .description("Fun!")
                .entryType(EntryTypeEnum.EXPENSE)
                .build();

        travelEntry = Entry.builder()
                .entryId(2L)
                .creatorId(1L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(9000000D)
                .title("Plane to Japan")
                .description("Excited!")
                .entryType(EntryTypeEnum.EXPENSE)
                .build();

        EHCFnB = EntryHasCategory.builder()
                .entryId(1L)
                .categoryId(1L)
                .build();

        EHCTravel = EntryHasCategory.builder()
                .entryId(2L)
                .categoryId(2L)
                .build();
    }

    @Test
    void whenFindAllEntryShouldReturnListOfEntries() {
        List<Entry> allEntries = List.of(entry);

        when(entryRepository.findAll()).thenReturn(allEntries);

        List<Entry> result = entryService.findAllEntries();
        verify(entryRepository, atLeastOnce()).findAll();
        Assertions.assertEquals(allEntries, result);
    }

    @Test
    void whenFindAllEntryByCreatorIdShouldReturnCreatorListOfEntries() {
        List<Entry> allUserEntries = List.of(entry);
        Long creatorId = 1L;

        when(entryService.findAllByCreatorId(1L))
                .thenReturn(allUserEntries);

        List<Entry> result = entryService.findAllByCreatorId(1L);
        verify(entryRepository, atLeastOnce()).findByCreatorId(1L);
        Assertions.assertEquals(allUserEntries, result);
    }

    @Test
    void whenFindByIdAndFoundShouldReturnEntry() {
        when(entryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(entry));

        Entry result = entryService.findById(1L);
        Assertions.assertEquals(entry, result);
    }

    @Test
    void whenFindByIdAndNotFoundShouldThrowException() {
        when(entryRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntryDoesNotExistException.class, () -> {
            entryService.findById(999L);
        });
    }

    @Test
    void whenFindCategoryByNameAndFoundShouldReturnCategory() {
        when(categoryRepository.findByName(any(String.class)))
                .thenReturn(Optional.of(fnb));
        Category result = entryService.findCategoryByName("FnB");
        Assertions.assertEquals(fnb, result);
    }

    @Test
    void whenFindCategoryByNameAndNotFoundShouldThrowException() {
        when(categoryRepository.findByName(any(String.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(CategoryDoesNotExistException.class, () -> {
            entryService.findCategoryByName("ZZZ");
        });
    }

    @Test
    void whenFindEHCByEntryIdAndFoundShouldReturnEHC() {
        when(ehcRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(EHCFnB));
        EntryHasCategory result = entryService.findEHCByEntryId(1L);
        Assertions.assertEquals(EHCFnB, result);
    }

    @Test
    void whenFindEHCByEntryIdAndNotFoundShouldReturnThrowException() {
        when(ehcRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntryHasCategoryDoesNotExistException.class, () -> {
            entryService.findEHCByEntryId(999L);
        });
    }

    @Test
    void whenSavingCategorySuccessShouldInvokeSaveMethod() {
        when(categoryRepository.save(
                any(Category.class)))
                .thenReturn(fnb);

        categoryRepository.save(fnb);

        System.out.println(categoryRepository.findAll());

        verify(categoryRepository, atLeastOnce())
                .save(any(Category.class));
    }

//    @Test
//    void whenCreateEntryShouldReturnTheCreatedEntry() {
//        when(categoryRepository.save(
//                any(Category.class)))
//                .thenAnswer(i -> i.getArguments()[0]);
//
//        when(entryRepository.save(
//                any(Entry.class)))
//                .thenAnswer(invocation -> {
//                    Entry entry = invocation.getArgument(0, Entry.class);
////                    entry.setEntryId(1L);
//                    return entry;
//                });
//
//        Category category = categoryService.create(fnbCategoryRequest);
//        Entry result = entryService.create(entryCreateRequest);
//
//        verify(categoryRepository, atLeastOnce()).save(any(Category.class));
//        verify(entryRepository, atLeastOnce()).save(any(Entry.class));
//        verify(ehcRepository, atLeastOnce()).save(any(EntryHasCategory.class));
//
//        // Custom assertion ignoring the time fields
//        assertThat(result).usingRecursiveComparison()
//                .ignoringFields("createdAt", "updatedAt")
//                .isEqualTo(entry);
//    }

    @Test
    @Disabled("Need more time to make this a proper unit test because of other MS dependencies")
    void whenUpdateEntryAndFoundShouldReturnTheUpdatedEntry() {
        when(entryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(entry));
        when(entryRepository.save(any(Entry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Entry.class)
                );

        Entry result = entryService.update(1L, entryUpdateRequest);

        verify(entryRepository, atLeastOnce()).save(any(Entry.class));

        // Custom assertion ignoring the time fields
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(entry);
    }

    @Test
    void whenUpdateEntryAndNotFoundShouldThrowException() {
        when(entryRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntryDoesNotExistException.class, () -> {
            entryService.update(1L, entryCreateRequest);
        });
    }

    @Test
    void whenDeleteEntryAndFoundShouldCallDeleteByIdOnRepo() {
        when(entryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(entry));

        entryService.delete(0L);
        verify(entryRepository, atLeastOnce()).deleteById(any(Long.class));
    }

    @Test
    void whenDeleteEntryAndNotFoundShouldThrowException() {
        when(entryRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntryDoesNotExistException.class, () -> {
            entryService.delete(1L);
        });
    }
}
