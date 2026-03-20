# Concurrent Banking System Simulation
## Overview

The Concurrent Banking System Simulation is a Java-based project designed to demonstrate thread safety, concurrent transactions, and deterministic financial correctness under heavy load.

The system simulates 100 concurrent customers performing thousands of random deposits and withdrawals on a single shared bank account. The objective is to verify that the system maintains data integrity, consistency, and correctness even under high contention.

This project focuses on fundamental concurrency concepts in Java, including:

- Thread pools

- Task execution using executors

- Synchronization and critical sections

- Shared mutable state protection

- Deterministic correctness under random workloads

-Concurrent task coordination

The system validates that the final balance always matches the mathematically expected balance, ensuring that no transaction is lost or corrupted.

## Project Goals

The primary goal of the system is to simulate real-world financial contention where many clients attempt to modify the same account simultaneously.

The system must guarantee:

Correct financial calculations

Thread-safe operations

No race conditions

Deterministic outcomes

Clean system shutdown

## Core Features
### 1. Shared Account Simulation

The system creates a single shared bank account with an opening balance.
All customer threads interact with this account concurrently.

Supported account operations:

- deposit(amount) – Adds money to the account

- withdraw(amount) – Removes money if sufficient funds exist

- getBalance() – Retrieves the current account balance

All financial updates are protected using synchronization mechanisms to prevent race conditions.

### 2. Concurrent Customer Transactions

Each customer operates as an independent task executed by a thread.

Each customer performs 50 cycles of transactions consisting of:

- One random deposit

- One random withdrawal

Transaction values are randomly generated between:

$1 - $100

This results in approximately:

100 customers × 100 transactions = 10,000 total transactions

### 3. Executor-Based Thread Management

Customer threads are managed using a fixed-size thread pool.

The system uses a thread pool of 5 worker threads to simulate limited computing resources handling many concurrent users.

Tasks are scheduled and executed through the executor service.

### 4. Transaction Integrity

The system ensures that the following financial invariant is always maintained:

finalBalance = startingBalance + totalDeposits - totalWithdrawals

At the end of the simulation the program verifies:

- Total deposits made by all customers

- Total withdrawals made by all customers

- The final account balance

If the numbers do not match, the system has failed.

### 5. Thread-Safe Transaction Processing

To prevent race conditions, all financial operations are executed within a critical section protected by a transaction lock.

This guarantees that:

- Deposits cannot overwrite each other

- Withdrawals cannot cause negative balances due to concurrent reads

- Balance updates remain atomic

The synchronization model ensures that only one thread modifies the account balance at a time.

## System Architecture

The project follows a simple layered architecture.

**Customer Threads -> BankService -> BankAccount**
   
### Components

#### BankAccount

Represents the domain model of a bank account.

Responsibilities:

- Store account metadata

- Maintain the account balance

- Perform deposit operations

- Perform withdrawal operations

- Enforce balance validation

This class represents the core financial state of the system.

#### BankService

Acts as the transaction manager responsible for coordinating operations on accounts.

Responsibilities:

- Create new accounts

- Retrieve existing accounts

- Execute deposits on a bank account

- Execute withdrawals on a bank account

- Ensure thread-safe financial operations

The service protects financial operations using a transaction lock to guarantee atomic updates.

#### Customer

Represents a simulated bank customer executing transactions concurrently.

Each customer:

- Performs multiple deposit and withdrawal operations

- Returns a summary of total deposits and withdrawals

Customer threads do not modify account data directly, All operations pass through the BankService layer.

#### BankSimulation (Main Application)

The main application orchestrates the entire simulation.

Responsibilities:

- Create the banking service

- Initialize the shared account

- Create customer tasks

- Submit tasks to the executor service

- Wait for all transactions to complete

- Aggregate results

- Verify the final balance

### Concurrency Model

The system relies on a coarse-grained locking strategy.

**100 Customer Tasks → Executor Thread Pool → Bank service lock → Shared Account Balance**

This model guarantees:

- Correctness

- Simplicity

- Deterministic outcomes

Although multiple threads generate requests simultaneously, balance updates are serialized through the lock.

## Simulation Flow

The simulation executes in the following sequence:

1. Create the banking service

2. Create a shared bank account with an initial balance

3. Initialize a thread pool

4. Generate 100 customer tasks

5. Submit tasks to the executor

6. Customers perform random transactions

7. All tasks complete

8. Transaction totals are aggregated

9. Expected balance is calculated

10. Final balance is retrieved

## Running the Simulation
### Requirements

Java 17 or later

### Steps
1. Clone the repository
2. Navigate to the java directory
3. Compile the project → javac com/jackalcode/practice/**/*.java
4. Run the simulation → java com.jackalcode.practice.demo.BankSystem

### Example Output
- Starting balance in shared account: 10000
- Total deposit by customers: 1265863
- Account deposits: 1265863
- Total withdrawn by customers: 1259727
- Account withdrawals: 1259727
- Total number of transactions in shared account: 50000
- Successful deposits: 25088
- Successful withdraws: 24912
- Expected balance in shared account: 16136
- Final balance in shared account: 16136
- Simulation successful? true


If the expected balance equals the actual balance, the system passed the concurrency test.
