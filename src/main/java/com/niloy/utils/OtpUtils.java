package com.niloy.utils;

import java.util.Random;

public class OtpUtils {


    public static String generateOtp() {
        int otpLength = 6;

        Random random = new Random();
        StringBuilder otp = new StringBuilder(otpLength);

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10)); // Generate a random digit between 0 and 9
        }

        return otp.toString();
    }
}
