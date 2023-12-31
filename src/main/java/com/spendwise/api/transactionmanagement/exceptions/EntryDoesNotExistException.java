package com.spendwise.api.transactionmanagement.exceptions;

public class EntryDoesNotExistException extends RuntimeException {
    public EntryDoesNotExistException(Long id) {
        super("Entry with id " + id + " does not exist");
    }
}
