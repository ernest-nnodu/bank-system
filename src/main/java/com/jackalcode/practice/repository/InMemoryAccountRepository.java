package com.jackalcode.practice.repository;

import com.jackalcode.practice.domain.BankAccount;

public class InMemoryAccountRepository implements AccountRepository {
    @Override
    public void save(BankAccount account) {

    }

    @Override
    public BankAccount findByAccountNumber(String accountNumber) {
        return null;
    }

    @Override
    public boolean exists(String accountNumber) {
        return false;
    }
}
