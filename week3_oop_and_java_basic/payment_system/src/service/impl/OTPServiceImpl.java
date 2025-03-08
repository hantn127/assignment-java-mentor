package service.impl;

import service.OTPService;

import java.util.Random;
import java.util.Scanner;

public class OTPServiceImpl implements OTPService {
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean verifyOTP() {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);
        System.out.println("OTP của bạn là: " + otp);

        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Nhập OTP: ");
            int inputOtp = scanner.nextInt();
            if (inputOtp == otp) {
                System.out.println("Xác minh OTP thành công.");
                return true;
            }
            System.out.println("OTP không đúng, thử lại.");
            attempts++;
        }
        System.out.println("Bạn đã thử quá 3 lần, giao dịch bị hủy.");
        return false;
    }
}

