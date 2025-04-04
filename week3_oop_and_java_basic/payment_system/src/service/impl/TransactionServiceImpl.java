package service.impl;

import model.Transaction;
import service.TransactionService;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {
    private static final String TRANSACTION_FILE = "week3_oop_and_java_basic/payment_system/src/data/transactions.txt";
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void saveTransaction(String userId, String method, String transactionType, BigDecimal amount, String status) {
        File file = new File(TRANSACTION_FILE);

        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            }

            int newTransactionId = 1;
            if (!lines.isEmpty()) {
                String lastLine = lines.get(lines.size() - 1);
                if (lastLine.startsWith("T")) {
                    String lastTransactionNumber = lastLine.split(",")[0].substring(1);
                    newTransactionId = Integer.parseInt(lastTransactionNumber) + 1;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < lines.size(); i++) {
                    writer.write(lines.get(i));
                    if (i < lines.size() - 1) {
                        writer.newLine();
                    }
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                if (!lines.isEmpty()) {
                    writer.newLine();
                }
                writer.write(String.format("T%03d,%s,%s,%s,%.2f,%s,%s",
                        newTransactionId, userId, method, transactionType, amount, LocalDate.now(), status));
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveTransactions(List<String> transactionLogs) {
        if (transactionLogs.isEmpty()) return;

        File file = new File(TRANSACTION_FILE);
        List<String> existingLines = new ArrayList<>();
        int newTransactionId = 1;

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    existingLines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (!existingLines.isEmpty()) {
                String lastLine = existingLines.get(existingLines.size() - 1);
                if (lastLine.startsWith("T")) {
                    String lastTransactionNumber = lastLine.split(",")[0].substring(1);
                    try {
                        newTransactionId = Integer.parseInt(lastTransactionNumber) + 1;
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi phân tích ID giao dịch. Đặt về 1.");
                    }
                }
            }
        }

        // Ghi lại dữ liệu vào file mà không tạo dòng trống cuối
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (int i = 0; i < existingLines.size(); i++) {
                writer.write(existingLines.get(i));
                if (i < existingLines.size() - 1 || !transactionLogs.isEmpty()) {
                    writer.newLine();
                }
            }

            for (int i = 0; i < transactionLogs.size(); i++) {
                String newTransaction = String.format("T%03d,%s", newTransactionId++, transactionLogs.get(i));
                writer.write(newTransaction);
                if (i < transactionLogs.size() - 1) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactions(String userId, String status) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .filter(transactionData -> transactionData[1].equals(userId) && transactionData[6].equals(status)) // Filter by userId and status
                    .map(transactionData -> {
                        try {
                            Date transactionDate = dateFormat.parse(transactionData[5]);
                            BigDecimal amount = new BigDecimal(transactionData[4]);
                            return new Transaction(transactionData[0], transactionData[1], transactionData[2],
                                    transactionData[3], amount, transactionDate, transactionData[6]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(t -> {
                        long diffInMillies = Math.abs(currentDate.getTime() - t.getDate().getTime());
                        long diffDays = diffInMillies / (1000 * 60 * 60 * 24);
                        return diffDays <= 7;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<Transaction> getTransactionsByUserAndStatus(String userId, String status) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .filter(transactionData -> transactionData[1].equals(userId) && transactionData[6].equals(status))
                    .map(transactionData -> {
                        try {
                            Date transactionDate = dateFormat.parse(transactionData[5]);
                            BigDecimal amount = new BigDecimal(transactionData[4]);
                            return new Transaction(transactionData[0], transactionData[1], transactionData[2],
                                    transactionData[3], amount, transactionDate, transactionData[6]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private List<Transaction> getTransactionsByUserId(String userId) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .filter(transactionData -> transactionData[1].equals(userId))
                    .map(transactionData -> {
                        try {
                            Date transactionDate = dateFormat.parse(transactionData[5]);
                            BigDecimal amount = new BigDecimal(transactionData[4]);
                            return new Transaction(transactionData[0], transactionData[1], transactionData[2],
                                    transactionData[3], amount, transactionDate, transactionData[6]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void displayFinancialReport(String userId) {
        boolean isBusinessAccount = true;

        displayTotalAmountByPaymentMethod(userId, "SUCCESS");

        displayTransactionStatusCount(userId);

        displaySuspiciousTransactions(userId, "SUSPICIOUS");

        if (isBusinessAccount) {
            displayMonthlyReport(userId, "SUCCESS");
        }

        System.out.println("Press any key to return to the main menu...");
        scanner.nextLine();
    }

    private void displayTotalAmountByPaymentMethod(String userId, String status) {
        List<Transaction> transactions = getTransactionsByUserAndStatus(userId, status);

        Map<String, BigDecimal> totalAmountByMethod = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getPaymentMethod,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        System.out.println("\n🔹 Total Amount by Payment Method:");
        totalAmountByMethod.forEach((method, amount) -> System.out.printf(" - %s: %.2f\n", method, amount));
    }

    private void displayTransactionStatusCount(String userId) {
        List<Transaction> transactions = getTransactionsByUserId(userId);

        long successfulTransactions = transactions.stream().filter(t -> "SUCCESS".equals(t.getStatus())).count();
        long failedTransactions = transactions.stream().filter(t -> "FAILED".equals(t.getStatus())).count();

        System.out.println("\n🔹 Transaction Status Summary:");
        System.out.println(" - Successful Transactions: " + successfulTransactions);
        System.out.println(" - Failed Transactions: " + failedTransactions);
    }

    private void displaySuspiciousTransactions(String userId, String status) {
        List<Transaction> transactions = getTransactionsByUserAndStatus(userId, status);

        List<Transaction> suspiciousTransactions = transactions.stream()
                .filter(t -> "SUSPICIOUS".equals(t.getStatus()))
                .collect(Collectors.toList());

        if (suspiciousTransactions.isEmpty()) {
            System.out.println("\n✅ No suspicious transactions found.");
            return;
        }

        System.out.println("\n⚠️ Suspicious Transactions:");
        suspiciousTransactions.forEach(t -> System.out.println(" - " + t));
    }

    private void displayMonthlyReport(String userId, String status) {
        List<Transaction> transactions = getTransactions(userId, status);

        Map<String, BigDecimal> totalAmountByMonth = transactions.stream()
                .collect(Collectors.groupingBy(t -> new SimpleDateFormat("yyyy-MM").format(t.getDate()),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        System.out.println("\n📅 Monthly Financial Report:");
        totalAmountByMonth.forEach((month, amount) -> System.out.printf(" - %s: %.2f\n", month, amount));
    }

}
