package com.kunbu.common.util.basic;

import java.util.Random;

/**
 * @project: kunbutool
 * @author: kunbu
 * @create: 2020-05-18 17:18
 **/
public class RandomUtil {

    public static final String[] NUMBER_ARRAY =
            {"0","1", "2", "3", "4", "5", "6", "7", "8", "9"};
    public static final String[] LETTER_UPCASE_ARRAY =
            {"a","b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static final String[] LETTER_LOWER_ARRAY =
            {"a","b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public static final String[] LETTER_UPPER_ARRAY =
            {"A","B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public static final String[] ALL_ARRAY =
            {"0","1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A","B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "a","b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static String randomNumberStr(int length) {
        StringBuilder builder = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(NUMBER_ARRAY[r.nextInt(NUMBER_ARRAY.length)]);
        }
        return builder.toString();
    }

}
