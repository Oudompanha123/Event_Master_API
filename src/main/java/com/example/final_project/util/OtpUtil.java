package com.example.final_project.util;


import org.springframework.stereotype.Component;


@Component
public class OtpUtil {

    private  static final int OTP_LENGTH = 4;

    public static String generateOtp() {
        int min = Integer.parseInt("1" + ("0".repeat(OTP_LENGTH - 1)));
        int max = Integer.parseInt("9".repeat(OTP_LENGTH));
        int randomNumber =  (int)Math.floor(Math.random() * (max - min + 1) + min);
        return String.valueOf(randomNumber);
    }
}


