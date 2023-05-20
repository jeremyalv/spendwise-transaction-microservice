package com.spendwise.api.transactionmanagement.controller;

import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.service.category.CategoryService;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryService;
import com.spendwise.api.transactionmanagement.service.entry.EntryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// TODO import spring security for next sprint
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class EntryController {
    private final EntryService entryService;
    private final CategoryService categoryService;
    private final EntryHasCategoryService ehcService;

    @GetMapping("/all")
    public ResponseEntity<List<Entry>> getAllEntries() {
        List<Entry> response = null;
        response = entryService.findAllEntries();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/all")
    // TODO: Add preAuthorize when auth is finished
    public ResponseEntity<List<Entry>> getAllEntriesFromUser(@PathVariable Long userId) {
        List<Entry> response = null;
        response = entryService.findAllByCreatorId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{entryId}")
    public ResponseEntity<Entry> getEntry(@PathVariable Long entryId) {
        Entry response = null;
        response = entryService.findById(entryId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<Entry> createEntry(@RequestBody EntryRequest request) {
        Entry response = null;
        response = entryService.create(request);
        entryService.createEHC(ehcService, response, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{entryId}")
    public ResponseEntity<Entry> updateEntry(@PathVariable Long entryId, @RequestBody EntryRequest request) {
        Entry response = null;
        response = entryService.update(entryId, request);
        entryService.updateEHC(ehcService, response, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{entryId}")
    public ResponseEntity<String> deleteEntry(@PathVariable Long entryId) {
        ehcService.delete(entryId);
        entryService.delete(entryId);
        String msg = "Deleted entry with id " + entryId;
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/ehc/all")
    public ResponseEntity<List<EntryHasCategory>> getAllEHC() {
        List<EntryHasCategory> response = ehcService.findAllEHC();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ehc/{entryId}")
    public ResponseEntity<EntryHasCategory> getEHCByID(@PathVariable Long entryId) {
        EntryHasCategory response = ehcService.findByEntryId(entryId);
        return ResponseEntity.ok(response);
    }
}



