package com.spendwise.api.transactionmanagement.model.entry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "entry")
public class Entry {
    @Id
    @GeneratedValue
    @Column(name = "entry_id")
    private Long entryId;
    @Column(name = "creator_id")
    private Long creatorId;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

    @Column(name = "entry_type")
    @Enumerated(EnumType.STRING)
    private EntryTypeEnum entryType;

    @Column(name = "category_name")
    private String categoryName;
}
