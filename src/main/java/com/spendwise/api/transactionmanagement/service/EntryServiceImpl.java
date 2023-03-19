package com.spendwise.api.transactionmanagement.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.spendwise.api.transactionmanagement.model.Entry;
import com.spendwise.api.transactionmanagement.model.EntryType;
import com.spendwise.api.transactionmanagement.repository.EntryRepository;
import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;
import com.spendwise.api.transactionmanagement.dto.EntryRequest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {
    private final EntryRepository entryRepository;

    @Override
    public List<Entry> findAllEntries() {
        return entryRepository.findAll();
    }

    @Override
    public List<Entry> findAllByCreatorId(Long creatorId) {
        return entryRepository.findAll()
                .stream().filter(entry -> entry.getCreatorId().equals(creatorId))
                .collect(Collectors.toList());
    }
    @Override
    public Entry findById(Long id) {
        Optional<Entry> optionalEntry = entryRepository.findById(id);

        if (optionalEntry.isPresent()) {
            return optionalEntry.get();
        }
        else {
            throw new EntryDoesNotExistException(id);
        }
    }

    @Override
    public Entry create(EntryRequest request) {
        Entry entry = Entry.builder()
                .creatorId(request.getCreatorId()) // TODO: To get from user object directly
                .type(EntryType.valueOf(request.getType()))
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .amount(request.getAmount())
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
        System.out.println(entry.toString());
        return entryRepository.save(entry);
    }

    @Override
    public Entry update(Long id, EntryRequest request) {
        Entry entry = findById(id);

        entry.setCreatorId(request.getCreatorId()); // TODO: To get from user object directly
        entry.setType(EntryType.valueOf(request.getType()));
        // TODO: Debug case CreatedAt increments by 1 day each time PUT request is sent
        // Updated At set to Now
        entry.setUpdatedAt(Instant.now());
        entry.setAmount(request.getAmount());
        entry.setTitle(request.getTitle());
        entry.setDescription(request.getDescription());

        return entryRepository.save(entry);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (isEntryDoesNotExist(id)) {
            throw new EntryDoesNotExistException(id);
        }

        entryRepository.deleteById(id);
    }

    private boolean isEntryDoesNotExist(Long id) {
        return entryRepository.findById(id).isEmpty();
    }
}
