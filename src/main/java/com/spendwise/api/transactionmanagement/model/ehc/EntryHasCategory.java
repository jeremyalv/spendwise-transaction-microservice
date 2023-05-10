package com.spendwise.api.transactionmanagement.model.ehc;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "entry_has_category")
public class EntryHasCategory {
    @Id
    @Column(name = "entry_id")
    private Long entryId;

    @Column(name = "category_id")
    private Long categoryId;
}
