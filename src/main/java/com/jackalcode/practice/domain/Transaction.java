package com.jackalcode.practice.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Transaction {

    private final String id;
    private final String accountNumber;
    private final TransactionType type;
    private final BigDecimal amount;
    private final Instant timestamp;

    public Transaction(String accountNumber, TransactionType type, BigDecimal amount) {

        //Validate that accountNumber, transaction type, and amount are provided, amount should be grater than zero
        validateTransactionDetails(accountNumber, type, amount);

        this.id = UUID.randomUUID().toString();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.timestamp = Instant.now();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Transaction that = (Transaction) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    private void validateTransactionDetails(String accountNumber, TransactionType type, BigDecimal amount) {
        if(accountNumber == null || accountNumber.isBlank())
            throw new IllegalArgumentException("Account number required");

        if(type == null)
            throw new IllegalArgumentException("Transaction type required");

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be greater than zero");
    }
}
