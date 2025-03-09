package main;

import model.User;
import service.PaymentService;
import service.RefundService;
import service.TransactionService;
import service.impl.PaymentServiceImpl;
import service.UserService;
import service.impl.RefundServiceImpl;
import service.impl.TransactionServiceImpl;
import service.impl.UserServiceImpl;

import java.util.Scanner;

public class PaymentApplication {
    private static final Scanner sc = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl();
    private static final PaymentService paymentService = new PaymentServiceImpl();
    private static final RefundService refundService = new RefundServiceImpl();
    private static final TransactionService transactionService = new TransactionServiceImpl();

    public static void main(String[] args) {
        User currentUser = null;

        while (true) {
            while (currentUser == null) {
                System.out.print("Nhập username: ");
                String username = sc.nextLine();
                System.out.print("Nhập password: ");
                String password = sc.nextLine();

                currentUser = userService.login(username, password);
                if (currentUser == null) {
                    System.out.println("Vui lòng thử lại!\n");
                }
            }
            showMainMenu(currentUser.getUserId(), currentUser.getUsername());
            currentUser = null;
        }
    }

    private static void showMainMenu(String userId, String username) {
        System.out.println("\nChào mừng " + username + " đã vào hệ thống!");
        boolean running = true;
        while (running) {
            System.out.println("\n========= MENU =========");
            System.out.println("1️⃣ Thực hiện thanh toán");
            System.out.println("2️⃣ Yêu cầu hoàn tiền");
            System.out.println("3️⃣ Báo cáo tài chính");
            System.out.println("4️⃣ Cài đặt tài khoản");
            System.out.println("5️⃣ Đăng xuất");
            System.out.println("==========================");

            System.out.print("Chọn chức năng: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    paymentService.processPayment(userId);
                    break;
                case "2":
                    refundService.processRefund(userId);
                    break;
                case "3":
                    transactionService.displayFinancialReport(userId);
                    break;
                case "4":
                    showAccountSettingsMenu(userId);
                    break;
                case "5":
                    confirmLogout();
                    running = false;
                    break;
                default:
                    System.out.println("❌ Lựa chọn không hợp lệ, vui lòng thử lại!");
                    break;
            }
        }
    }

    private static void confirmLogout() {
        System.out.print("Bạn có chắc chắn muốn đăng xuất không? (y/n): ");
        String confirmation = sc.nextLine().toLowerCase();
        if (confirmation.equals("y")) {
            userService.logout();
        }
    }

    private static void showAccountSettingsMenu(String userId) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== CÀI ĐẶT TÀI KHOẢN =====");
            System.out.println("1. Thay đổi mật khẩu");
            System.out.println("2. Thêm phương thức thanh toán mới");
            System.out.println("3. Xóa phương thức thanh toán");
            System.out.println("4. Xem thông tin bảo mật tài khoản");
            System.out.println("5. Quay lại menu chính");
            System.out.print("Chọn một tùy chọn: ");

            choice = sc.nextInt();
            sc.nextLine(); // Xử lý dòng new line

            switch (choice) {
                case 1:
                    userService.changePassword(userId);
                    break;
                case 2:
                    paymentService.addPaymentMethod(userId);
                    break;
                case 3:
                    paymentService.removePaymentMethod(userId);
                    break;
                case 4:
                    userService.viewSecurityInfo(userId);
                    break;
                case 5:
                    System.out.println("Quay lại menu chính...\n");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
            }
        } while (choice != 5);
    }
}