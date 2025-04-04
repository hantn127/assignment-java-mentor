package service;

import model.PaymentMethod;
import model.Transaction;
import model.User;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void saveTransactions(List<String> transactionLogs);
    void saveTransaction(String userId, String method, String transactionType, BigDecimal amount, String status);
    List<Transaction> getTransactions(String userId, String status);
    void displayFinancialReport(String userId);
}
