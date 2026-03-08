package com.jackalcode.practice.service;


import com.jackalcode.practice.BankService.BankService;
import com.jackalcode.practice.domain.BankAccount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
