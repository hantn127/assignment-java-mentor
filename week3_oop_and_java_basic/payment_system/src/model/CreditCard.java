package model;

import java.math.BigDecimal;

public class CreditCard extends PaymentMethod {
    private BigDecimal creditLimit;
    private BigDecimal foreignTransactionFee = BigDecimal.valueOf(0.02);

    public CreditCard(String methodId, String methodName, BigDecimal balance, BigDecimal creditLimit, BigDecimal foreignTransactionFee) {
        super(methodId, methodName, balance);
        this.creditLimit = creditLimit;
        this.foreignTransactionFee = foreignTransactionFee;
    }

    @Override
    public boolean processPayment(BigDecimal amount, boolean isForeignTransaction) {
        BigDecimal totalAmount = amount;
        if (isForeignTransaction) {
            totalAmount = totalAmount.add(amount.multiply(foreignTransactionFee));
        }
        if (totalAmount.compareTo(balance.add(creditLimit)) > 0) {
            return false;
        }
        balance = balance.subtract(totalAmount);
        return true;
    }

    @Override
    public boolean processRefund(BigDecimal amount) {
        balance = balance.add(amount);
        return true;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getForeignTransactionFee() {
        return foreignTransactionFee;
    }

    public void setForeignTransactionFee(BigDecimal foreignTransactionFee) {
        this.foreignTransactionFee = foreignTransactionFee;
    }
}
