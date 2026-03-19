package com.jackalcode.practice.demo;

import com.jackalcode.practice.BankService.BankService;
import com.jackalcode.practice.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
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
        int threadPoolSize = 5;
        int numberOfCustomers = 1000;
        long totalDepositByCustomers = 0;
        long totalWithdrawalByCustomers = 0;
        BigDecimal startingBalance = sharedAccount.getBalance();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        try {
            IntStream.rangeClosed(1, numberOfCustomers)
                    .forEach(number -> futureCustomers.add(executor.submit(
                            new Customer(sharedAccount.getAccountNumber(), bankService))));
        } finally {
            executor.shutdown();
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
        BigDecimal accountDeposits = sharedAccount.getTransactions().stream()
                        .filter(transaction -> transaction.getType().equals(TransactionType.DEPOSIT))
                                .map(Transaction::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal accountWithdraws = sharedAccount.getTransactions().stream()
                        .filter(transaction -> transaction.getType().equals(TransactionType.WITHDRAW))
                                .map(Transaction::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("=======================================================");
        System.out.println("Starting balance in shared account: " + startingBalance);
        System.out.println("Total deposit by customers: " + totalDepositByCustomers);
        System.out.println("Total withdrawn by customers: " + totalWithdrawalByCustomers);
        System.out.println("Account deposits: " + accountDeposits);
        System.out.println("Account withdrawals: " + accountWithdraws);
        System.out.println("Expected balance in shared account: " + expectedBalance);
        System.out.println("Final balance in shared account: " + finalBalance);
        System.out.println("Total number of transactions in shared account: " + sharedAccount.getTransactions().size());
        System.out.println("Successful deposits: " + BankService.successfulDeposits);
        System.out.println("Successful withdraws: " + BankService.successfulWithdrawals);
        System.out.println("========================================================");
    }
}
