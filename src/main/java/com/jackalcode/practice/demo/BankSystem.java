package com.jackalcode.practice.demo;

import com.jackalcode.practice.BankService.BankService;
import com.jackalcode.practice.domain.BankAccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class BankSystem {

    public static void main(String[] args) {

        var bankService = new BankService();
        BankAccount sharedAccount = bankService.createAccount(
                "1234",
                "john",
                BigDecimal.valueOf(10_000L));

        List<Future<?>> futureCustomers = new ArrayList<>();
        int numberOfThreads = 5;
        int numberOfCustomers = 100;
        int totalDepositPerThread = 550;
        int totalWithdrawalPerThread = 550;
        BigDecimal startingBalance = sharedAccount.getBalance();
        BigDecimal expectedBalance = startingBalance
                .add(BigDecimal.valueOf(totalDepositPerThread * numberOfCustomers))
                .subtract(BigDecimal.valueOf(totalWithdrawalPerThread * numberOfCustomers));

        try (var executor = Executors.newFixedThreadPool(numberOfThreads)) {
            IntStream.rangeClosed(1, 100)
                    .forEach(number -> futureCustomers.add(executor.submit(
                            Customer.customerOf(sharedAccount.getAccountNumber(), bankService))));
        }

        futureCustomers.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        });

        BigDecimal finalBalance = sharedAccount.getBalance();

        System.out.println("Starting balance in shared account: " + startingBalance);
        System.out.println("Expected balance in shared account: " + expectedBalance);
        System.out.println("Final balance in shared account: " + finalBalance);
    }
}
