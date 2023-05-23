package com.spendwise.api.transactionmanagement.controller;

import com.spendwise.api.transactionmanagement.dto.EntryRequest;
import com.spendwise.api.transactionmanagement.exceptions.CategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EntryDoesNotExistException;
import com.spendwise.api.transactionmanagement.exceptions.EntryHasCategoryDoesNotExistException;
import com.spendwise.api.transactionmanagement.model.ehc.EntryHasCategory;
import com.spendwise.api.transactionmanagement.model.entry.Entry;
import com.spendwise.api.transactionmanagement.service.AuthService;
import com.spendwise.api.transactionmanagement.service.category.CategoryService;
import com.spendwise.api.transactionmanagement.service.ehc.EntryHasCategoryService;
import com.spendwise.api.transactionmanagement.service.entry.EntryService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
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
    private final AuthService authService;

    @GetMapping("/all")
    public ResponseEntity<List<Entry>> getAllEntries() {
        List<Entry> response = null;
        response = entryService.findAllEntries();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/all")
    // TODO: Add preAuthorize when auth is finished
    public ResponseEntity<List<Entry>> getAllEntriesFromUser(HttpServletRequest servletRequest, @PathVariable Long userId) {
        ResponseEntity<String> verifyResponse = authService.verify(servletRequest);

        if (verifyResponse.getStatusCode() != HttpStatusCode.valueOf(200)) {
            return ResponseEntity.status(403).body(null);
        }

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
        try {
            response = entryService.create(request);
            entryService.createEHC(ehcService, response, request);
            entryService.createAnalyticsExpense(response);

            return ResponseEntity.ok(response);
        }
        catch (CategoryDoesNotExistException catError) {
            throw new RuntimeException("Category does not exist.");
        }
    }

    @PutMapping("/update/{entryId}")
    public ResponseEntity<Entry> updateEntry(@PathVariable Long entryId, @RequestBody EntryRequest request) {
        Entry newEntry = null;
        try {
            Entry oldEntry = entryService.findById(entryId);

            newEntry = entryService.update(entryId, request);
            entryService.updateEHC(ehcService, newEntry, request);
            entryService.updateAnalyticsExpense(oldEntry, newEntry);

            return ResponseEntity.ok(newEntry);
        }
        catch (EntryDoesNotExistException entryError) {
            throw new RuntimeException("Entry does not exist.");
        }
        catch (CategoryDoesNotExistException catError) {
            throw new RuntimeException("Category does not exist.");
        }
    }

    @DeleteMapping("/delete/{entryId}")
    public ResponseEntity<String> deleteEntry(@PathVariable Long entryId) {
        try {
            entryService.deleteAnalyticsExpense(entryId);
            ehcService.delete(entryId);
            entryService.delete(entryId);
            String msg = "Deleted entry with id " + entryId;

            return ResponseEntity.ok(msg);
        }
        catch (EntryHasCategoryDoesNotExistException ehcError) {
            throw new RuntimeException("EHC does not exist.");
        }
        catch (EntryDoesNotExistException entryError) {
            throw new RuntimeException("Entry does not exist.");
        }
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



