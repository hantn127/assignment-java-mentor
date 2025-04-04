package service.impl;

import model.User;
import service.UserService;
import constance.UserStatus;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserServiceImpl implements UserService {
    private final Map<String, User> users = new HashMap<>();
    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 3;
    private static final String USER_FILE = "week3_oop_and_java_basic/payment_system/src/data/users.txt";
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public User login(String username, String password) {
        User user = getUserFromFile(username);

        if (user == null || !user.getPassword().equals(password)) {
            if (user != null) {
                user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
                handleFailedLogin(user);
                updateUserInFile(user);
            } else {
                System.out.println("Tài khoản không tồn tại.");
            }
            return null;
        }
        if (isUserSuspended(user)) {
            System.out.println("Tài khoản của bạn đã bị khóa. Liên hệ admin để mở khóa.");
            return null;
        }
        user.setFailedLoginAttempts(0);
        System.out.println("Đăng nhập thành công!");
        return user;
    }

    private User getUserFromFile(String username) {
        try (Stream<String> lines = Files.lines(Paths.get(USER_FILE))) {
            return lines.map(line -> line.split(","))
                    .filter(parts -> parts.length >= 7 && parts[1].equals(username))
                    .map(parts -> new User(
                            parts[0], parts[1], parts[2],
                            UserStatus.valueOf(parts[3]),
                            new BigDecimal(parts[4]),
                            Integer.parseInt(parts[5]),
                            Integer.parseInt(parts[6])
                    ))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateUserInFile(User updatedUser) {
        try {
            List<String> updatedLines = Files.lines(Paths.get(USER_FILE))
                    .map(line -> {
                        String[] parts = line.split(",");
                        if (parts[0].equals(updatedUser.getUserId())) {
                            return String.format("%s,%s,%s,%s,%s,%d,%d",
                                    updatedUser.getUserId(), updatedUser.getUsername(),
                                    updatedUser.getPassword(), updatedUser.getStatus(),
                                    updatedUser.getBalance(), updatedUser.getFailedLoginAttempts(),
                                    updatedUser.getFailedOtpAttempts());
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(Paths.get(USER_FILE), updatedLines);
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi file: " + e.getMessage());
        }
    }

    private User getUserById(String userId) {
        try (Stream<String> lines = Files.lines(Paths.get(USER_FILE))) {
            return lines.map(line -> line.split(","))
                    .filter(parts -> parts.length >= 7 && parts[0].equals(userId))
                    .map(parts -> new User(
                            parts[0], parts[1], parts[2],
                            UserStatus.valueOf(parts[3]),
                            new BigDecimal(parts[4]),
                            Integer.parseInt(parts[5]),
                            Integer.parseInt(parts[6])
                    ))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void logout() {
        System.out.println("Đã đăng xuất thành công!\n");
    }

    @Override
    public boolean changePassword(String userId) {
        String oldPassword = getOldPassword();
        String newPassword = getNewPassword();

        User user = getUserById(userId);

        if (user != null && user.getPassword().equals(oldPassword)) {
            if (newPassword.length() >= 6) {
                user.setPassword(newPassword);
                updateUserInFile(user);
                System.out.println("Mật khẩu đã được thay đổi thành công.");
                return true;
            } else {
                System.out.println("Mật khẩu mới phải có ít nhất 6 ký tự.");
            }
        } else {
            System.out.println("Mật khẩu cũ không chính xác.");
        }
        return false;
    }

    private String getOldPassword() {
        System.out.print("Nhập mật khẩu cũ: ");
        return scanner.next();
    }

    private String getNewPassword() {
        System.out.print("Nhập mật khẩu mới: ");
        return scanner.next();
    }

    @Override
    public void viewSecurityInfo(String userId) {
        System.out.println("Thông tin bảo mật của tài khoản:");
        System.out.println("1. Xác thực hai yếu tố (2FA): Đã bật");
        System.out.println("2. Lịch sử đăng nhập:");
        System.out.println("- Đăng nhập từ địa chỉ IP 192.168.1.1 vào lúc 2025-03-06");
    }

    private void handleFailedLogin(User user) {
        if (user.getFailedLoginAttempts() >= MAX_FAILED_LOGIN_ATTEMPTS) {
            user.setStatus(UserStatus.SUSPENDED);
            System.out.println("Tài khoản đã bị khóa do nhập sai quá 3 lần.");
        } else {
            System.out.println("Sai tên đăng nhập hoặc mật khẩu. Vui lòng thử lại.");
        }
    }

    private boolean isUserSuspended(User user) {
        if (user.getStatus() == UserStatus.SUSPENDED) {
            return true;
        }
        return false;
    }

}
