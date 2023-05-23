package com.spendwise.api.transactionmanagement.model.analytics;

import com.spendwise.api.transactionmanagement.model.category.ExpenseCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseRequest {
    private Long userId;
    private Instant instant;
    private String category;
    private Double expenseAmount;
}
