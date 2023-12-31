package com.spendwise.api.transactionmanagement.service.ehc;

import com.spendwise.api.transactionmanagement.dto.EHCRequest;
import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EntryHasCategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import com.spendwise.api.transactionmanagement.repository.EntryHasCategoryRepository;
import com.spendwise.api.transactionmanagement.repository.EntryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryHasCategoryServiceImpl implements EntryHasCategoryService {
    private final EntryHasCategoryRepository ehcRepository;
    private final EntryRepository entryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EntryHasCategory> findAllEHC() {
        return ehcRepository.findAll();
    }

    @Override
    public List<EntryHasCategory> findAllByCreatorId(Long creatorId) {
        List<Entry> allUserEntries = entryRepository.findByCreatorId(creatorId);
        List<Long> userEntryIds = allUserEntries
                .stream()
                .map(entry -> entry.getEntryId())
                .toList();

        List<EntryHasCategory> allUserEHC = findAllEHC()
                .stream()
                .filter(ehc ->
                        userEntryIds.contains(ehc.getEntryId())
                )
                .toList();

        return allUserEHC;
    }

    @Override
    public List<Category> findAllCategoryByCreatorId(Long creatorId) {
        List<EntryHasCategory> allUserEHC = findAllByCreatorId(creatorId);
        List<Category> allUserCategories = allUserEHC
                .stream()
                .map(ehc -> {
                    Long categoryId = ehc.getCategoryId();
                    Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

                    if (optionalCategory.isPresent()) {
                        return optionalCategory.get();
                    }
                    else {
                        throw new CategoryDoesNotExistException(categoryId);
                    }
                })
                .toList();

        return allUserCategories; //NOSONAR
    }
    @Override
    public EntryHasCategory findByEntryId(Long entryId) {
        Optional<EntryHasCategory> optionalEHC = ehcRepository.findById(entryId);

        if (optionalEHC.isPresent()) {
            return optionalEHC.get();
        }
        else {
            throw new EntryHasCategoryDoesNotExistException(entryId);
        }
    }

    @Override
    public EntryHasCategory create(EHCRequest request) {
        EntryHasCategory ehc = EntryHasCategory
                .builder()
                .entryId(request.getEntryId())
                .categoryId(request.getCategoryId())
                .build();

        return ehcRepository.save(ehc);
    }

    @Override
    public EntryHasCategory update(Long entryId, EHCRequest request) {
        if (isEHCDoesNotExist(entryId)) {
            throw new EntryHasCategoryDoesNotExistException(entryId);
        }

        EntryHasCategory ehc = findByEntryId(entryId);

        ehc.setEntryId(request.getEntryId());
        ehc.setCategoryId(request.getCategoryId());

        return ehcRepository.save(ehc);
    }

    @Override
    @Transactional
    public void delete(Long entryId) {
        if (isEHCDoesNotExist(entryId)) {
            throw new EntryHasCategoryDoesNotExistException(entryId);
        }

        ehcRepository.deleteById(entryId);
    }

    private boolean isEHCDoesNotExist(Long entryId) {
        return ehcRepository.findById(entryId).isEmpty();
    }
}
