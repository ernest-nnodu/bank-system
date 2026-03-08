package com.jackalcode.practice.repository;

import com.jackalcode.practice.domain.BankAccount;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryAccountRepository implements AccountRepository {

    private final Map<String, BankAccount> accounts;

    public InMemoryAccountRepository() {
        this.accounts = new HashMap<>();
    }

    @Override
    public void save(BankAccount account) {
        Objects.requireNonNull(account, "Account is required");

        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        Objects.requireNonNull(accountNumber, "Account number is required");

        return accounts.get(accountNumber);
    }

    @Override
    public boolean exists(String accountNumber) {
        Objects.requireNonNull(accountNumber, "Account number is required");
        return accounts.containsKey(accountNumber);
    }
}
