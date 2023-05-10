package com.spendwise.api.transactionmanagement.service.ehc;

import com.spendwise.api.transactionmanagement.dto.EHCRequest;
import com.spendwise.api.transactionmanagement.model.category.Category;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EHCService {
    List<EntryHasCategory> findAllEHC();
    List<EntryHasCategory> findAllByCreatorId(Long creatorId);
    List<Category> findAllCategoryByCreatorId(Long creatorId);
    EntryHasCategory findByEntryId(Long entryId);
    EntryHasCategory create(EHCRequest request);
    EntryHasCategory update(Long entryId, EHCRequest request);
    void delete(Long entryId);
}
