package com.jackalcode.practice.day2;

import java.math.BigDecimal;
import java.util.Objects;

public class BankAccount {

    private final String accountNumber;
    private final String accountHolderName;
    private BigDecimal balance;

    public BankAccount(String accountNumber, String accountHolderName, BigDecimal balance) {
        validateAccountNumber(accountNumber);
        validateAccountHolderName(accountHolderName);
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

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);

        if (amount.intValue() > this.balance.intValue()) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        BankAccount that = (BankAccount) object;
        return Objects.equals(accountNumber, that.accountNumber) && Objects.equals(accountHolderName, that.accountHolderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accountHolderName);
    }

    private void validateAccountHolderName(String accountHolderName) {
        if (accountHolderName == null || accountHolderName.isBlank()) {
            throw new IllegalArgumentException("Account holder name is required");
        }
    }

    private void validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number is required");
        }
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
