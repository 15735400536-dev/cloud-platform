package com.maxinhai.platform.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：NumberBaseConverter
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 16:47
 * @Description: 进制转换工具类（支持十进制与二进制、八进制、十六进制之间的相互转换）
 */
public class NumberBaseConverter {

    // 十六进制字符映射表
    private static final Map<Character, Integer> HEX_MAP;

    static {
        HEX_MAP = new HashMap<>();
        // 初始化0-9的映射
        for (int i = 0; i <= 9; i++) {
            HEX_MAP.put((char) ('0' + i), i);
        }
        // 初始化A-F的映射
        for (int i = 10; i <= 15; i++) {
            HEX_MAP.put((char) ('A' + i - 10), i);
        }
        // 初始化a-f的映射
        for (int i = 10; i <= 15; i++) {
            HEX_MAP.put((char) ('a' + i - 10), i);
        }
    }

    /**
     * 十进制转二进制
     *
     * @param decimal 十进制数
     * @return 二进制字符串
     */
    public static String decimalToBinary(int decimal) {
        if (decimal == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        boolean isNegative = decimal < 0;
        if (isNegative) {
            decimal = -decimal;
        }

        while (decimal > 0) {
            sb.append(decimal % 2);
            decimal = decimal / 2;
        }

        if (isNegative) {
            sb.append("-");
        }

        return sb.reverse().toString();
    }

    /**
     * 十进制转八进制
     *
     * @param decimal 十进制数
     * @return 八进制字符串
     */
    public static String decimalToOctal(int decimal) {
        if (decimal == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        boolean isNegative = decimal < 0;
        if (isNegative) {
            decimal = -decimal;
        }

        while (decimal > 0) {
            sb.append(decimal % 8);
            decimal = decimal / 8;
        }

        if (isNegative) {
            sb.append("-");
        }

        return sb.reverse().toString();
    }

    /**
     * 十进制转十六进制
     *
     * @param decimal 十进制数
     * @return 十六进制字符串(大写)
     */
    public static String decimalToHex(int decimal) {
        if (decimal == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        boolean isNegative = decimal < 0;
        if (isNegative) {
            decimal = -decimal;
        }

        while (decimal > 0) {
            int remainder = decimal % 16;
            if (remainder < 10) {
                sb.append(remainder);
            } else {
                sb.append((char) ('A' + remainder - 10));
            }
            decimal = decimal / 16;
        }

        if (isNegative) {
            sb.append("-");
        }

        return sb.reverse().toString();
    }

    /**
     * 二进制转十进制
     *
     * @param binary 二进制字符串
     * @return 对应的十进制数
     * @throws IllegalArgumentException 当输入不是有效的二进制字符串时抛出
     */
    public static int binaryToDecimal(String binary) {
        validateBinary(binary);

        boolean isNegative = binary.startsWith("-");
        int startIndex = isNegative ? 1 : 0;
        int result = 0;

        for (int i = startIndex; i < binary.length(); i++) {
            char c = binary.charAt(i);
            int digit = c - '0';
            result = result * 2 + digit;
        }

        return isNegative ? -result : result;
    }

    /**
     * 八进制转十进制
     *
     * @param octal 八进制字符串
     * @return 对应的十进制数
     * @throws IllegalArgumentException 当输入不是有效的八进制字符串时抛出
     */
    public static int octalToDecimal(String octal) {
        validateOctal(octal);

        boolean isNegative = octal.startsWith("-");
        int startIndex = isNegative ? 1 : 0;
        int result = 0;

        for (int i = startIndex; i < octal.length(); i++) {
            char c = octal.charAt(i);
            int digit = c - '0';
            result = result * 8 + digit;
        }

        return isNegative ? -result : result;
    }

    /**
     * 十六进制转十进制
     *
     * @param hex 十六进制字符串
     * @return 对应的十进制数
     * @throws IllegalArgumentException 当输入不是有效的十六进制字符串时抛出
     */
    public static int hexToDecimal(String hex) {
        validateHex(hex);

        boolean isNegative = hex.startsWith("-");
        int startIndex = isNegative ? 1 : 0;
        int result = 0;

        for (int i = startIndex; i < hex.length(); i++) {
            char c = hex.charAt(i);
            result = result * 16 + HEX_MAP.get(c);
        }

        return isNegative ? -result : result;
    }

    /**
     * 验证二进制字符串是否有效
     */
    private static void validateBinary(String binary) {
        if (binary == null || binary.isEmpty()) {
            throw new IllegalArgumentException("二进制字符串不能为空");
        }

        int startIndex = binary.startsWith("-") ? 1 : 0;
        if (startIndex >= binary.length()) {
            throw new IllegalArgumentException("无效的二进制字符串: " + binary);
        }

        for (int i = startIndex; i < binary.length(); i++) {
            char c = binary.charAt(i);
            if (c != '0' && c != '1') {
                throw new IllegalArgumentException("无效的二进制字符: " + c);
            }
        }
    }

    /**
     * 验证八进制字符串是否有效
     */
    private static void validateOctal(String octal) {
        if (octal == null || octal.isEmpty()) {
            throw new IllegalArgumentException("八进制字符串不能为空");
        }

        int startIndex = octal.startsWith("-") ? 1 : 0;
        if (startIndex >= octal.length()) {
            throw new IllegalArgumentException("无效的八进制字符串: " + octal);
        }

        for (int i = startIndex; i < octal.length(); i++) {
            char c = octal.charAt(i);
            if (c < '0' || c > '7') {
                throw new IllegalArgumentException("无效的八进制字符: " + c);
            }
        }
    }

    /**
     * 验证十六进制字符串是否有效
     */
    private static void validateHex(String hex) {
        if (hex == null || hex.isEmpty()) {
            throw new IllegalArgumentException("十六进制字符串不能为空");
        }

        int startIndex = hex.startsWith("-") ? 1 : 0;
        if (startIndex >= hex.length()) {
            throw new IllegalArgumentException("无效的十六进制字符串: " + hex);
        }

        for (int i = startIndex; i < hex.length(); i++) {
            char c = hex.charAt(i);
            if (!HEX_MAP.containsKey(c)) {
                throw new IllegalArgumentException("无效的十六进制字符: " + c);
            }
        }
    }

}
