package com.spendwise.api.transactionmanagement.repository;

import com.spendwise.api.transactionmanagement.model.entry.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {
    List<Entry> findByCreatorId(Long creatorId);
}
