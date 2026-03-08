package com.erp.jp.common.util;

import java.util.regex.Pattern;

/**
 * 日本 JAN 码校验工具
 */
public class JanCodeValidator {
    
    private static final Pattern JAN_PATTERN = Pattern.compile("^\\d{8}(\\d{3})?$");
    
    /**
     * 校验 JAN 码格式
     */
    public static boolean isValid(String janCode) {
        if (janCode == null || janCode.isEmpty()) {
            return false;
        }
        return JAN_PATTERN.matcher(janCode).matches();
    }
    
    /**
     * 校验 JAN 码并计算校验位
     */
    public static boolean checkDigit(String janCode) {
        if (!isValid(janCode)) {
            return false;
        }
        
        int sum = 0;
        int weight = 1;
        
        // 从右往左计算（不包括最后一位校验位）
        for (int i = janCode.length() - 2; i >= 0; i--) {
            int digit = Character.getNumericValue(janCode.charAt(i));
            sum += digit * weight;
            weight = weight == 1 ? 3 : 1;
        }
        
        int checkDigit = (10 - (sum % 10)) % 10;
        int actualCheckDigit = Character.getNumericValue(janCode.charAt(janCode.length() - 1));
        
        return checkDigit == actualCheckDigit;
    }
}
