package com.spendwise.api.transactionmanagement.model;

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
public class Entry {
    @Id
    @GeneratedValue
    private Long id;
    // TODO: To be converted to foreign key to User.ID
    private Long creatorId;
    @Enumerated(EnumType.STRING)
    private EntryType type;
    private Instant createdAt;
    private Instant updatedAt;
    private Long amount;
    private String title;
    private String description;
}
