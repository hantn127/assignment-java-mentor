package service;

import model.PaymentMethod;

public interface PaymentService {
    void processPayment(String userId);
    boolean addPaymentMethod(String userId, PaymentMethod paymentMethod);
    boolean removePaymentMethod(String userId, String paymentMethodId);
}
