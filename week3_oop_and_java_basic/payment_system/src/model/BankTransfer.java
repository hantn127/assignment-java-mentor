package model;

public class BankTransfer extends PaymentMethod {
    private static final double TRANSACTION_FEE_THRESHOLD = 2000;
    private static final double TRANSACTION_FEE_PERCENT = 0.01;
    private static final int HOLD_DAYS = 3;
    private boolean isOnHold = false;

    public BankTransfer(String methodId, String methodName, double balance) {
        super(methodId, methodName, balance);
    }

    @Override
    public boolean processPayment(double amount, boolean isForeignTransaction) {
        double finalAmount = amount;
        if (isForeignTransaction) {
            isOnHold = true;
        }
        if (amount > TRANSACTION_FEE_THRESHOLD) {
            finalAmount += amount * TRANSACTION_FEE_PERCENT;
        }
        if (finalAmount > balance) {
            return false;
        }
        balance -= finalAmount;
        return true;
    }

    @Override
    public boolean processRefund(double amount) {
        balance += amount;
        return true;
    }

    public boolean isOnHold() {
        return isOnHold;
    }

    public void setOnHold(boolean onHold) {
        isOnHold = onHold;
    }
}
