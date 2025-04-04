package service.impl;

import model.*;
import service.FraudDetectionService;
import service.OTPService;
import service.PaymentService;
import service.TransactionService;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaymentServiceImpl implements PaymentService {
    private static final String PAYMENT_METHOD_FILE = "week3_oop_and_java_basic/payment_system/src/data/payment_methods.txt";
    private static final String TRANSACTION_FILE = "week3_oop_and_java_basic/payment_system/src/data/transactions.txt";
    private static final String TRANSACTION_TYPE = "Payment";
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void processPayment(String userId) {
        BigDecimal amount = getUserInputAmount();

        if (!checkTotalBalance(userId, amount)) {
            System.out.println("Tổng số dư trong tất cả phương thức thanh toán không đủ để thực hiện giao dịch.");
            return;
        }

        String method = selectPaymentMethod();

        if (isFraudulentTransaction(userId, amount)) {
            System.out.println("Giao dịch có dấu hiệu gian lận. Giao dịch này đã bị chặn, yêu cầu xác minh qua call center.");
            saveTransaction(userId, method, TRANSACTION_TYPE, amount, "SUSPICIOUS");
            return;
        }

        if (!checkBalance(userId, method, amount)) {
            if (!handleInsufficientBalance(userId, method, amount)) {
            }
        } else {
            if (amount.compareTo(BigDecimal.valueOf(5000)) > 0) {
                System.out.println("Số tiền lớn hơn 5000$, cần xác minh OTP.");
                if (!verifyOTP()) {
                    System.out.println("Xác minh OTP thất bại, hủy giao dịch.");
                    return;
                }
                processPaymentWithMethod(userId, method, amount);
            }
        }
    }

    private String getPaymentMethod() {
        System.out.print("Nhập phương thức thanh toán bạn muốn thêm mới hoặc xóa: ");
        return scanner.nextLine().trim();
    }

    private BigDecimal getBalanceInput() {
        System.out.print("Nhập số dư cho phương thức thanh toán: ");
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Số dư không hợp lệ. Vui lòng nhập lại.");
            scanner.next();
        }
        return scanner.nextBigDecimal();
    }

    @Override
    public boolean addPaymentMethod(String userId) {
        String paymentMethod = getPaymentMethod();

        if (isPaymentMethodExist(userId, paymentMethod)) {
            System.out.println("Phương thức thanh toán đã tồn tại.");
            return false;
        }

        BigDecimal balance = getBalanceInput();

        savePaymentMethod(userId, paymentMethod, balance);
        System.out.println("Phương thức thanh toán đã được thêm thành công.");
        return true;
    }

    @Override
    public boolean removePaymentMethod(String userId) {
        String paymentMethod = getPaymentMethod();

        if (isPendingTransaction(userId, paymentMethod)) {
            System.out.println("Không thể xóa phương thức thanh toán, có giao dịch đang chờ xử lý.");
            return false;
        }
        if (isPaymentMethodExist(userId, paymentMethod)) {
            deletePaymentMethod(userId, paymentMethod);
        } else {
            System.out.println("Phương thức thanh toán này không tồn tại!");
        }
        return true;
    }

    private boolean isPendingTransaction(String userId, String paymentMethod) {
        try {
            return Files.lines(Paths.get(TRANSACTION_FILE))
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 7)
                    .anyMatch(parts -> parts[1].equals(userId) && parts[2].equals(paymentMethod) && parts[6].equals("FAILED"));
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file giao dịch: " + e.getMessage());
            return false;
        }
    }

    private void deletePaymentMethod(String userId, String paymentMethod) {
        File inputFile = new File(PAYMENT_METHOD_FILE);
        File tempFile = new File(inputFile.getAbsolutePath() + "_temp");

        try {
            List<String> lines = Files.readAllLines(inputFile.toPath());
            List<String> updatedLines = lines.stream()
                    .filter(line -> {
                        String[] parts = line.split(",");
                        return parts.length < 4 || !(parts[2].equals(userId) && parts[1].equals(paymentMethod));
                    })
                    .collect(Collectors.toList());

            Files.write(tempFile.toPath(), updatedLines);
            if (!inputFile.delete()) {
                System.out.println("Lỗi khi xóa file cũ.");
            } else if (!tempFile.renameTo(inputFile)) {
                System.out.println("Lỗi khi đổi tên file mới.");
            } else {
                System.out.println("Phương thức thanh toán đã được xóa thành công.");
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi cập nhật file: " + e.getMessage());
        }
    }

    private boolean isPaymentMethodExist(String userId, String paymentMethod) {
        try {
            return Files.lines(Paths.get(PAYMENT_METHOD_FILE))
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 4)
                    .anyMatch(parts -> parts[2].equals(userId) && parts[1].equals(paymentMethod));
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
            return false;
        }
    }

    private void savePaymentMethod(String userId, String paymentMethod, BigDecimal balance) {
        try {
            String newId = generatePaymentMethodId();
            String newLine = newId + "," + paymentMethod + "," + userId + "," + balance;
            Files.write(Paths.get(PAYMENT_METHOD_FILE), (newLine + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    private String generatePaymentMethodId() {
        try {
            return Files.lines(Paths.get(PAYMENT_METHOD_FILE))
                    .map(line -> line.split(","))
                    .filter(parts -> parts.length >= 1 && parts[0].startsWith("PM"))
                    .map(parts -> Integer.parseInt(parts[0].substring(2)))
                    .max(Integer::compare)
                    .map(maxId -> "PM" + String.format("%03d", maxId + 1))
                    .orElse("PM001");
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file để tạo ID: " + e.getMessage());
            return "PM001";
        }
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
        return scanner.nextLine().trim();
    }

    private boolean verifyOTP() {
        OTPService otpService = new OTPServiceImpl();
        return otpService.verifyOTP();
    }

    private boolean checkBalance(String userId, String method, BigDecimal amount) {
        try (Stream<String> lines = Files.lines(Paths.get(PAYMENT_METHOD_FILE))) {
            return lines.map(line -> line.split(","))
                    .filter(parts -> parts.length == 4)
                    .filter(parts -> parts[2].trim().equals(userId) && parts[1].trim().equalsIgnoreCase(method))
                    .map(parts -> new BigDecimal(parts[3].trim()))
                    .anyMatch(balance -> balance.compareTo(amount) >= 0);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi kiểm tra số dư: " + e.getMessage());
        }
        return false;
    }

    private void saveTransaction(String userId, String method, String transactionType, BigDecimal amount, String status) {
        TransactionService transactionService = new TransactionServiceImpl();
        transactionService.saveTransaction(userId, method, transactionType, amount, status);
    }

    private void saveTransactions(List<String> transactionLogs) {
        TransactionService transactionService = new TransactionServiceImpl();
        transactionService.saveTransactions(transactionLogs);
    }

    private PaymentMethod getPaymentMethod(String method) {
        try (Stream<String> lines = Files.lines(Paths.get(PAYMENT_METHOD_FILE))) {
            return lines.map(line -> line.split(","))
                    .filter(parts -> parts.length == 4)
                    .filter(parts -> parts[1].trim().equalsIgnoreCase(method))
                    .map(parts -> {
                        String methodId = parts[0].trim();
                        String storedUserId = parts[2].trim();
                        BigDecimal balance = new BigDecimal(parts[3].trim());
                        return switch (method) {
                            case "BankTransfer" -> new BankTransfer(methodId, method, storedUserId, balance);
                            case "CreditCard" -> new CreditCard(methodId, method, storedUserId, balance);
                            case "EWallet" -> new EWallet(methodId, method, storedUserId, balance);
                            default -> null;
                        };
                    })
                    .findFirst()
                    .orElse(null);
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi đọc phương thức thanh toán: " + e.getMessage());
        }
        return null;
    }

    private boolean handleInsufficientBalance(String userId, String method, BigDecimal amount) {
        System.out.print("Số dư không đủ trong phương thức này. Bạn có muốn chia nhỏ thanh toán không? (Y/N): ");
        String choice = scanner.nextLine().trim();

        if ("Y".equalsIgnoreCase(choice)) {
            if (amount.compareTo(BigDecimal.valueOf(5000)) > 0) {
                System.out.println("Số tiền lớn hơn 5000$, cần xác minh OTP trước khi chia nhỏ.");
                if (!verifyOTP()) {
                    System.out.println("Xác minh OTP thất bại, hủy giao dịch.");
                    return false;
                }
            }
            splitPayment(userId, method, amount);
            return true;
        } else {
            System.out.println("Giao dịch thất bại.");
            saveTransaction(userId, method, TRANSACTION_TYPE, amount, "FAILED");
            return false;
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

    private boolean processPaymentWithMethod(String userId, String method, BigDecimal amount, List<String> transactionLogs) {
        boolean isForeignTransaction = false;
        PaymentMethod paymentMethod = getPaymentMethod(method);

        if (paymentMethod != null) {
            BigDecimal oldBalance = paymentMethod.getBalance();
            boolean transactionSuccessful = paymentMethod.processPayment(amount, isForeignTransaction);

            if (transactionSuccessful) {
                System.out.println("Thanh toán thành công bằng phương thức " + method);
                transactionLogs.add(String.format("%s,%s,%s,%.2f,%s,SUCCESS",
                        userId, method, TRANSACTION_TYPE, amount, LocalDate.now()));
                return true;
            } else {
                paymentMethod.setBalance(oldBalance);
                System.out.println("Lỗi khi xử lý thanh toán với phương thức " + method + ". Hoàn lại số dư.");
                return false;
            }
        } else {
            System.out.println("Phương thức thanh toán không hợp lệ.");
            return false;
        }
    }

    private void splitPayment(String userId, String method, BigDecimal amount) {
        List<String> transactionLogs = new ArrayList<>();
        List<PaymentMethod> usedMethods = new ArrayList<>();
        Map<String, BigDecimal> deductedAmounts = new HashMap<>();
        boolean rollbackOccurred = false;

        BigDecimal firstPart = getBalanceInAccount(userId, method);
        if (processPaymentWithMethod(userId, method, firstPart, transactionLogs)) {
            usedMethods.add(getPaymentMethod(method));
        }

        BigDecimal remainingAmount = amount.subtract(firstPart);
        if (remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
            System.out.println("Thanh toán hoàn tất.");
            saveTransactions(transactionLogs);
            return;
        }

        while (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("Số tiền còn lại cần thanh toán: " + remainingAmount);

            String secondMethod = null;
            boolean methodUsed = false;

            while (!methodUsed) {
                secondMethod = selectPaymentMethod();
                String finalSecondMethod = secondMethod;
                methodUsed = usedMethods.stream().noneMatch(m -> m.getMethodName().equals(finalSecondMethod));

                if (!methodUsed) {
                    System.out.println("Phương thức " + secondMethod + " đã được sử dụng. Vui lòng chọn phương thức khác.");
                }
            }

            BigDecimal secondPart = getBalanceInAccount(userId, secondMethod);

            if (processPaymentWithMethod(userId, secondMethod, remainingAmount, transactionLogs)) {
                usedMethods.add(getPaymentMethod(secondMethod));
                remainingAmount = remainingAmount.subtract(secondPart);
            } else {
                rollbackPayments(usedMethods, transactionLogs);
                rollbackOccurred = true;
            }
            if (rollbackOccurred) {
                System.out.print("Có lỗi xảy ra, bạn có muốn tiếp tục thanh toán không hay dừng lại? (Nếu dừng giao dịch tại đây, các giao dịch trước đó của bạn sẽ không được lưu) (Y/N): ");
                String choice = scanner.nextLine().trim();
                if ("N".equalsIgnoreCase(choice)) {
                    System.out.println("Giao dịch bị hủy, các khoản đã thanh toán sẽ không được lưu.");
                    return;
                }
                rollbackOccurred = false;
            }
        }

        System.out.println("Thanh toán hoàn tất.");
        saveTransactions(transactionLogs);
    }

    private void updatePaymentMethodsFile(List<PaymentMethod> updatedMethods) {
        List<PaymentMethod> allMethods = new ArrayList<>();

        // Đọc toàn bộ file payment_methods.csv
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_METHOD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String methodId = parts[0];
                    String methodName = parts[1];
                    String userId = parts[2];
                    BigDecimal balance = new BigDecimal(parts[3]);

                    PaymentMethod method;
                    switch (methodName) {
                        case "CreditCard":
                            method = new CreditCard(methodId, methodName, userId, balance);
                            break;
                        case "EWallet":
                            method = new EWallet(methodId, methodName, userId, balance);
                            break;
                        case "BankTransfer":
                            method = new BankTransfer(methodId, methodName, userId, balance);
                            break;
                        default:
                            System.out.println("Không xác định phương thức: " + methodName);
                            continue;
                    }

                    // Kiểm tra nếu phương thức này cần cập nhật
                    for (PaymentMethod updatedMethod : updatedMethods) {
                        if (updatedMethod.getMethodId().equals(methodId)) {
                            method.setBalance(updatedMethod.getBalance()); // Cập nhật số dư
                            break;
                        }
                    }
                    allMethods.add(method);
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file payment_methods.csv: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_METHOD_FILE))) {
            for (PaymentMethod method : allMethods) {
                writer.write(String.format("%s,%s,%s,%.2f%n",
                        method.getMethodId(),
                        method.getMethodName(),
                        method.getUserId(),
                        method.getBalance()));
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file payment_methods.csv: " + e.getMessage());
        }
    }

    private boolean hasAlternativePaymentMethod(String userId, BigDecimal remainingAmount, List<PaymentMethod> usedMethods) {
        List<PaymentMethod> availableMethods = getAvailablePaymentMethods(userId);

        for (PaymentMethod method : availableMethods) {
            if (!usedMethods.contains(method) && method.getBalance().compareTo(remainingAmount) >= 0) {
                return true;
            }
        }
        return false;
    }


    private void rollbackPayments(List<PaymentMethod> usedMethods, List<String> transactionLogs) {
        for (PaymentMethod method : usedMethods) {
            method.rollbackLastTransaction();
        }
        transactionLogs.clear();
        System.out.println("Tất cả giao dịch đã được rollback.");
    }

    private List<PaymentMethod> getAvailablePaymentMethods(String userId) {
        List<PaymentMethod> allMethods = getAllPaymentMethodsForUser(userId);
        List<PaymentMethod> availableMethods = new ArrayList<>();

        for (PaymentMethod method : allMethods) {
            if (method.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                availableMethods.add(method);
            }
        }

        return availableMethods;
    }

    private List<PaymentMethod> getAllPaymentMethodsForUser(String userId) {
        List<PaymentMethod> userMethods = new ArrayList<>();
        String filePath = PAYMENT_METHOD_FILE;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String methodId = parts[0];
                String methodName = parts[1];
                String ownerId = parts[2];
                BigDecimal balance = new BigDecimal(parts[3]);

                if (ownerId.equals(userId)) {
                    PaymentMethod method = null;
                    switch (methodName) {
                        case "CreditCard":
                            method = new CreditCard(methodId, methodName, ownerId, balance);
                            break;
                        case "EWallet":
                            method = new EWallet(methodId, methodName, ownerId, balance);
                            break;
                        case "BankTransfer":
                            method = new BankTransfer(methodId, methodName, ownerId, balance);
                            break;
                    }
                    if (method != null) {
                        userMethods.add(method);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userMethods;
    }

    private boolean isFraudulentTransaction(String userId, BigDecimal amount) {
        FraudDetectionService fraudDetectionService = new FraudDetectionServiceImpl();
        return fraudDetectionService.isFraudulentTransaction(userId, amount);
    }

    private BigDecimal getBalanceInAccount(String userId, String method) {
        try (Stream<String> lines = Files.lines(Paths.get(PAYMENT_METHOD_FILE))) {
            return lines.map(line -> line.split(","))
                    .filter(parts -> parts.length == 4)
                    .filter(parts -> parts[2].trim().equals(userId) && parts[1].trim().equalsIgnoreCase(method))
                    .map(parts -> new BigDecimal(parts[3].trim()))
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
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
                lines.set(i, methodId + "," + methodName + "," + fileUserId + "," + updatedBalance);
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

    private boolean checkTotalBalance(String userId, BigDecimal amount) {
        try (Stream<String> lines = Files.lines(Paths.get(PAYMENT_METHOD_FILE))) {
            BigDecimal totalBalance = lines.map(line -> line.split(","))
                    .filter(parts -> parts.length == 4 && parts[2].trim().equals(userId))
                    .map(parts -> new BigDecimal(parts[3].trim()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return totalBalance.compareTo(amount) >= 0;
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi kiểm tra tổng số dư: " + e.getMessage());
        }
        return false;
    }

}

