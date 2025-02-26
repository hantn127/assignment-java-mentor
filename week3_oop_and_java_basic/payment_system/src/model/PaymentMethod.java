package model;

public abstract class PaymentMethod {
    protected String methodId;
    protected String methodName;
    protected double balance;

    public PaymentMethod(String methodId, String methodName, double balance) {
        this.methodId = methodId;
        this.methodName = methodName;
        this.balance = balance;
    }

    public abstract boolean processPayment(double amount, boolean isForeignTransaction);
    public abstract boolean processRefund(double amount);

}
