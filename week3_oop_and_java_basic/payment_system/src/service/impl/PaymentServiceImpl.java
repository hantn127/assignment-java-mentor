package service.impl;

import model.*;
import service.FraudDetectionService;
import service.OTPService;
import service.PaymentService;
import service.TransactionService;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_METHOD_FILE = "week3_oop_and_java_basic/payment_system/src/data/payment_methods.txt";
    private static final String TRANSACTION_TYPE = "Payment";
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void processPayment(String userId) {
        BigDecimal amount = getUserInputAmount();
        String method = selectPaymentMethod();

        if (amount.compareTo(BigDecimal.valueOf(5000)) > 0) {
            System.out.println("Số tiền lớn hơn 5000$, cần xác minh OTP.");
            if (!verifyOTP()) {
                System.out.println("Xác minh OTP thất bại, hủy giao dịch.");
                return;
            }
        }

        // Random
        if (isFraudulentTransaction(userId, amount)) {
            System.out.println("Giao dịch có dấu hiệu gian lận. Giao dịch này đã bị chặn, yêu cầu xác minh qua call center.");
            saveTransaction(userId, method, TRANSACTION_TYPE, amount, "SUSPICIOUS");
            return;
        }

        if (checkBalance(userId, method, amount)) {
            processPaymentWithMethod(userId, method, amount);
        } else {
            System.out.println("Số dư không đủ.");
            handleInsufficientBalance(userId, method, amount);
        }
    }

    @Override
    public boolean addPaymentMethod(String userId, PaymentMethod paymentMethod) {
        if (isPaymentMethodExist(userId, paymentMethod)) {
            System.out.println("Phương thức thanh toán đã tồn tại.");
            return false;
        }

        savePaymentMethod(userId, paymentMethod); // Lưu vào file
        System.out.println("Phương thức thanh toán đã được thêm thành công.");
        return true;
    }

    @Override
    public boolean removePaymentMethod(String userId, String paymentMethodId) {
        if (isPendingTransaction(userId, paymentMethodId)) {
            System.out.println("Không thể xóa phương thức thanh toán, có giao dịch đang chờ xử lý.");
            return false;
        }

        deletePaymentMethod(userId, paymentMethodId);
        System.out.println("Phương thức thanh toán đã được xóa thành công.");
        return true;
    }

    private boolean isPendingTransaction(String userId, String paymentMethodType) {
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String transactionUserId = parts[1];
                    String transactionMethod = parts[2];
                    String status = parts[6];

                    if (transactionUserId.equals(userId) && transactionMethod.equals(paymentMethodType)
                            && status.equals("FAILED")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file giao dịch: " + e.getMessage());
        }
        return false;
    }

    private void deletePaymentMethod(String userId, String paymentMethodType) {
        File inputFile = new File("payment_methods.txt");
        File tempFile = new File("payment_methods_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String existingUserId = parts[2];
                    String existingMethod = parts[1];

                    if (existingUserId.equals(userId) && existingMethod.equals(paymentMethodType)) {
                        continue;
                    }
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi cập nhật file: " + e.getMessage());
            return;
        }

        // Đổi tên file tạm thành file gốc
        if (!inputFile.delete()) {
            System.out.println("Lỗi khi xóa file cũ.");
            return;
        }
        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Lỗi khi đổi tên file mới.");
        }
    }

    private boolean isPaymentMethodExist(String userId, PaymentMethod paymentMethod) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String existingUserId = parts[2];
                    String existingMethod = parts[1];

                    if (existingUserId.equals(userId) && existingMethod.equals(paymentMethod.getMethodName())) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
        }
        return false;
    }

    private void savePaymentMethod(String userId, PaymentMethod paymentMethod) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_METHOD_FILE, true))) {
            String newId = generatePaymentMethodId(); // Tạo ID mới
            writer.write(newId + "," + paymentMethod.getMethodName() + "," + userId + "," + paymentMethod.getBalance());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    private String generatePaymentMethodId() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].startsWith("PM")) {
                    int id = Integer.parseInt(parts[0].substring(2));
                    maxId = Math.max(maxId, id);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file để tạo ID: " + e.getMessage());
        }
        return "PM" + String.format("%03d", maxId + 1);
    }



    private BigDecimal getUserInputAmount() {
        Scanner scanner = new Scanner(System.in);
        BigDecimal amount = null;

        while (amount == null) {
            System.out.print("Nhập số tiền cần thanh toán: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
                continue;
            }

            try {
                amount = new BigDecimal(input);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Số tiền phải lớn hơn 0.");
                    amount = null;
                }
            } catch (NumberFormatException e) {
                System.out.println("Định dạng không hợp lệ. Hãy nhập một số.");
            }
        }
        return amount;
    }

    private String selectPaymentMethod() {
        System.out.print("Chọn phương thức thanh toán (CreditCard, EWallet, BankTransfer): ");
        return scanner.next();
    }

    private boolean verifyOTP() {
        OTPService otpService = new OTPServiceImpl();
        return otpService.verifyOTP();
    }

    private boolean checkBalance(String userId, String method, BigDecimal amount) {
        try (BufferedReader br = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String methodId = parts[0].trim();
                    String storedMethod = parts[1].trim();
                    String storedUserId = parts[2].trim();
                    BigDecimal balance = new BigDecimal(parts[3].trim());

                    if (storedUserId.equals(userId) && storedMethod.equalsIgnoreCase(method)) {
                        return balance.compareTo(amount) >= 0;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi kiểm tra số dư: " + e.getMessage());
        }
        return false;
    }

    private void saveTransaction(String userId, String method, String transactionType, BigDecimal amount, String status) {
        TransactionService transactionService = new TransactionServiceImpl();
        transactionService.saveTransaction(userId, method, transactionType, amount, status);
    }

    private PaymentMethod getPaymentMethod(String method) {
        try (BufferedReader br = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String methodId = parts[0].trim();
                    String storedMethod = parts[1].trim();
                    String storedUserId = parts[2].trim();
                    BigDecimal balance = new BigDecimal(parts[3].trim());

                    if (storedMethod.equalsIgnoreCase(method)) {
                        switch (method) {
                            case "BankTransfer":
                                return new BankTransfer(methodId, storedMethod, storedUserId, balance);
                            case "CreditCard":
                                return new CreditCard(methodId, storedMethod, storedUserId, balance);
                            case "EWallet":
                                return new EWallet(methodId, storedMethod, storedUserId, balance);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi đọc phương thức thanh toán: " + e.getMessage());
        }
        return null;
    }

    private void handleInsufficientBalance(String userId, String method, BigDecimal amount) {
        System.out.print("Bạn có muốn chọn phương thức thanh toán khác không? (Y/N): ");
        scanner.nextLine();
        String choice = scanner.nextLine();

        if ("Y".equalsIgnoreCase(choice)) {
            System.out.println("Chọn phương thức thanh toán mới.");
            String newMethod = selectPaymentMethod();
            processPaymentWithMethod(userId, newMethod, amount);
        } else {
            System.out.print("Bạn có muốn chia nhỏ thanh toán không? (Y/N): ");
            String splitChoice = scanner.nextLine();
            if ("Y".equalsIgnoreCase(splitChoice)) {
                splitPayment(userId, method, amount);
            } else {
                System.out.println("Giao dịch thất bại.");
                saveTransaction(userId, method, TRANSACTION_TYPE, amount, "FAILED");
            }
        }
    }

    private void processPaymentWithMethod(String userId, String method, BigDecimal amount) {
        boolean isForeignTransaction = false; //xử lí cho ngưởi dùng chọn
        PaymentMethod paymentMethod = getPaymentMethod(method);

        if (paymentMethod != null) {
            boolean transactionSuccessful = paymentMethod.processPayment(amount, isForeignTransaction);

            if (transactionSuccessful) {
                System.out.println("Thanh toán thành công bằng phương thức " + method);
                saveTransaction(userId, method, TRANSACTION_TYPE, amount, "SUCCESS");
            } else {
                System.out.println("Lỗi khi xử lý thanh toán với phương thức " + method);
                saveTransaction(userId, method, TRANSACTION_TYPE, amount, "FAILED");
            }
        } else {
            System.out.println("Phương thức thanh toán không hợp lệ.");
        }
    }

    private void splitPayment(String userId, String method, BigDecimal amount) {
        BigDecimal firstPart = getBalance(userId, method);
        processPaymentWithMethod(userId, method, firstPart);

        BigDecimal secondPart = amount.subtract(firstPart);
        if (secondPart.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Thanh toán hoàn tất.");
            return;
        }

        System.out.println("Thanh toán phần còn lại: " + secondPart);
        String secondMethod = selectPaymentMethod();
        if (checkBalance(userId, method, secondPart)) {
            processPaymentWithMethod(userId, secondMethod, secondPart);
        } else {
            System.out.println("Số dư không đủ.");
            handleInsufficientBalance(userId, secondMethod, secondPart);
        }
    }

    private boolean isFraudulentTransaction(String userId, BigDecimal amount) {
        FraudDetectionService fraudDetectionService = new FraudDetectionServiceImpl();
        return fraudDetectionService.isFraudulentTransaction(userId, amount);
    }

    private BigDecimal getBalance(String userId, String method) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String methodId = data[0];
                String methodName = data[1];
                String fileUserId = data[2];
                BigDecimal fileBalance = new BigDecimal(data[3]);

                if (fileUserId.equals(userId) && methodName.equalsIgnoreCase(method)) {
                    return fileBalance;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    private void updateBalance(String userId, String method, BigDecimal amountPaid) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] data = line.split(",");

            String methodId = data[0];
            String methodName = data[1];
            String fileUserId = data[2];
            BigDecimal fileBalance = new BigDecimal(data[3]);

            if (fileUserId.equals(userId) && methodName.equalsIgnoreCase(method)) {
                BigDecimal updatedBalance = fileBalance.subtract(amountPaid);
                lines.set(i, methodId + "," + methodName + "," + fileUserId + "," + updatedBalance.toString());
                break;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_METHOD_FILE))) {
            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                if (i < lines.size() - 1) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

