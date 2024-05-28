package com.example.final_project.util;

import java.util.Random;

public class RandomGenerator {

    // Method to generate a random alphanumeric string of 6 characters
    public static String generateRandomString() {
        // Define the characters allowed in the random string
        String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        // Create a StringBuilder to store the generated string
        StringBuilder sb = new StringBuilder(6);
        // Create a Random object
        Random random = new Random();

        // Generate each character of the random string
        for (int i = 0; i < 6; i++) {
            // Get a random index from 0 to the length of the characters string
            int index = random.nextInt(characters.length());
            // Append the character at the random index to the StringBuilder
            sb.append(characters.charAt(index));
        }

        // Convert StringBuilder to String and return
        return sb.toString();
    }
}

