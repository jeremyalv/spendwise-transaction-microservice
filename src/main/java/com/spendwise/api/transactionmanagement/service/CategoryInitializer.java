package com.spendwise.api.transactionmanagement.service;

import com.spendwise.api.transactionmanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {
    @Autowired
    private CategoryRepository categoryRepository;
}
