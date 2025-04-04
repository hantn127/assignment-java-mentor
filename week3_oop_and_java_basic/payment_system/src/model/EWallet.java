package model;

import java.math.BigDecimal;

public class EWallet extends PaymentMethod {
    private static final BigDecimal DAILY_LIMIT = BigDecimal.valueOf(5000);

    public EWallet(String methodId, String methodName, String userId, BigDecimal balance) {
        super(methodId, methodName, userId, balance);
    }

    public EWallet() {
    }

    @Override
    public boolean processPayment(BigDecimal amount, boolean isForeignTransaction) {
        if (amount.compareTo(DAILY_LIMIT) > 0 || amount.compareTo(balance) > 0) {
            System.out.println("Số tiền cần thanh toán vượt quá giới hạn tiền trong ngày của phương thức thanh toán!");
            return false;
        }
        lastBalance = balance;
        balance = balance.subtract(amount);
        return true;
    }

    @Override
    public boolean processRefund(BigDecimal amount) {
        balance = balance.add(amount);
        return true;
    }
}
