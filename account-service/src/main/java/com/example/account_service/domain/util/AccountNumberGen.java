package com.example.account_service.domain.util;

import java.util.Random;

public class AccountNumberGen {

    private static final Random random = new Random();

    public static String generate() {

        String entity = generateNumber(4);
        String branch = generateNumber(4);
        String accNum = generateNumber(4);

        int control1 = calculateDigit(entity + branch);
        int control2 = calculateDigit(accNum);

        return entity +
                "-" +
                branch +
                "-" +
                (control1 + control2) +
                accNum;

    }

    private static String generateNumber(int maxIteration) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < maxIteration; i++) {
            sb.append(random.nextInt());
        }

        return sb.toString();
    }

    private static  int calculateDigit(String num) {
        int sum = 0;
        for (int i = 0; i < num.length(); i++) {
            sum += Character.getNumericValue(num.charAt(i));
        }

        return sum % 10;
    }
}
