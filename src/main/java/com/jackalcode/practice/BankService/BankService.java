package com.jackalcode.practice.BankService;

import com.jackalcode.practice.domain.BankAccount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BankService {

    private final Map<String, BankAccount> accounts;

    public BankService() {
        this.accounts = new HashMap<>();
    }

    public BankAccount createAccount(String accountNumber, String accountHolderName, BigDecimal startingBalance) {

        Objects.requireNonNull(accountNumber, "Account number is required");
        Objects.requireNonNull(accountHolderName, "Account holder name is required");
        Objects.requireNonNull(startingBalance, "Starting balance is required");

        if (accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account already exists with account number: " + accountNumber);
        }

        BankAccount newAccount = null;
        try {
            newAccount = new BankAccount(accountNumber, accountHolderName, startingBalance);
            accounts.put(accountNumber, newAccount);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return newAccount;
    }

    public BankAccount getAccount(String accountNumber) {

        return getAccountByAccountNumber(accountNumber);
    }

    public void deposit(String accountNumber, BigDecimal amount) {
        BankAccount existingAccount;

        validateAmount(amount);
        existingAccount = getAccountByAccountNumber(accountNumber);

        try {
            existingAccount.deposit(amount);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void withdraw(String accountNumber, BigDecimal amount) {
        BankAccount existingAccount;

        validateAmount(amount);
        existingAccount = getAccountByAccountNumber(accountNumber);

        try {
            existingAccount.withdraw(amount);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private BankAccount getAccountByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account is required");
        }

        if (!accounts.containsKey(accountNumber)) {
            throw new IllegalArgumentException("Account not found with account number: " + accountNumber);
        }

        return accounts.get(accountNumber);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount is required and should be greater than zero");
        }
    }
}
