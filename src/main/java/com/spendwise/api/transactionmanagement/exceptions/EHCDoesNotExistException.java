package com.spendwise.api.transactionmanagement.exceptions;

public class EHCDoesNotExistException extends RuntimeException {
    public EHCDoesNotExistException(Long entryId) {
        super("EntryHasCategory with entry id " + entryId + " does not exist");
    }
}
