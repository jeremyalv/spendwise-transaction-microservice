package com.spendwise.api.transactionmanagement.exceptions;

public class CategoryDoesNotExistException extends RuntimeException {
    public CategoryDoesNotExistException(String name) {
        super("Category with name " + name + " does not exist");
    }
}
