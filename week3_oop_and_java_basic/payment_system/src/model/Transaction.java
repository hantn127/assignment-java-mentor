package model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private String transactionId;
    private User user;
    private PaymentMethod paymentMethod;
    private String transactionType;
    private BigDecimal amount;
    private Date date;
    private boolean isSuccessful;

    public Transaction(String transactionId, User user, PaymentMethod paymentMethod, String transactionType, BigDecimal amount, Date date, boolean isSuccessful) {
        this.transactionId = transactionId;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.isSuccessful = isSuccessful;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}
