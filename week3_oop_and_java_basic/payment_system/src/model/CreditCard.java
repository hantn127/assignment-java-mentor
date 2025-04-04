package model;

import java.math.BigDecimal;

public class CreditCard extends PaymentMethod {
    private static final BigDecimal creditLimit = new BigDecimal(5000);
    private static final BigDecimal foreignTransactionFee = new BigDecimal(0.02);

    public CreditCard(String methodId, String methodName, String userId, BigDecimal balance) {
        super(methodId, methodName, userId, balance);
    }

    public CreditCard() {
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
        lastBalance = balance;
        balance = balance.subtract(totalAmount);
        return true;
    }

    @Override
    public boolean processRefund(BigDecimal amount) {
        balance = balance.add(amount);
        return true;
    }
}
