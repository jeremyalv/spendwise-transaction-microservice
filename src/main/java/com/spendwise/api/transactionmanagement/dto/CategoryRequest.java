package com.spendwise.api.transactionmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @JsonProperty("entry_type")
    private String entryType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("is_expense")
    private Boolean isExpense;
    @JsonProperty("expense_category")
    private String expenseCategory;
    @JsonProperty("income_category")
    private String incomeCategory;
}
