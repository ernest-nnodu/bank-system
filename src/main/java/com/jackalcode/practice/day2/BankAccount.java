package com.jackalcode.practice.day2;

import java.math.BigDecimal;
import java.util.Objects;

public class BankAccount {

    private final String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;

    public BankAccount(String accountNumber, String accountHolderName, BigDecimal balance) {
        validateAccountNumber(accountNumber);
        validateAccountHolderName(accountHolderName);
        validateBalance(balance);

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

    public BigDecimal deposit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);

        return balance;
    }

    public BigDecimal withdraw(BigDecimal amount) {
        validateAmount(amount);

        if (amount.compareTo(this.balance) > 0) {
            throw new IllegalStateException("Insufficient funds");
        }

        this.balance = this.balance.subtract(amount);

        return balance;
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
        return Objects.equals(accountNumber, that.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountHolderName='" + accountHolderName + '\'' +
                ", balance=" + balance +
                '}';
    }

    private void validateBalance(BigDecimal balance) {
        if(balance == null) {
            throw new IllegalArgumentException("Balance is required");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance should be zero or greater");
        }
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

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount should be greater than zero");
        }
    }
}
