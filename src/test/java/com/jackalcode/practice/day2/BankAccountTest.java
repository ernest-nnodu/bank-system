package com.jackalcode.practice.day2;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void shouldCreateAccountWithValidData() {
        BankAccount account =
                new BankAccount("12345", "John Doe", new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }

    @Test
    void shouldNotAllowNegativeInitialBalance() {
        assertThrows(IllegalArgumentException.class,
                () -> new BankAccount("123", "John", new BigDecimal("-1")));
    }

    @Test
    void depositShouldIncreaseBalance() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("100"));

        account.deposit(new BigDecimal("50"));

        assertEquals(new BigDecimal("150"), account.getBalance());
    }

    @Test
    void depositShouldRejectNullAmount() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("100"));

        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(null));
    }

    @Test
    void depositShouldRejectZeroOrNegativeAmount() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("100"));

        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(new BigDecimal("-10")));
    }

    @Test
    void withdrawShouldDecreaseBalance() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("100"));

        account.withdraw(new BigDecimal("40"));

        assertEquals(new BigDecimal("60"), account.getBalance());
    }

    @Test
    void withdrawShouldRejectNullAmount() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("100"));

        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(null));
    }

    @Test
    void withdrawShouldRejectZeroOrNegativeAmount() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("100"));

        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(BigDecimal.ZERO));

        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(new BigDecimal("-5")));
    }

    @Test
    void withdrawShouldFailWhenInsufficientFunds() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("50"));

        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(new BigDecimal("100")));
    }

    @Test
    void balanceShouldNeverBecomeNegative() {
        BankAccount account =
                new BankAccount("123", "John", new BigDecimal("100"));

        try {
            account.withdraw(new BigDecimal("200"));
        } catch (InsufficientFundsException ignored) {}

        assertEquals(new BigDecimal("100"), account.getBalance());
    }

    @Test
    void transferShouldMoveMoneyBetweenAccounts() {

        BankAccount source =
                new BankAccount("1", "Alice", new BigDecimal("100"));

        BankAccount target =
                new BankAccount("2", "Bob", new BigDecimal("50"));

        source.transferTo(target, new BigDecimal("30"));

        assertEquals(new BigDecimal("70"), source.getBalance());
        assertEquals(new BigDecimal("80"), target.getBalance());
    }

    @Test
    void transferShouldFailWhenInsufficientFunds() {

        BankAccount source =
                new BankAccount("1", "Alice", new BigDecimal("20"));

        BankAccount target =
                new BankAccount("2", "Bob", new BigDecimal("50"));

        assertThrows(
                InsufficientFundsException.class,
                () -> source.transferTo(target, new BigDecimal("100"))
        );
    }

    @Test
    void transferShouldNotChangeBalancesIfItFails() {

        BankAccount source =
                new BankAccount("1", "Alice", new BigDecimal("20"));

        BankAccount target =
                new BankAccount("2", "Bob", new BigDecimal("50"));

        try {
            source.transferTo(target, new BigDecimal("100"));
        } catch (InsufficientFundsException ignored) {}

        assertEquals(new BigDecimal("20"), source.getBalance());
        assertEquals(new BigDecimal("50"), target.getBalance());
    }

    @Test
    void transferShouldRejectSameAccount() {

        BankAccount account =
                new BankAccount("1", "Alice", new BigDecimal("100"));

        assertThrows(
                IllegalArgumentException.class,
                () -> account.transferTo(account, new BigDecimal("10"))
        );
    }

    @Test
    void transferShouldRejectNullTarget() {

        BankAccount account =
                new BankAccount("1", "Alice", new BigDecimal("100"));

        assertThrows(
                IllegalArgumentException.class,
                () -> account.transferTo(null, new BigDecimal("10"))
        );
    }
}
