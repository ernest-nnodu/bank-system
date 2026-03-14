package com.jackalcode.practice.demo;

import com.jackalcode.practice.BankService.BankService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

public class Customer implements Callable<TotalTransaction> {
    private final String accountNumber;
    private final BankService bankService;
    private TotalTransaction totalTransaction;

    public static Customer customerOf(String accountNumber, BankService bankService) {
        Objects.requireNonNull(accountNumber, "Bank account number is required");
        Objects.requireNonNull(bankService, "Bank service is required");
        return new Customer(accountNumber, bankService);
    }

    private Customer(String accountNumber, BankService bankService) {
        this.accountNumber = accountNumber;
        this.bankService = bankService;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public TotalTransaction call() {
        RandomGenerator generator = RandomGenerator.getDefault();
        BigDecimal totalDeposit = BigDecimal.ZERO;
        BigDecimal totalWithdrawn = BigDecimal.ZERO;

        for (int cycle = 0; cycle < 50; cycle++) {
            int depositAmount = getAmount();
            totalDeposit = totalDeposit.add(deposit(depositAmount));

            int withdrawAmount = getAmount();
            totalWithdrawn = totalWithdrawn.add(withdraw(withdrawAmount));
        }

        return new TotalTransaction(totalDeposit, totalWithdrawn);
    }

    private static int getAmount() {
        RandomGenerator generator = RandomGenerator.getDefault();
        return generator.nextInt(1, 101);
    }

    private BigDecimal deposit(int amount) {
        BigDecimal deposit = bankService.deposit(accountNumber, BigDecimal.valueOf(amount));

        if (deposit == null) {
            return BigDecimal.ZERO;
        }
        return deposit;
    }

    private BigDecimal withdraw(int amount) {
        BigDecimal withdraw = bankService.withdraw(accountNumber, BigDecimal.valueOf(amount));

        if (withdraw == null) {
            return BigDecimal.ZERO;
        }

        return withdraw;
    }
}
