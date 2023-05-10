package com.spendwise.api.transactionmanagement.service.category;

import com.spendwise.api.transactionmanagement.model.category.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> findAllCategories();
    Category findById(Long categoryId);
    Category findByName(String categoryName);
}
