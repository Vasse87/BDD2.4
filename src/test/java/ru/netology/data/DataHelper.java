package ru.netology.data;
import com.github.javafaker.Faker;

import lombok.Value;

import java.util.Locale;
import java.util.Random;

public class DataHelper {
    private DataHelper() {}

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getOtherAuthInfo() {
        return new AuthInfo("petya", "123qwerty");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        return new VerificationCode("12345");
    }

    @Value
    public static class CardInfo {
        private String cardNumber;
        private Integer sum;
        private int balance;
    }

public static String getCardNumber1() {
        return "5559 0000 0000 0001";
}

    public static String getCardNumber2() {
        return "5559 0000 0000 0002";
    }

    public static int generateSum(int min, int max) {
        Faker faker = new Faker();
        return faker.number().numberBetween(min,max);
    }

}
