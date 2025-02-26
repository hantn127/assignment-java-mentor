package model;

public class CreditCard extends PaymentMethod {
    private double creditLimit;
    private double foreignTransactionFee = 0.02;

    public CreditCard(String methodId, String methodName, double balance, double creditLimit, double foreignTransactionFee) {
        super(methodId, methodName, balance);
        this.creditLimit = creditLimit;
        this.foreignTransactionFee = foreignTransactionFee;
    }

    @Override
    public boolean processPayment(double amount, boolean isForeignTransaction) {
        double totalAmount = amount;
        if (isForeignTransaction) {
            totalAmount += amount * foreignTransactionFee;
        }
        if (totalAmount > (balance + creditLimit)) {
            return false;
        }
        balance -= totalAmount;
        return true;
    }

    @Override
    public boolean processRefund(double amount) {
        balance += amount;
        return true;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public double getForeignTransactionFee() {
        return foreignTransactionFee;
    }

    public void setForeignTransactionFee(double foreignTransactionFee) {
        this.foreignTransactionFee = foreignTransactionFee;
    }
}
