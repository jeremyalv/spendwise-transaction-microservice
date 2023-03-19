package com.spendwise.api.transactionmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntryRequest {
    // TODO: Later on, remove creatorId from EntryRequest and grab userId property from User directly
    private Long creatorId;
    private String type;
    private Instant createdAt;
    private Instant updatedAt;
    private Long amount;
    private String title;
    private String description;
}
