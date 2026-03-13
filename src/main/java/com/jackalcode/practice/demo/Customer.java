package com.jackalcode.practice.demo;

import com.jackalcode.practice.BankService.BankService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.stream.IntStream;

public class Customer implements Runnable {
    private final String accountNumber;
    private final BankService bankService;

    public static Customer customerOf(String accountNumber, BankService bankService) {
        Objects.requireNonNull(accountNumber, "Bank account number is required");
        Objects.requireNonNull(bankService, "Bank service is required");
        return new Customer(accountNumber, bankService);
    }

    private Customer(String accountNumber, BankService bankService) {
        this.accountNumber = accountNumber;
        this.bankService = bankService;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public void run() {
        IntStream.rangeClosed(1, 10)
                .forEach(amount -> bankService.deposit(accountNumber, BigDecimal.valueOf(amount * 10L)));
        IntStream.rangeClosed(1, 10)
                .forEach(amount -> bankService.withdraw(accountNumber, BigDecimal.valueOf(amount * 10L)));
    }
}
