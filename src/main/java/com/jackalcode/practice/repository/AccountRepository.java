package com.jackalcode.practice.repository;

import com.jackalcode.practice.domain.BankAccount;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository {

    void save(BankAccount account);

    BankAccount findByAccountNumber(String accountNumber);

    boolean exists(String accountNumber);

    List<BankAccount> getAllAccounts();

    List<BankAccount> getAccountsWithBalanceGreaterThan(BigDecimal amount);

    BigDecimal getTotalMoneyInBank();
}
