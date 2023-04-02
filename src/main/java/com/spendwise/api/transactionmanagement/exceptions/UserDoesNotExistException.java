package com.spendwise.api.transactionmanagement.exceptions;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(Long id) {
        super("User with id " + id + " does not exist");
    }
}
