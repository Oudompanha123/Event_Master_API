package com.example.final_project.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OtpUtilTest {

    /**
     * The OTP is what stands between a stranger and a verified account, and the
     * column it is stored in is {@code varchar(4)} - a code of any other length
     * would be silently truncated or rejected by the database.
     */
    @Test
    void generatesAFourDigitCode() {
        for (int i = 0; i < 200; i++) {
            String otp = OtpUtil.generateOtp();

            assertThat(otp).hasSize(4).containsOnlyDigits();
            assertThat(Integer.parseInt(otp)).isBetween(1000, 9999);
        }
    }
}
