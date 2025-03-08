package service;

import model.Transaction;

public interface RefundService {
    void processRefund(String userId);
}
