package com.spendwise.api.transactionmanagement.repository;

import com.spendwise.api.transactionmanagement.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, Long> {

}
