package com.paulovfe.agendavoting.component;

import java.util.Random;

public class CPFHelper {

    public static String generate() {
        final Random r = new Random();
        final StringBuilder sbCpfNumber = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sbCpfNumber.append(r.nextInt(9));
        }
        return generateDigits(sbCpfNumber.toString());
    }

    private static String generateDigits(final String digitsBase) {
        final StringBuilder sbCpfNumber = new StringBuilder(digitsBase);
        int total = 0;
        int multiple = digitsBase.length() + 1;
        for (final char digit : digitsBase.toCharArray()) {
            final long parcial = Integer.parseInt(String.valueOf(digit)) * (multiple--);
            total += parcial;
        }
        int resto = Integer.parseInt(String.valueOf(Math.abs(total % 11)));
        if (resto < 2) {
            resto = 0;
        } else {
            resto = 11 - resto;
        }
        sbCpfNumber.append(resto);
        if (sbCpfNumber.length() < 11) {
            return generateDigits(sbCpfNumber.toString());
        }
        return sbCpfNumber.toString();
    }
}
