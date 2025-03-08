package model;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private String transactionId;
    private String userId;
    private String paymentMethod;
    private String transactionType;
    private BigDecimal amount;
    private Date date;
    private String status;

    public Transaction(String transactionId, String userId, String paymentMethod, String transactionType, BigDecimal amount, Date date, String status) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.paymentMethod = paymentMethod;
        this.transactionType = transactionType;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Mã GD: %s | Phương thức: %s | Loại: %s | Số tiền: %.2f | Ngày: %s | Trạng thái: %s",
                transactionId, paymentMethod, transactionType, amount, date, status);
    }

}
