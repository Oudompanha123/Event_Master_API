package com.example.final_project.util;

import com.example.final_project.exception.BadRequestException;

public class Validation {
    public static void validatePhoneNumber(String phoneNumber){
        if(!phoneNumber.startsWith("0"))
            throw new BadRequestException("Phone number must start with number 0");
        else if (phoneNumber.length() < 9)
            throw new BadRequestException("length of phone number must be more than 8 and less than 12");
        if(phoneNumber.length() > 11)
            throw new BadRequestException("length of phone number must be more than 8 and less than 12");
        for (char digit : phoneNumber.toCharArray())
            if (!Character.isDigit(digit))
                throw new BadRequestException("Phone number must be number");
    }

    public static void validateImage(String image){
        String[] validFormats = {
                ".png", ".jpg", ".jpeg", ".gif", ".bmp", ".tiff", ".tif",
                ".webp", ".svg", ".ico", ".pdf", ".eps"
        };
        boolean isValid = false;
        for(String validFormat : validFormats){
            if (image.endsWith(validFormat)) {
                isValid = true;
                break;
            }
        }
        if(!isValid)
            throw new BadRequestException("Invalid image format");
    }
}
