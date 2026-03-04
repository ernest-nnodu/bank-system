package com.jackalcode.practice.day2;

import java.math.BigDecimal;

public class BankAccount {

    private final String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;

    public BankAccount(String accountNumber, String accountHolderName, BigDecimal balance) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }

        if (accountHolderName == null || accountHolderName.isBlank()) {
            throw new IllegalArgumentException("Account holder name is required");
        }

        validateBalance(balance);

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    private void validateBalance(BigDecimal balance) {
        if (balance.intValue() < 0) {
            throw new IllegalArgumentException("Balance should be zero or greater");
        }
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal bigDecimal) {
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }

        if (amount.intValue() <= 0) {
            throw new IllegalArgumentException("amount should be greater than zero");
        }
    }
}
