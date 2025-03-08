package model;

import constance.UserStatus;

import java.math.BigDecimal;

public class User {
    private String userId;
    private String username;
    private String password;
    private UserStatus status;
    private BigDecimal balance;
//    private List<PaymentMethod> paymentMethods;
    private int failedLoginAttempts;
    private int failedOtpAttempts;

    public User(String userId, String username, String password, UserStatus status, BigDecimal balance, int failedLoginAttempts, int failedOtpAttempts) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.status = status;
        this.balance = balance;
        this.failedLoginAttempts = failedLoginAttempts;
        this.failedOtpAttempts = failedOtpAttempts;
//        this.paymentMethods = paymentMethods;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

//    public List<PaymentMethod> getPaymentMethods() {
//        return paymentMethods;
//    }
//
//    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
//        this.paymentMethods = paymentMethods;
//    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public int getFailedOtpAttempts() {
        return failedOtpAttempts;
    }

    public void setFailedOtpAttempts(int failedOtpAttempts) {
        this.failedOtpAttempts = failedOtpAttempts;
    }
}
