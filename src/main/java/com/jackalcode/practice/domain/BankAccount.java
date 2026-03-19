package com.jackalcode.practice.domain;

import com.jackalcode.practice.exception.InsufficientFundsException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BankAccount {

    @Getter
    private final String accountNumber;
    @Getter
    private final String accountHolderName;
    @Getter
    private BigDecimal balance;
    private final List<Transaction> transactions;

    public BankAccount(String accountNumber, String accountHolderName, BigDecimal balance) {
        this.transactions = new ArrayList<>();

        //Validate that account is not null
        validateAccountNumber(accountNumber);

        //Validate that account holder name is not null
        validateAccountHolderName(accountHolderName);

        //Validate that balance is not null and it is greater than zero
        validateBalance(balance);

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(this.transactions);
    }

    public void deposit(BigDecimal amount) {

        //Validate that amount is not null and greater than zero
        validateAmount(amount);

        //Update account balance
        this.balance = this.balance.add(amount);

        //Store deposit transaction
        transactions.add(
                new Transaction(accountNumber, TransactionType.DEPOSIT, amount)
        );
    }

    public void withdraw(BigDecimal amount) {

        //Validate that amount is not null and greater than zero
        validateAmount(amount);

        //Check that account have sufficient balance to satisfy withdraw amount
        hasSufficientFunds(amount);

        //Update balance after withdraw
        this.balance = this.balance.subtract(amount);

        //store withdraw transaction
        this.transactions.add(
                new Transaction(accountNumber, TransactionType.WITHDRAW, amount)
        );

    }

    public void transferTo(BankAccount target, BigDecimal amount) {

        //Check that target account is not null
        if (target == null) {
            throw new IllegalArgumentException("Target account is required");
        }

        //Check that target account is not the same as the source account
        if (this.equals(target)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        //Validate amount is not null and greater than zero
        validateAmount(amount);

        //Check source account have sufficient funds for transfer
        hasSufficientFunds(amount);

        //Update account balance as per transfer amount
        this.balance = this.balance.subtract(amount);

        //Store transfer transaction
        Transaction transferTransaction = new Transaction(accountNumber, TransactionType.TRANSFER_OUT, amount);
        this.transactions.add(transferTransaction);

        try {
            target.transferIn(amount);
        } catch (Exception e) {
            this.balance = this.balance.add(amount); // rollback transfer if unsuccessful
            this.transactions.remove(transferTransaction); //Remove transfer transaction
            throw e;
        }
    }

    private void transferIn(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
        this.transactions.add(
                new Transaction(accountNumber, TransactionType.TRANSFER_IN, amount)
        );
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

    private void hasSufficientFunds(BigDecimal amount) {
        if (amount.compareTo(this.balance) > 0) {
            throw new InsufficientFundsException("Account " + this.accountNumber + " have Insufficient funds: " + balance);
        }
    }
}
