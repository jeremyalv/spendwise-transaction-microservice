package com.spendwise.api.transactionmanagement.controller;

import com.spendwise.api.transactionmanagement.dto.CategoryRequest;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.service.category.CategoryService;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryService;
import com.spendwise.api.transactionmanagement.service.entry.EntryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// TODO import spring security for next sprint
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> response = null;
        response = categoryService.findAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        Category response = null;
        response = categoryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Category> getByName(@PathVariable String name) {
        Category response = null;
        response = categoryService.findByName(name);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest request) {
        Category response = null;
        response = categoryService.create(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Deleted category with id " + id);
    }
}
