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
@Table(name = "entry")
public class Entry {
    @Id
    @GeneratedValue
    @Column(name = "entry_id")
    private Long entryId;
    // TODO: To be converted to foreign key to User.ID
    @Column(name = "creator_id")
    private Long creatorId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EntryType type;
    @Column(name = "created_at")
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
}
