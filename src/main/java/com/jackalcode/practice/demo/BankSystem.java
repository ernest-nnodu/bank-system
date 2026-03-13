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
        List<Future<?>> futureCustomers = new ArrayList<>();

        var bankService = new BankService();
        BankAccount sharedAccount = bankService.createAccount(
                "1234",
                "john",
                BigDecimal.valueOf(10_000L));
        System.out.println("Starting balance in shared account: " + sharedAccount.getBalance());

        try (var executor = Executors.newFixedThreadPool(5)) {
            IntStream.rangeClosed(1, 100)
                    .forEach(number -> futureCustomers.add(executor.submit(
                            Customer.customerOf(sharedAccount.getAccountNumber(), bankService))));
        }

        futureCustomers.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println("Final balance in shared account: " + sharedAccount.getBalance());
    }
}
