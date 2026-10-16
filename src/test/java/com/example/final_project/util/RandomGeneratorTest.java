package com.example.final_project.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RandomGeneratorTest {

    @Test
    void generatesSixCharactersFromTheAllowedAlphabet() {
        for (int i = 0; i < 100; i++) {
            assertThat(RandomGenerator.generateRandomString()).matches("^[0-9a-zA-Z]{6}$");
        }
    }

    /**
     * The value is an organization's join code, so two organizations registered
     * moments apart must not receive related codes. With 62^6 possibilities, 500
     * draws from a sound generator collide with probability well under one in a
     * thousand; a clock-seeded {@code java.util.Random} recreated per call is the
     * failure this guards against.
     */
    @Test
    void doesNotRepeatAcrossRapidSuccessiveCalls() {
        Set<String> codes = new HashSet<>();
        for (int i = 0; i < 500; i++) {
            codes.add(RandomGenerator.generateRandomString());
        }

        assertThat(codes).hasSize(500);
    }
}
