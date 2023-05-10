package com.spendwise.api.transactionmanagement.service.category;

import com.spendwise.api.transactionmanagement.dto.CategoryRequest;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.category.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> findAllCategories();
    List<Category> findAllByCreatorId(Long creatorId);
    Category findById(Long categoryId);
    Category findByName(String categoryName);
    Category create(CategoryRequest request);
    Category update(Long categoryId, CategoryRequest request);
    void delete(Long categoryId);
}
