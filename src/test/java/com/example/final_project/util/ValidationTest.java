package com.example.final_project.util;

import com.example.final_project.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidationTest {

    @ParameterizedTest
    @ValueSource(strings = {"012345678", "0123456789", "01234567890"})
    void acceptsPhoneNumbersOfNineToElevenDigits(String phone) {
        assertThatCode(() -> Validation.validatePhoneNumber(phone)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "112345678",     // does not start with 0
            "01234567",      // 8 digits, one short
            "012345678901",  // 12 digits, one too many
            "01234567a"      // not all digits
    })
    void rejectsMalformedPhoneNumbers(String phone) {
        assertThatThrownBy(() -> Validation.validatePhoneNumber(phone))
                .isInstanceOf(BadRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"logo.png", "photo.jpg", "scan.jpeg", "chart.svg", "poster.pdf", "icon.ico"})
    void acceptsKnownImageExtensions(String image) {
        assertThatCode(() -> Validation.validateImage(image)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"notes.txt", "payload.exe", "archive.zip", "noextensionatall"})
    void rejectsExtensionsThatAreNotImages(String image) {
        assertThatThrownBy(() -> Validation.validateImage(image))
                .isInstanceOf(BadRequestException.class);
    }

    /**
     * Documents a real limitation rather than asserting it is correct:
     * {@code validateImage} compares with {@code endsWith}, so the check is case
     * sensitive and a camera-default name like IMG_0001.PNG is turned away. Left
     * as-is because loosening it changes what the upload endpoints accept; see
     * the note raised alongside these tests.
     */
    @Test
    void treatsUppercaseExtensionsAsInvalid() {
        assertThatThrownBy(() -> Validation.validateImage("IMG_0001.PNG"))
                .isInstanceOf(BadRequestException.class);
    }
}
