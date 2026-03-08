package com.jackalcode.practice.repository;

import com.jackalcode.practice.domain.BankAccount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

        if (exists(account.getAccountNumber())) {
            throw new IllegalArgumentException("Account already exists with account number: " + account.getAccountNumber());
        }
        accounts.put(account.getAccountNumber(), account);
    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        Objects.requireNonNull(accountNumber, "Account number is required");
        BankAccount account = accounts.get(accountNumber);

        if (account == null) {
            throw new IllegalArgumentException("Account not found with account number: " + accountNumber);
        }

        return account;
    }

    @Override
    public boolean exists(String accountNumber) {
        Objects.requireNonNull(accountNumber, "Account number is required");
        return accounts.containsKey(accountNumber);
    }

    @Override
    public List<BankAccount> getAllAccounts() {

        return accounts.values()
                .stream()
                .toList();
    }

    @Override
    public List<BankAccount> getAccountsWithBalanceGreaterThan(BigDecimal amount) {
        return accounts.values()
                .stream()
                .filter(acc -> acc.getBalance().compareTo(amount) > 0)
                .toList();
    }

    @Override
    public BigDecimal getTotalMoneyInBank() {
        if (accounts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return accounts.values()
                .stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal::add)
                .get();
    }
}
