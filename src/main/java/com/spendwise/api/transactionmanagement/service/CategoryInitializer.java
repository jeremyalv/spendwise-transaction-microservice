package com.spendwise.api.transactionmanagement.service;

import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.model.category.ExpenseCategory;
import com.spendwise.api.transactionmanagement.model.category.IncomeCategory;
import com.spendwise.api.transactionmanagement.model.entry.EntryTypeEnum;
import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CategoryInitializer {
    @Autowired
    private CategoryRepository categoryRepository;

    private int NUM_EXPENSE = 12;
    private int NUM_INCOME = 7;

    private void deleteAll() {
        categoryRepository.deleteAll();
    }

    private void initCategory() {
        Long id_count = 1L;
        ArrayList<String> expenseCat = new ArrayList<>(
                Arrays.asList(
                        "FNB",
                        "TRANSPORTATION",
                        "HOUSEHOLD_NEEDS",
                        "SHOPPING",
                        "HEALTH",
                        "TRAVEL",
                        "ENTERTAINMENT",
                        "EDUCATION",
                        "DONATIONS",
                        "INVESTMENTS",
                        "OTHERS",
                        "NONE"
                )
        );
        ArrayList<String> incomeCat = new ArrayList<>(
                Arrays.asList(
                        "SALARY",
                        "INTEREST",
                        "INVESTMENTS",
                        "ALLOWANCE",
                        "GIFTS",
                        "OTHER",
                        "NONE"
                )
        );

        for (int i = 1; i <= expenseCat.size(); i++) {
            String name = expenseCat.get(i - 1);
            Category c = new Category(
                    Long.valueOf(id_count),
                    EntryTypeEnum.EXPENSE,
                    name,
                    true,
                    ExpenseCategory.valueOf(name),
                    IncomeCategory.NONE
            );
            
            id_count++;

            categoryRepository.save(c);
        }

        for (int i = 1; i <= incomeCat.size(); i++) {
            String name = incomeCat.get(i - 1);
            Category c = new Category(
                    Long.valueOf(id_count),
                    EntryTypeEnum.INCOME,
                    name,
                    false,
                    ExpenseCategory.NONE,
                    IncomeCategory.valueOf(name)
            );
            
            id_count++;

            categoryRepository.save(c);
        }
    }

    @PostConstruct
    public void init() {
        if (categoryRepository.count() != NUM_INCOME + NUM_EXPENSE) {
            deleteAll();
            initCategory();
        }
    }
}
