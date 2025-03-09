package service;

import model.PaymentMethod;

public interface PaymentService {
    void processPayment(String userId);
    boolean addPaymentMethod(String userId);
    boolean removePaymentMethod(String userId);
}
