package com.jackalcode.practice.demo;

import com.jackalcode.practice.domain.BankAccount;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.IntStream;

public class Customer implements Runnable {
    private final BankAccount account;

    public static Customer customerOf(BankAccount account) {
        Objects.requireNonNull(account, "Bank account is required");
        return new Customer(account);
    }

    private Customer(BankAccount account) {
        this.account = account;
    }

    @Override
    public void run() {
        IntStream.rangeClosed(1, 10)
                .forEach(amount -> account.deposit(BigDecimal.valueOf(amount * 10L)));
    }
}
