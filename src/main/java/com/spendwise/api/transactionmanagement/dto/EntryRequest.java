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
public class EntryRequest {
    // TODO: Later on, remove creatorId from EntryRequest and grab userId property from User directly
    @JsonProperty("creator_id")
    private Long creatorId;
    @JsonProperty("amount")
    private Long amount;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("entry_type")
    private String entryType;
    @JsonProperty("category_name")
    private String categoryName;
}
