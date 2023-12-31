package com.spendwise.api.transactionmanagement.service.entry;

import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    Map<String, Object> createAnalyticsExpense(Entry entryPayload);
    void deleteAnalyticsExpense(Long id);
    Map<String, Object> updateAnalyticsExpense(Entry oldEntry, Entry newEntry);
    Map<String, Object> createNotification(Entry entry, String type);
    Map<String, Object> createNotificationDirectly(Long uid, String message);
}
