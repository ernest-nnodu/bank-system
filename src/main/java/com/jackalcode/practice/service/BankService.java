package com.jackalcode.practice.service;

import com.jackalcode.practice.domain.BankAccount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BankService {

    private final Map<String, BankAccount> accounts;
    private final Object transactionLock = new Object();
    public static long successfulDeposits = 0;
    public static long successfulWithdrawals = 0;

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

        BankAccount newAccount = new BankAccount(accountNumber, accountHolderName, startingBalance);
        accounts.put(accountNumber, newAccount);

        return newAccount;
    }

    public BankAccount getAccount(String accountNumber) {

        return getAccountByAccountNumber(accountNumber);
    }

    public synchronized BigDecimal deposit(String accountNumber, BigDecimal amount) {

        BankAccount existingAccount = getAccountByAccountNumber(accountNumber);
        validateAmount(amount);

        existingAccount.deposit(amount);
        successfulDeposits++;

        return amount;
    }

    public synchronized BigDecimal withdraw(String accountNumber, BigDecimal amount) {

        BankAccount existingAccount = getAccountByAccountNumber(accountNumber);
        validateAmount(amount);

        existingAccount.withdraw(amount);
        successfulWithdrawals++;

        return amount;
    }

    public void transfer(String sourceAccountNumber, String targetAccountNumber, BigDecimal amount) {

        BankAccount sourceAccount = getAccountByAccountNumber(sourceAccountNumber);
        BankAccount targetAccount = getAccountByAccountNumber(targetAccountNumber);
        validateAmount(amount);

        sourceAccount.transferTo(targetAccount, amount);
    }

    private BankAccount getAccountByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account is required");
        }

        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found with account number: " + accountNumber);
        }

        return account;
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount is required and should be greater than zero");
        }
    }
}
