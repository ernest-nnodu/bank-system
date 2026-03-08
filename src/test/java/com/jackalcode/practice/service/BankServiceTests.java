package com.jackalcode.practice.service;


import com.jackalcode.practice.BankService.BankService;
import com.jackalcode.practice.domain.BankAccount;
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
}
