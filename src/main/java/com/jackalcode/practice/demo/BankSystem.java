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

        List<Future<TotalTransaction>> futureCustomers = new ArrayList<>();
        int numberOfThreads = 5;
        int numberOfCustomers = 100;
        long totalDepositByCustomers = 0;
        long totalWithdrawalByCustomers = 0;
        BigDecimal startingBalance = sharedAccount.getBalance();

        try (var executor = Executors.newFixedThreadPool(numberOfThreads)) {
            IntStream.rangeClosed(1, 100)
                    .forEach(number -> futureCustomers.add(executor.submit(
                            Customer.customerOf(sharedAccount.getAccountNumber(), bankService))));
        }

        for (Future<TotalTransaction> future : futureCustomers) {
            try {
                TotalTransaction totalTransaction = future.get();
                totalDepositByCustomers += totalTransaction.totalDeposit().longValue();
                totalWithdrawalByCustomers += totalTransaction.totalWithdraw().longValue();

            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }

        BigDecimal expectedBalance = startingBalance
                .add(BigDecimal.valueOf(totalDepositByCustomers))
                .subtract(BigDecimal.valueOf(totalWithdrawalByCustomers));

        BigDecimal finalBalance = sharedAccount.getBalance();

        System.out.println("Starting balance in shared account: " + startingBalance);
        System.out.println("Total deposit by customers: " + totalDepositByCustomers);
        System.out.println("Total withdrawn by customers: " + totalWithdrawalByCustomers);
        System.out.println("Expected balance in shared account: " + expectedBalance);
        System.out.println("Final balance in shared account: " + finalBalance);
    }
}
