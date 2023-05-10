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
public class EHCRequest {
    @JsonProperty("entry_id")
    private Long entryId;

    @JsonProperty("category_id")
    private Long categoryId;
}
