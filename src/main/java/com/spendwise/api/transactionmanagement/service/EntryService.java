package com.spendwise.api.transactionmanagement.service;

import com.spendwise.api.transactionmanagement.model.Entry;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;
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
}
