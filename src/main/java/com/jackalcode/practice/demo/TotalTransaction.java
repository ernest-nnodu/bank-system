package com.jackalcode.practice.demo;

import java.math.BigDecimal;

public record TotalTransaction(
        BigDecimal totalDeposit,
        BigDecimal totalWithdraw
) {
}
