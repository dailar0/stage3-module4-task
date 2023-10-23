package com.mjc.school.service.validation;

import com.mjc.school.service.DTO.NewsInputDTO;
import com.mjc.school.service.NewsServiceImpl;
import com.mjc.school.service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ValidatorImplTest {
    @Autowired
    NewsServiceImpl newsService;
    Random random = new Random();

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomString(int minLength, int maxLength) {
        if (minLength > maxLength) {
            throw new IllegalArgumentException("Minimum length cannot be greater than maximum length.");
        }

        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    private static String getValidTitle() {
        return randomString(5, 30);
    }

    private static String getValidContent() {
        return randomString(5, 255);
    }

    @BeforeEach
    public void populateAuthor() {

    }

    @Test
    public void testShortTitle() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(
                random.nextLong(),
                randomString(0, 4),
                getValidContent(),
                null,
                null);
        assertThrows(ValidationException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void testLongTitle() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(
                random.nextLong(),
                randomString(30, 1000),
                getValidTitle(),
                null,
                null);
        assertThrows(ValidationException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void testShortContent() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                getValidTitle(),
                randomString(0, 4),
                null,
                null);
        assertThrows(ValidationException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void testLongContent() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                getValidTitle(),
                randomString(256, 1000),
                null,
                null);
        assertThrows(ValidationException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void testMaxBoundaryValues() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                randomString(30, 30),
                randomString(255, 255),
                null,
                null);
        newsService.create(newsInputDTO);
    }

    @Test
    public void testMinBoundaryValues() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                randomString(5, 5),
                randomString(5, 5),
                null,
                null);
        newsService.create(newsInputDTO);
    }

    @Test
    public void testNullTitle() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                null,
                getValidContent(),
                null,
                null);
        assertThrows(ValidationException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void testNullContent() {
        NewsInputDTO newsInputDTO = new NewsInputDTO(random.nextLong(),
                getValidTitle(),
                null,
                null,
                null);
        assertThrows(ValidationException.class, () -> newsService.create(newsInputDTO));
    }

    @Test
    public void TestNullObject() {
        assertThrows(NullPointerException.class,() -> newsService.create(null));
    }

}