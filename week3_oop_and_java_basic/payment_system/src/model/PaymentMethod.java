package model;

import java.math.BigDecimal;

public abstract class PaymentMethod {
    protected String methodId;
    protected String methodName;
    protected BigDecimal balance;

    public PaymentMethod(String methodId, String methodName, BigDecimal balance) {
        this.methodId = methodId;
        this.methodName = methodName;
        this.balance = balance;
    }

    public abstract boolean processPayment(BigDecimal amount, boolean isForeignTransaction);
    public abstract boolean processRefund(BigDecimal amount);

}
