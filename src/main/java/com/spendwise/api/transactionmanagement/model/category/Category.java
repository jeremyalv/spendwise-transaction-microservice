package com.spendwise.api.transactionmanagement.model.category;

import com.spendwise.api.transactionmanagement.model.entry.EntryTypeEnum;
import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "entry_type")
    @Enumerated(EnumType.STRING)
    private EntryTypeEnum entryType;
    @Column(name = "name")
    private String name;

    @Column(name = "is_expense")
    private Boolean isExpense;

    @Column(name = "expense_category")
    @Enumerated(EnumType.STRING)
    private ExpenseCategory expenseCategory;

    @Column(name = "income_category")
    @Enumerated(EnumType.STRING)
    private IncomeCategory incomeCategory;
}
