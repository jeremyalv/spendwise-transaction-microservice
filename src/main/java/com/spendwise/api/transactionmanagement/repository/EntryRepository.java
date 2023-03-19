package com.spendwise.api.transactionmanagement.repository;

import com.spendwise.api.transactionmanagement.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.Optional;


public interface EntryRepository extends JpaRepository<Entry, Long> {

}
