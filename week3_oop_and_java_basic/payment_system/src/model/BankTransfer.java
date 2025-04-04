package model;

import java.math.BigDecimal;

public class BankTransfer extends PaymentMethod {
    private static final BigDecimal TRANSACTION_FEE_THRESHOLD = BigDecimal.valueOf(2000);
    private static final BigDecimal TRANSACTION_FEE_PERCENT = BigDecimal.valueOf(0.01);
    private static final int HOLD_DAYS = 3;
    private boolean isOnHold = false;

    public BankTransfer(String methodId, String methodName, String userId, BigDecimal balance) {
        super(methodId, methodName, userId, balance);
    }

    public BankTransfer(boolean isOnHold) {
        this.isOnHold = isOnHold;
    }

    @Override
    public boolean processPayment(BigDecimal amount, boolean isForeignTransaction) {
        BigDecimal finalAmount = amount;
        if (isForeignTransaction) {
            isOnHold = true;
        }
        if (amount.compareTo(TRANSACTION_FEE_THRESHOLD) > 0) {
            finalAmount = amount.multiply(TRANSACTION_FEE_PERCENT);
        }
        if (finalAmount.compareTo(balance) > 0) {
            return false;
        }
        lastBalance = balance;
        balance = balance.subtract(finalAmount);
        return true;
    }

    @Override
    public boolean processRefund(BigDecimal amount) {
        balance = balance.add(amount);
        return true;
    }

    public boolean isOnHold() {
        return isOnHold;
    }

    public void setOnHold(boolean onHold) {
        isOnHold = onHold;
    }
}
