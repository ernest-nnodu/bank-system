package com.jackalcode.practice.repository;

import com.jackalcode.practice.domain.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void exists_shouldReturnTrueWhenAccountExists() {

        BankAccount account =
                new BankAccount("A1", "Alice", new BigDecimal("100"));

        repository.save(account);

        boolean exists = repository.exists("A1");

        assertTrue(exists);
    }

    @Test
    void exists_shouldReturnFalseWhenAccountDoesNotExist() {

        boolean exists = repository.exists("UNKNOWN");

        assertFalse(exists);
    }

    @Test
    void findByAccountNumber_shouldReturnStoredAccount() {

        BankAccount account =
                new BankAccount("A1", "Alice", new BigDecimal("200"));

        repository.save(account);

        BankAccount found = repository.findByAccountNumber("A1");

        assertEquals("Alice", found.getAccountHolderName());
        assertEquals(new BigDecimal("200"), found.getBalance());
    }

    @Test
    void findByAccountNumber_shouldThrowWhenAccountNotFound() {

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findByAccountNumber("UNKNOWN")
        );
    }

    @Test
    void save_shouldStoreMultipleAccounts() {

        BankAccount account1 =
                new BankAccount("A1", "Alice", new BigDecimal("100"));

        BankAccount account2 =
                new BankAccount("A2", "Bob", new BigDecimal("200"));

        repository.save(account1);
        repository.save(account2);

        assertTrue(repository.exists("A1"));
        assertTrue(repository.exists("A2"));
    }

    @Test
    void save_shouldFailWhenAccountAlreadyExists() {

        BankAccount account =
                new BankAccount("A1", "Alice", new BigDecimal("100"));

        repository.save(account);

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(account)
        );
    }

    @Test
    void getAllAccounts_shouldReturnAllAccounts() {

        repository.save(new BankAccount("A1", "Alice", new BigDecimal("100")));
        repository.save(new BankAccount("A2", "Bob", new BigDecimal("200")));

        List<BankAccount> result = repository.getAllAccounts();

        assertEquals(2, result.size());
    }

    @Test
    void getAccountsWithBalanceGreaterThan_shouldFilterAccounts() {

        repository.save(new BankAccount("A1", "Alice", new BigDecimal("100")));
        repository.save(new BankAccount("A2", "Bob", new BigDecimal("300")));
        repository.save(new BankAccount("A3", "Charlie", new BigDecimal("50")));

        List<BankAccount> result =
                repository.getAccountsWithBalanceGreaterThan(new BigDecimal("100"));

        assertEquals(1, result.size());
        assertEquals("A2", result.get(0).getAccountNumber());
    }
}
