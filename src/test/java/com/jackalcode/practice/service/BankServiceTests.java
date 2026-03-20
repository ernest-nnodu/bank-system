package com.jackalcode.practice.service;


import com.jackalcode.practice.domain.BankAccount;
import com.jackalcode.practice.domain.Transaction;
import com.jackalcode.practice.domain.TransactionType;
import com.jackalcode.practice.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BankServiceTests {

    @Test
    void createAccount_shouldStoreAccount() {

        BankService bank = new BankService();

        BankAccount account = bank.createAccount(
                "A1",
                "Alice",
                new BigDecimal("100")
        );

        assertEquals("A1", account.getAccountNumber());
        assertEquals(new BigDecimal("100"), account.getBalance());
    }

    @Test
    void createAccount_shouldFailIfAccountExists() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.createAccount("A1", "Bob", new BigDecimal("50"))
        );
    }

    @Test
    void getAccount_shouldReturnAccount() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));

        BankAccount account = bank.getAccount("A1");

        assertEquals("Alice", account.getAccountHolderName());
    }

    @Test
    void deposit_shouldIncreaseBalance() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));

        bank.deposit("A1", new BigDecimal("50"));

        assertEquals(
                new BigDecimal("150"),
                bank.getAccount("A1").getBalance()
        );
    }

    @Test
    void withdraw_shouldDecreaseBalance() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));

        bank.withdraw("A1", new BigDecimal("30"));

        assertEquals(
                new BigDecimal("70"),
                bank.getAccount("A1").getBalance()
        );
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("200"));
        bank.createAccount("A2", "Bob", new BigDecimal("100"));

        bank.transfer("A1", "A2", new BigDecimal("50"));

        assertEquals(new BigDecimal("150"), bank.getAccount("A1").getBalance());
        assertEquals(new BigDecimal("150"), bank.getAccount("A2").getBalance());
    }

    @Test
    void transfer_shouldFailWhenInsufficientFunds() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));
        bank.createAccount("A2", "Bob", new BigDecimal("100"));

        assertThrows(
                InsufficientFundsException.class,
                () -> bank.transfer("A1", "A2", new BigDecimal("200"))
        );
    }

    @Test
    void deposit_shouldFailWhenAccountNotFound() {

        BankService bank = new BankService();

        assertThrows(
                IllegalArgumentException.class,
                () -> bank.deposit("UNKNOWN", new BigDecimal("10"))
        );
    }

    @Test
    void transfer_shouldCreateTwoTransactions() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("200"));
        bank.createAccount("A2", "Bob", new BigDecimal("100"));

        bank.transfer("A1", "A2", new BigDecimal("50"));

        BankAccount source = bank.getAccount("A1");
        BankAccount target = bank.getAccount("A2");

        assertEquals(1, source.getTransactions().size());
        assertEquals(1, target.getTransactions().size());

        Transaction sourceTx = source.getTransactions().getFirst();
        Transaction targetTx = target.getTransactions().getFirst();

        assertEquals(TransactionType.TRANSFER_OUT, sourceTx.getType());
        assertEquals(TransactionType.TRANSFER_IN, targetTx.getType());
    }

    @Test
    void deposit_shouldRecordTransaction() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));

        bank.deposit("A1", new BigDecimal("40"));

        BankAccount account = bank.getAccount("A1");

        assertEquals(1, account.getTransactions().size());

        Transaction tx = account.getTransactions().getFirst();

        assertEquals(TransactionType.DEPOSIT, tx.getType());
        assertEquals(new BigDecimal("40"), tx.getAmount());
    }

    @Test
    void withdraw_shouldRecordTransaction() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));

        bank.withdraw("A1", new BigDecimal("25"));

        BankAccount account = bank.getAccount("A1");

        assertEquals(1, account.getTransactions().size());

        Transaction tx = account.getTransactions().getFirst();

        assertEquals(TransactionType.WITHDRAW, tx.getType());
        assertEquals(new BigDecimal("25"), tx.getAmount());
    }

    @Test
    void transfer_shouldRollbackWhenInsufficientFunds() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));
        bank.createAccount("A2", "Bob", new BigDecimal("100"));

        assertThrows(
                InsufficientFundsException.class,
                () -> bank.transfer("A1", "A2", new BigDecimal("200"))
        );

        BankAccount source = bank.getAccount("A1");
        BankAccount target = bank.getAccount("A2");

        assertEquals(0, source.getTransactions().size());
        assertEquals(0, target.getTransactions().size());

        assertEquals(new BigDecimal("100"), source.getBalance());
        assertEquals(new BigDecimal("100"), target.getBalance());
    }

    @Test
    void transactionsList_shouldBeImmutable() {

        BankService bank = new BankService();

        bank.createAccount("A1", "Alice", new BigDecimal("100"));

        BankAccount account = bank.getAccount("A1");

        assertThrows(
                UnsupportedOperationException.class,
                () -> account.getTransactions().add(
                        new Transaction("A1", TransactionType.DEPOSIT, new BigDecimal("10"))
                )
        );
    }
}
