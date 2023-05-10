package com.spendwise.api.transactionmanagement.exceptions;

public class EntryHasCategoryDoesNotExistException extends RuntimeException {
    public EntryHasCategoryDoesNotExistException(Long entryId) {
        super("EntryHasCategory with entry id " + entryId + " does not exist");
    }
}
