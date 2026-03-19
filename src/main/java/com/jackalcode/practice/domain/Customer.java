package com.jackalcode.practice.domain;

import com.jackalcode.practice.BankService.BankService;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.random.RandomGenerator;

public class Customer implements Callable<TotalTransaction> {
    @Getter
    private final String accountNumber;
    private final BankService bankService;
    private TotalTransaction totalTransaction;
    private static final int NUMBER_OF_TRANSACTIONS = 50;
    private final RandomGenerator generator = RandomGenerator.getDefault();



    public Customer (String accountNumber, BankService bankService) {
        Objects.requireNonNull(accountNumber, "Bank account number is required");
        Objects.requireNonNull(bankService, "Bank service is required");
        this.accountNumber = accountNumber;
        this.bankService = bankService;
    }

    @Override
    public TotalTransaction call() {
        //RandomGenerator generator = RandomGenerator.getDefault();
        BigDecimal totalDeposit = BigDecimal.ZERO;
        BigDecimal totalWithdrawn = BigDecimal.ZERO;

        for (int cycle = 0; cycle < NUMBER_OF_TRANSACTIONS; cycle++) {

            totalDeposit = totalDeposit.add(deposit(getAmount()));

            totalWithdrawn = totalWithdrawn.add(withdraw(10));
        }

        return new TotalTransaction(totalDeposit, totalWithdrawn);
    }

    private int getAmount() {
        //return generator.nextInt(1, 101);
        return 1;
    }

    private BigDecimal deposit(int amount) {
        //System.out.printf("Depositing $%d from %s%n", amount, Thread.currentThread().getName());

        BigDecimal deposit = bankService.deposit(accountNumber, BigDecimal.valueOf(amount));

        //Return zero if deposit was unsuccessful
        if (deposit == null) {
            return BigDecimal.ZERO;
        }
        return deposit;
    }

    private BigDecimal withdraw(int amount) {
        //System.out.printf("Withdrawing $%d from %s%n", amount, Thread.currentThread().getName());


        BigDecimal withdraw = bankService.withdraw(accountNumber, BigDecimal.valueOf(amount));

        //Return zero if withdraw was unsuccessful
        if (withdraw == null) {
            System.out.println((Object) null);
            return BigDecimal.ZERO;
        }

        return withdraw;
    }
}
