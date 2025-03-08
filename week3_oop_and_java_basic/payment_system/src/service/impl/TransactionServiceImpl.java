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

    public List<Transaction> getTransactions(String userId, String status) {
        List<Transaction> refundableTransactions = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionData = line.split(",");
                String transactionId = transactionData[0];
                String user = transactionData[1];
                String paymentMethod = transactionData[2];
                String transactionType = transactionData[3];
                BigDecimal amount = new BigDecimal(transactionData[4]);
                Date transactionDate = dateFormat.parse(transactionData[5]);
                String fileStatus = transactionData[6];

                if (user.equals(userId) && fileStatus.equals(status)) {
                    long diffInMillies = Math.abs(currentDate.getTime() - transactionDate.getTime());
                    long diffDays = diffInMillies / (1000 * 60 * 60 * 24);
                    if (diffDays <= 7) {
                        refundableTransactions.add(new Transaction(transactionId, user, paymentMethod, transactionType, amount, transactionDate, fileStatus));
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return refundableTransactions;
    }

    private List<Transaction> getTransactionsByUserId(String userId) {
        List<Transaction> transactions = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] transactionData = line.split(",");
                String transactionId = transactionData[0];
                String user = transactionData[1];
                String paymentMethod = transactionData[2];
                String transactionType = transactionData[3];
                BigDecimal amount = new BigDecimal(transactionData[4]);
                Date transactionDate = dateFormat.parse(transactionData[5]);
                String fileStatus = transactionData[6];

                if (user.equals(userId)) { // Chỉ lọc theo userId, không lọc theo status
                    transactions.add(new Transaction(transactionId, user, paymentMethod, transactionType, amount, transactionDate, fileStatus));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return transactions;
    }


    public void displayFinancialReport(String userId) {
        boolean isBusinessAccount = true;

//        List<Transaction> transactions = getTransactions(userId);
//        if (transactions.isEmpty()) {
//            System.out.println("No transactions available to display.");
//            return;
//        }

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
        List<Transaction> transactions = getTransactions(userId, status);

        Map<String, BigDecimal> totalAmountByMethod = new HashMap<>();
        for (Transaction t : transactions) {
            totalAmountByMethod.put(
                    t.getPaymentMethod(),
                    totalAmountByMethod.getOrDefault(t.getPaymentMethod(), BigDecimal.ZERO).add(t.getAmount())
            );
        }
        System.out.println("\n🔹 Total Amount by Payment Method:");
        for (Map.Entry<String, BigDecimal> entry : totalAmountByMethod.entrySet()) {
            System.out.printf(" - %s: %.2f\n", entry.getKey(), entry.getValue());
        }
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
        List<Transaction> transactions = getTransactions(userId, status);

        List<Transaction> suspiciousTransactions = transactions.stream()
                .filter(t -> "SUSPICIOUS".equals(t.getStatus()))
                .collect(Collectors.toList());

        if (suspiciousTransactions.isEmpty()) {
            System.out.println("\n✅ No suspicious transactions found.");
            return;
        }

        System.out.println("\n⚠️ Suspicious Transactions:");
        for (Transaction t : suspiciousTransactions) {
            System.out.println(" - " + t);
        }
    }

    private void displayMonthlyReport(String userId, String status) {
        List<Transaction> transactions = getTransactions(userId, status);

        Map<String, BigDecimal> totalAmountByMonth = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

        for (Transaction t : transactions) {
            String monthYear = monthFormat.format(t.getDate()); // Format Date to "YYYY-MM"
            totalAmountByMonth.put(
                    monthYear,
                    totalAmountByMonth.getOrDefault(monthYear, BigDecimal.ZERO).add(t.getAmount())
            );
        }

        System.out.println("\n📅 Monthly Financial Report:");
        for (Map.Entry<String, BigDecimal> entry : totalAmountByMonth.entrySet()) {
            System.out.printf(" - %s: %.2f\n", entry.getKey(), entry.getValue());
        }
    }

}
