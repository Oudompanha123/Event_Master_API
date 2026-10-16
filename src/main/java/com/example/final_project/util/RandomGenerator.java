package com.example.final_project.util;

import java.security.SecureRandom;

public class RandomGenerator {

    // Define the characters allowed in the random string
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * One shared, cryptographically secure generator.
     *
     * <p>Created once rather than per call: a fresh {@link java.util.Random} is
     * seeded from the clock, so codes issued close together are correlated.
     * {@link SecureRandom} is used rather than {@code Random} because the value
     * this class produces is an organization's join code - anyone holding it can
     * register into that organization through the public
     * {@code /api/auth/user-register} endpoint - so one code must not be
     * predictable from another.
     */
    private static final SecureRandom RANDOM = new SecureRandom();

    // Method to generate a random alphanumeric string of 6 characters
    public static String generateRandomString() {
        // Create a StringBuilder to store the generated string
        StringBuilder sb = new StringBuilder(6);

        // Generate each character of the random string
        for (int i = 0; i < 6; i++) {
            // Get a random index from 0 to the length of the characters string
            int index = RANDOM.nextInt(CHARACTERS.length());
            // Append the character at the random index to the StringBuilder
            sb.append(CHARACTERS.charAt(index));
        }

        // Convert StringBuilder to String and return
        return sb.toString();
    }
}
