package com.spendwise.api.transactionmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spendwise.api.transactionmanagement.model.EntryType;
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
    @JsonProperty("creator_id")
    private Long creatorId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
}
