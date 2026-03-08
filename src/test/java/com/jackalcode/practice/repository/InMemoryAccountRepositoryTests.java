package com.jackalcode.practice.repository;

import com.jackalcode.practice.domain.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryAccountRepositoryTests {

    private InMemoryAccountRepository repository;

    @BeforeEach
    void setup() {
        repository = new InMemoryAccountRepository();
    }

    @Test
    void save_shouldStoreAccount() {

        BankAccount account =
                new BankAccount("A1", "Alice", new BigDecimal("100"));

        repository.save(account);

        BankAccount found = repository.findByAccountNumber("A1");

        assertNotNull(found);
        assertEquals("A1", found.getAccountNumber());
    }
}
