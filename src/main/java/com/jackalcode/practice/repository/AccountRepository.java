package com.jackalcode.practice.repository;

import com.jackalcode.practice.domain.BankAccount;

public interface AccountRepository {

    void save(BankAccount account);
    BankAccount findByAccountNumber(String accountNumber);
    boolean exists(String accountNumber);
}
