package com.jackalcode.practice.domain;

import java.math.BigDecimal;

public record TotalTransaction(
        BigDecimal totalDeposit,
        BigDecimal totalWithdraw
) {
}
