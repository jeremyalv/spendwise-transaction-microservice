package com.spendwise.api.transactionmanagement.service.entry;

import com.spendwise.api.transactionmanagement.dto.EHCRequest;
import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EntryHasCategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.analytics.ExpenseRequest;
import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import com.spendwise.api.transactionmanagement.repository.EntryHasCategoryRepository;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryService;
import com.spendwise.api.transactionmanagement.service.AuthService;


import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.model.entry.EntryTypeEnum;
import com.spendwise.api.transactionmanagement.repository.EntryRepository;
import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final EntryHasCategoryService ehcService;
    private final EntryRepository entryRepository;
    private final CategoryRepository categoryRepository;
    private final EntryHasCategoryRepository ehcRepository;

    private final RestTemplate restTemplate;

    private String analyticsCreateExpenseURL = "http://localhost:8082/api/v1/analytics/expense/add";
    private String analyticsCreateIncomeURL = "http://localhost:8082/api/v1/analytics/income/add";

    @Override
    public List<Entry> findAllEntries() {
        return entryRepository.findAll();
    }

    @Override
    public List<Entry> findAllByCreatorId(Long creatorId) {
        return entryRepository.findByCreatorId(creatorId);
    }

    @Override
    public Entry findById(Long id) {
        Optional<Entry> optionalEntry = entryRepository.findById(id);

        if (optionalEntry.isPresent()) {
            return optionalEntry.get();
        }
        else {
            throw new EntryDoesNotExistException(id);
        }
    }


    @Override
    public Entry create(EntryRequest request) {
        Entry entry = Entry.builder()
                .creatorId(request.getCreatorId()) // TODO: To get from user object directly
                .entryType(EntryTypeEnum.valueOf(request.getEntryType()))
                .categoryName(request.getCategoryName())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(request.getAmount())
                .title(request.getTitle())
                .description(request.getDescription())
                .build(); // NOSONAR

        return entryRepository.save(entry);
    }

    @Override
    public Entry update(Long entryId, EntryRequest request) {
        if (isEntryDoesNotExist(entryId)) {
            throw new EntryDoesNotExistException(entryId);
        }

        Entry entry = findById(entryId);

        entry.setCreatorId(request.getCreatorId()); // TODO: To get from user object directly
        entry.setEntryType(EntryTypeEnum.valueOf(request.getEntryType()));
        entry.setCategoryName(request.getCategoryName());
        entry.setUpdatedAt(Instant.now());
        entry.setAmount(request.getAmount());
        entry.setTitle(request.getTitle());
        entry.setDescription(request.getDescription());

        return entryRepository.save(entry);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (isEntryDoesNotExist(id)) {
            throw new EntryDoesNotExistException(id);
        }

        entryRepository.deleteById(id);
    }

    private boolean isEntryDoesNotExist(Long id) {
        return entryRepository.findById(id).isEmpty();
    }

    public Category findCategoryByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);

        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        else {
            throw new CategoryDoesNotExistException(name);
        }
    }

    public EntryHasCategory findEHCByEntryId(Long entryId) {
        Optional<EntryHasCategory> optionalEHC = ehcRepository.findById(entryId);

        if (optionalEHC.isPresent()) {
            return optionalEHC.get();
        }
        else {
            throw new EntryHasCategoryDoesNotExistException(entryId);
        }
    }

    public EHCRequest createEHCRequest(Entry entry, Category category) {
        EHCRequest ehcRequest = new EHCRequest();

        ehcRequest.setEntryId(entry.getEntryId());
        ehcRequest.setCategoryId(category.getCategoryId());

        return ehcRequest;
    }

    @Override
    public EntryHasCategory createEHC(EntryHasCategoryService ehcService, Entry entry, EntryRequest request) {
        Category category = findCategoryByName(request.getCategoryName());

        EHCRequest ehcRequest = createEHCRequest(entry, category);

        return ehcService.create(ehcRequest);
    }

    @Override
    public EntryHasCategory updateEHC(EntryHasCategoryService ehcService, Entry entry, EntryRequest request) {
        Category category = findCategoryByName(request.getCategoryName());

        EHCRequest ehcRequest = createEHCRequest(entry, category);

        return ehcService.update(entry.getEntryId(), ehcRequest);
    }

    // TODO add isUserDoesNotExist which checks the UserRepository from Auth service

    @Override
    public Map<String, Object> createAnalyticsEntry(Entry entry) {
        boolean isExpense = entry.getEntryType().toString() == "EXPENSE" ? true : false;
        Category category = getCategoryFromEntry(entry);

        String url = isExpense ? analyticsCreateExpenseURL : analyticsCreateIncomeURL;

        Map<String, Object> payload = new HashMap<>();
        payload.put("userId", entry.getCreatorId());
        payload.put("instant", entry.getCreatedAt());
        payload.put("category", entry.getCategoryName());
        payload.put("expenseAmount", entry.getAmount());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return payload;
    }
//    @Override
//    public String createAnalyticsEntry(Entry entry) {
//        boolean isExpense = entry.getEntryType().toString() == "EXPENSE" ? true : false;
//        Category category = getCategoryFromEntry(entry);
//
//        String url = isExpense ? analyticsCreateExpenseURL : analyticsCreateIncomeURL;
//
//        ExpenseRequest payload = ExpenseRequest.builder()
//                .userId(entry.getCreatorId())
//                .instant(entry.getCreatedAt())
//                .category(category.getName())
//                .expenseAmount(entry.getAmount())
//                .build();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<ExpenseRequest> request = new HttpEntity<>(payload, headers);
//
//        String response = restTemplate.postForObject(url, request, String.class);
//
//        return response;
//    }

    private Category getCategoryFromEntry(Entry entry) {
        Optional<EntryHasCategory> ehcOpt = ehcRepository.findByEntryId(entry.getEntryId());
        if (!ehcOpt.isPresent()) {
            throw new EntryHasCategoryDoesNotExistException(entry.getEntryId());
        }

        EntryHasCategory ehc = ehcOpt.get();

        Optional<Category> categoryOpt = categoryRepository.findById(ehc.getCategoryId());
        if (!categoryOpt.isPresent()) {
            throw new CategoryDoesNotExistException(ehc.getCategoryId());
        }

        Category category = categoryOpt.get();

        return category;
    }
}
