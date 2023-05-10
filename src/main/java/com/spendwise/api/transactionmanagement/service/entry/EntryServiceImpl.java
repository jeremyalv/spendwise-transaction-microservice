package com.spendwise.api.transactionmanagement.service.entry;

import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EHCDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import com.spendwise.api.transactionmanagement.repository.EntryHasCategoryRepository;
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
    private final EntryRepository entryRepository;
    private final CategoryRepository categoryRepository;
    private final EntryHasCategoryRepository entryHasCategoryRepository;

    @Override
    public List<Entry> findAllEntries() {
        return entryRepository.findAll();
    }

    @Override
    public List<Entry> findAllByCreatorId(Long creatorId) {
        return entryRepository.findAll()
                .stream().filter(entry -> entry.getCreatorId().equals(creatorId))
                .toList();
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
    public Category findCategoryByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);

        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        else {
            throw new CategoryDoesNotExistException(name);
        }
    }

//    TODO not refactored yet
    @Override
    public EntryHasCategory findEHCByEntryId(Long entryId) {
        Optional<EntryHasCategory> optionalEHC = entryHasCategoryRepository.findById(entryId);

        if (optionalEHC.isPresent()) {
            return optionalEHC.get();
        }
        else {
            throw new EHCDoesNotExistException(entryId);
        }
    }

    @Override
        public Entry create(EntryRequest request) {
        var entry = Entry.builder()
                .creatorId(request.getCreatorId()) // TODO: To get from user object directly
                .type(EntryTypeEnum.valueOf(request.getEntryType()))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(request.getAmount())
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        Category category = findCategoryByName(request.getCategoryName());

        var entryHasCategory = EntryHasCategory.builder()
                .entryId(entry.getEntryId())
                .categoryId(category.getCategoryId())
                .build();

        entryHasCategoryRepository.save(entryHasCategory);

        return entryRepository.save(entry);
    }

    @Override
    public Entry update(Long id, EntryRequest request) {
        if (isEntryDoesNotExist(id)) {
            throw new EntryDoesNotExistException(id);
        }

        var entry = findById(id);

        entry.setCreatorId(request.getCreatorId()); // TODO: To get from user object directly
        entry.setType(EntryTypeEnum.valueOf(request.getEntryType()));
        // TODO: Debug case CreatedAt increments by 1 day each time PUT request is sent
        // Updated At set to Now
        entry.setUpdatedAt(Instant.now());
        entry.setAmount(request.getAmount());
        entry.setTitle(request.getTitle());
        entry.setDescription(request.getDescription());

        Category category = findCategoryByName(request.getCategoryName());

        var entryHasCategory = findEHCByEntryId(id);

        entryHasCategory.setCategoryId(category.getCategoryId());

        entryHasCategoryRepository.save(entryHasCategory);
        return entryRepository.save(entry);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (isEntryDoesNotExist(id)) {
            throw new EntryDoesNotExistException(id);
        }

        deleteEHC(id);
        entryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteEHC(Long entryId) {
        if (isEHCDoesNotExist(entryId)) {
            throw new EHCDoesNotExistException(entryId);
        }

        entryHasCategoryRepository.deleteById(entryId);
    }

    private boolean isEntryDoesNotExist(Long id) {
        return entryRepository.findById(id).isEmpty();
    }

    private boolean isEHCDoesNotExist(Long entryId) {
        return entryHasCategoryRepository.findById(entryId).isEmpty();
    }

    // TODO add isUserDoesNotExist which checks the UserRepository from Auth service
}
