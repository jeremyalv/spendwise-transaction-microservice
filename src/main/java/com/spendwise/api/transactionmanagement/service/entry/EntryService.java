package com.spendwise.api.transactionmanagement.service.entry;

import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface EntryService {
    List<Entry> findAllEntries();
    List<Entry> findAllByCreatorId(Long creatorId);
    Entry findById(Long id);
    Entry create(EntryRequest request);
    Entry update(Long id, EntryRequest request);
    void delete(Long id);
    EntryHasCategory createEHC(EntryHasCategoryService ehcService, Entry entry, EntryRequest request);
    EntryHasCategory updateEHC(EntryHasCategoryService ehcService, Entry entry, EntryRequest request);
}
