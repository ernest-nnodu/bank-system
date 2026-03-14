package com.jackalcode.practice.demo;

import java.math.BigDecimal;

public class TotalTransaction {

    private BigDecimal totalDeposit;
    private BigDecimal totalWithdraw;

    public BigDecimal getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(BigDecimal totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public BigDecimal getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(BigDecimal totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }
}
