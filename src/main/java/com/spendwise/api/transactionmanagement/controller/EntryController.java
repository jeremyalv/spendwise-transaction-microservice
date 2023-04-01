package com.spendwise.api.transactionmanagement.controller;

import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.Entry;
import com.spendwise.api.transactionmanagement.service.EntryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// TODO import spring security for next sprint
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class EntryController {
    private final EntryService entryService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllEntries() {
        try {
            List<Entry> response = null;
            response = entryService.findAllEntries();
            return ResponseEntity.ok(response);
        }
        catch (EntryDoesNotExistException e) {
            String msg = "An error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

    }

    @GetMapping("/{userId}/all")
    // TODO: Add preAuthorize when auth is finished
    public ResponseEntity<?> getAllEntriesFromUser(@PathVariable Long userId) {
        try {
            List<Entry> response = null;
            response = entryService.findAllByCreatorId(userId);
            return ResponseEntity.ok(response);
        }
        catch (EntryDoesNotExistException e) {
            String msg = "An error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<?> getEntry(@PathVariable Long entryId) {
        try {
            Entry response = null;
            response = entryService.findById(entryId);
            return ResponseEntity.ok(response);
        }
        catch (EntryDoesNotExistException e) {
            String msg = "An error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<Entry> addEntry(@RequestBody EntryRequest request) {
        Entry response = null;
        response = entryService.create(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{entryId}")
    public ResponseEntity<?> updateEntry(@PathVariable Long entryId, @RequestBody EntryRequest request) {
        try {
            Entry response = null;
            response = entryService.update(entryId, request);
            return ResponseEntity.ok(response);
        }
        catch (EntryDoesNotExistException e) {
            String msg = "An error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
    }

    @DeleteMapping("/delete/{entryId}")
    public ResponseEntity<String> deleteEntry(@PathVariable Long entryId) {
        try {
            entryService.delete(entryId);
            String msg = "Deleted entry with id " + entryId;
            return ResponseEntity.ok(msg);
        }
        catch (EntryDoesNotExistException e) {
            String msg = "An error occurred: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }


    }

}



