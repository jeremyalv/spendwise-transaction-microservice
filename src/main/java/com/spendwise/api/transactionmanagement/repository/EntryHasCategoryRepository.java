package com.spendwise.api.transactionmanagement.repository;

import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryHasCategoryRepository extends JpaRepository<EntryHasCategory, Long> {
    List<EntryHasCategory> findAllByEntryId(Long entryId);
    List<EntryHasCategory> findAllByCategoryId(Long categoryId);
}
