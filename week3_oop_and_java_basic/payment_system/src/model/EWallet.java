package model;

public class EWallet extends PaymentMethod {
    private static final double DAILY_LIMIT = 5000;

    public EWallet(String methodId, String methodName,  double balance) {
        super(methodId, methodName, balance);
    }

    @Override
    public boolean processPayment(double amount, boolean isForeignTransaction) {
        if (amount > DAILY_LIMIT || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public boolean processRefund(double amount) {
        balance += amount;
        return true;
    }
}
