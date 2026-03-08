package com.jackalcode.practice.BankService;

import com.jackalcode.practice.domain.BankAccount;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BankService {

    private final Map<String, BankAccount> accounts;

    public BankService() {
        this.accounts = new HashMap<>();
    }

    public BankAccount createAccount(String accountNumber, String accountHolderName, BigDecimal startingBalance) {

        Objects.requireNonNull(accountNumber, "Account number is required");
        Objects.requireNonNull(accountHolderName, "Account holder name is required");
        Objects.requireNonNull(startingBalance, "Starting balance is required");

        BankAccount newAccount = null;
        try {
            newAccount = new BankAccount(accountNumber, accountHolderName, startingBalance);
            accounts.put(accountNumber, newAccount);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return newAccount;
    }
}
