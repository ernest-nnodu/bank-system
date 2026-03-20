package com.jackalcode.practice.demo;

import com.jackalcode.practice.service.BankService;
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

        //list of future result from each customer thread
        List<Future<TotalTransaction>> futureCustomers = new ArrayList<>();

        int threadPoolSize = 5;
        int numberOfCustomers = 1000;
        BigDecimal totalDepositByCustomers = BigDecimal.ZERO;
        BigDecimal totalWithdrawalByCustomers = BigDecimal.ZERO;
        BigDecimal startingBalance = sharedAccount.getBalance();
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        //Create customer threads and submit to executor service to run tasks
        try {
            IntStream.rangeClosed(1, numberOfCustomers)
                    .forEach(number -> futureCustomers.add(executor.submit(
                            new Customer(sharedAccount.getAccountNumber(), bankService))));
        } finally {
            executor.shutdown();
        }

        //Wait for each customer thread to finish and get their results. Calculate total deposits and withdraws from customer results
        for (Future<TotalTransaction> future : futureCustomers) {
            try {
                TotalTransaction totalTransaction = future.get();
                totalDepositByCustomers = totalDepositByCustomers.add(totalTransaction.totalDeposit());
                totalWithdrawalByCustomers = totalWithdrawalByCustomers.add(totalTransaction.totalWithdraw());

            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
        }

        //Calculate expected balance in shared account based on total transactions made by customers
        BigDecimal expectedBalance = startingBalance
                .add(totalDepositByCustomers)
                .subtract(totalWithdrawalByCustomers);

        //Get shared account balance
        BigDecimal finalBalance = sharedAccount.getBalance();

        //Get total deposit amount from shared account transactions
        BigDecimal accountDeposits = sharedAccount.getTransactions().stream()
                        .filter(transaction -> transaction.getType().equals(TransactionType.DEPOSIT))
                                .map(Transaction::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        //Calculate total withdraw amount from shared account transactions
        BigDecimal accountWithdraws = sharedAccount.getTransactions().stream()
                        .filter(transaction -> transaction.getType().equals(TransactionType.WITHDRAW))
                                .map(Transaction::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        /*
         * Determine simulation success by comparing -
         * expected balance and actual balance in shared account
         * total deposits made by customers and total deposit transactions in shared account
         * total withdrawals made by customers and total withdraw transactions in shared account
         */
        boolean simulationSuccessful = (expectedBalance.compareTo(finalBalance) == 0) &&
                (accountDeposits.compareTo(totalDepositByCustomers) == 0) &&
                (accountWithdraws.compareTo(totalWithdrawalByCustomers) == 0);

        System.out.println("=======================================================");
        System.out.println("Starting balance in shared account: " + startingBalance);
        System.out.println("Total deposit by customers: " + totalDepositByCustomers);
        System.out.println("Account deposits: " + accountDeposits);
        System.out.println("Total withdrawn by customers: " + totalWithdrawalByCustomers);
        System.out.println("Account withdrawals: " + accountWithdraws);
        System.out.println("Total number of transactions in shared account: " + sharedAccount.getTransactions().size());
        System.out.println("Successful deposits: " + BankService.successfulDeposits);
        System.out.println("Successful withdraws: " + BankService.successfulWithdrawals);
        System.out.println("Expected balance in shared account: " + expectedBalance);
        System.out.println("Final balance in shared account: " + finalBalance);
        System.out.println("Simulation successful? " + simulationSuccessful);
        System.out.println("========================================================");
    }
}
