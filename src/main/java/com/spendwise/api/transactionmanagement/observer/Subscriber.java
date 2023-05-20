package com.spendwise.api.transactionmanagement.observer;

// TODO integrate with Auth first so you can use
// user objects as subscribers...
public interface Subscriber {
    void handleNotification();
}
