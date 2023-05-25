package com.spendwise.api.transactionmanagement.repository;

import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryHasCategoryRepository extends JpaRepository<EntryHasCategory, Long> {
    // Each Entry can only have 1 Category
    Optional<EntryHasCategory> findByEntryId(Long entryId);
    List<EntryHasCategory> findAllByCategoryId(Long categoryId);
}
