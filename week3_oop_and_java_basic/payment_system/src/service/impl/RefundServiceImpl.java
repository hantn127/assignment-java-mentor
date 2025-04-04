package service.impl;

import model.*;
import service.RefundService;
import service.TransactionService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.IntStream;

public class RefundServiceImpl implements RefundService {
    private static final String PAYMENT_METHOD_FILE = "week3_oop_and_java_basic/payment_system/src/data/payment_methods.txt";
    private static final String TRANSACTION_TYPE = "REFUND";
    private static final Scanner scanner = new Scanner(System.in);

    public void processRefund(String userId) {
        List<Transaction> refundableTransactions = getTransactions(userId, "SUCCESS");
        if (refundableTransactions.isEmpty()) {
            System.out.println("Không có giao dịch đủ điều kiện hoàn tiền.");
            return;
        }

        int selectedTransactionIndex = showRefundableTransactions(refundableTransactions);

        Transaction selectedTransaction = refundableTransactions.get(selectedTransactionIndex);
        System.out.println("Giao dịch đã chọn: " + selectedTransaction);

        processRefundWithMethod(userId, selectedTransaction.getPaymentMethod(), selectedTransaction.getAmount());
    }

    private List<Transaction> getTransactions(String userId, String status) {
        TransactionService transactionService = new TransactionServiceImpl();
        return transactionService.getTransactions(userId, status);
    }

    private Optional<PaymentMethod> getPaymentMethod(String method) {
        try (BufferedReader br = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            return br.lines()
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length == 4 && parts[1].trim().equalsIgnoreCase(method))
                    .map(parts -> {
                        String methodId = parts[0].trim();
                        String storedMethod = parts[1].trim();
                        String storedUserId = parts[2].trim();
                        BigDecimal balance = new BigDecimal(parts[3].trim());

                        switch (storedMethod) {
                            case "BankTransfer":
                                return new BankTransfer(methodId, storedMethod, storedUserId, balance);
                            case "CreditCard":
                                return new CreditCard(methodId, storedMethod, storedUserId, balance);
                            case "EWallet":
                                return new EWallet(methodId, storedMethod, storedUserId, balance);
                            default:
                                return null;
                        }
                    })
                    .filter(Objects::nonNull) // Filter out null results
                    .findFirst(); // Return the first match (if any)
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi đọc phương thức thanh toán: " + e.getMessage());
        }
        return Optional.empty(); // Return an empty optional if no match
    }

    private int showRefundableTransactions(List<Transaction> refundableTransactions) {
        System.out.println("Danh sách giao dịch có thể hoàn tiền:");
        IntStream.range(0, refundableTransactions.size())
                .forEach(i -> System.out.println((i + 1) + ". " + refundableTransactions.get(i)));

        System.out.print("Chọn giao dịch cần hoàn tiền (nhập số thứ tự): ");
        int choice = scanner.nextInt();

        if (choice > 0 && choice <= refundableTransactions.size()) {
            return choice - 1;
        } else {
            return -1;
        }
    }

    private void processRefundWithMethod(String userId, String method, BigDecimal amount) {
        Optional<PaymentMethod> paymentMethod = getPaymentMethod(method);

        if (paymentMethod.isPresent()) {
            boolean transactionSuccessful = paymentMethod.get().processRefund(amount);

            if (transactionSuccessful) {
                System.out.println("Hoàn tiền thành công bằng phương thức " + method);
                saveTransaction(userId, method, TRANSACTION_TYPE, amount, "SUCCESS");
            } else {
                System.out.println("Lỗi khi xử lý hoàn tiền với phương thức " + method);
                saveTransaction(userId, method, TRANSACTION_TYPE, amount, "FAILED");
            }
        } else {
            System.out.println("Phương thức hoàn tiền không hợp lệ.");
        }
    }

    private void saveTransaction(String userId, String method, String transactionType, BigDecimal amount, String status) {
        TransactionService transactionService = new TransactionServiceImpl();
        transactionService.saveTransaction(userId, method, TRANSACTION_TYPE, amount, status);
    }

}
