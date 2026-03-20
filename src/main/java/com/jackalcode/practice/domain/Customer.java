package com.jackalcode.practice.domain;

import com.jackalcode.practice.service.BankService;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.random.RandomGenerator;

public class Customer implements Callable<TotalTransaction> {
    @Getter
    private final String accountNumber;
    private final BankService bankService;
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

        BigDecimal totalDeposit = BigDecimal.ZERO;
        BigDecimal totalWithdrawn = BigDecimal.ZERO;

        for (int cycle = 0; cycle < NUMBER_OF_TRANSACTIONS; cycle++) {

            if (generateTransactionType() == TransactionType.DEPOSIT) {
                totalDeposit = totalDeposit.add(deposit(generateAmount()));
            } else {
                totalWithdrawn = totalWithdrawn.add(withdraw(generateAmount()));
            }
        }

        //Return total successful deposit and withdraw made by customer
        return new TotalTransaction(totalDeposit, totalWithdrawn);
    }

    private TransactionType generateTransactionType() {

        return generator.nextInt(1, 3) == 1 ? TransactionType.DEPOSIT : TransactionType.WITHDRAW;
    }

    private int generateAmount() {
        return generator.nextInt(1, 101);
    }

    private BigDecimal deposit(int amount) {

        try {
            //Return deposited amount if deposit successful
            return bankService.deposit(accountNumber, BigDecimal.valueOf(amount));
        } catch (Exception e) {
            //Return zero if deposit unsuccessful
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal withdraw(int amount) {

        try {
            //Return withdraw amount if withdraw successful
            return bankService.withdraw(accountNumber, BigDecimal.valueOf(amount));
        } catch (Exception e) {
            //Return zero if withdraw unsuccessful
            return BigDecimal.ZERO;
        }
    }
}
