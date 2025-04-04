package model;

import java.math.BigDecimal;

public abstract class PaymentMethod {
    protected String methodId;
    protected String methodName;
    protected String userId;
    protected BigDecimal balance;
    protected BigDecimal lastBalance;

    public PaymentMethod(String methodId, String methodName, String userId, BigDecimal balance) {
        this.methodId = methodId;
        this.methodName = methodName;
        this.userId = userId;
        this.balance = balance;
        this.lastBalance = balance;
    }

    public PaymentMethod() {}

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public abstract boolean processPayment(BigDecimal amount, boolean isForeignTransaction);
    public abstract boolean processRefund(BigDecimal amount);

    public void rollbackLastTransaction() {
        System.out.println("Rollback giao dịch cho phương thức: " + methodName);
        this.balance = lastBalance;
    }
}
