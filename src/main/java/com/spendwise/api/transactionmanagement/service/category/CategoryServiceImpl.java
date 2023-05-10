package com.spendwise.api.transactionmanagement.service.category;

import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import org.springframework.stereotype.Service;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        else {
            throw new CategoryDoesNotExistException(categoryId);
        }
    }

    @Override
    public Category findByName(String name) {
        Optional<Category> optionalCategory = categoryRepository.findByName(name);

        if (optionalCategory.isPresent()) {
            return optionalCategory.get();
        }
        else {
            throw new CategoryDoesNotExistException(name);
        }
    }
}
