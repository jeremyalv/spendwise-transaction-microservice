package com.spendwise.api.transactionmanagement.service.entry;

import com.spendwise.api.transactionmanagement.dto.EHCRequest;
import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EntryHasCategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import com.spendwise.api.transactionmanagement.repository.EntryHasCategoryRepository;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryService;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryServiceImpl;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.model.entry.EntryTypeEnum;
import com.spendwise.api.transactionmanagement.repository.EntryRepository;
import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final EntryHasCategoryService ehcService;
    private final EntryRepository entryRepository;
    private final CategoryRepository categoryRepository;
    private final EntryHasCategoryRepository ehcRepository;

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
                .type(EntryTypeEnum.valueOf(request.getEntryType()))
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
        entry.setType(EntryTypeEnum.valueOf(request.getEntryType()));
        // TODO: Debug case CreatedAt increments by 1 day each time PUT request is sent
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

    public EntryHasCategory createEHC(EntryHasCategoryService ehcService, Entry entry, EntryRequest request) {
        Category category = findCategoryByName(request.getCategoryName());

        EHCRequest ehcRequest = createEHCRequest(entry, category);

        return ehcService.create(ehcRequest);
    }

    public EHCRequest createEHCRequest(Entry entry, Category category) {
        EHCRequest ehcRequest = new EHCRequest();

        ehcRequest.setEntryId(entry.getEntryId());
        ehcRequest.setCategoryId(category.getCategoryId());

        return ehcRequest;
    }

    // TODO add isUserDoesNotExist which checks the UserRepository from Auth service
}
