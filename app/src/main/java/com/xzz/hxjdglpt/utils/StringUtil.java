package com.xzz.hxjdglpt.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringUtil {
    public static final String SPLITOR_COMMON = "_#,#_";

    public static final String NUMERIC_CAHRS = "0123456789";
    public static final String UPPER_ALPHA_NUMERIC_CAHRS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHA_NUMERIC_CHARS = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String[] CHINESE_MONEY_NUMBERS = {"�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?"};
    private static final String[] CHINESE_MONEY_INTEGER = {"�?", "�?", "�?", "�?", "�?", "�?",
			"�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?", "�?"};
    private static final String[] CHINESE_MONEY_DECIMAL = {"�?", "�?", "�?"};

    public static String append(String str, String appender, int position) {
        if (toString(str).equals("")) {
            return appender;
        } else if (toString(appender).equals("")) {
            return str;
        } else if (position <= 0) {
            return appender + str;
        } else if (position > str.length()) {
            return str + appender;
        }

        String substr1 = str.substring(0, position);
        String substr2 = str.substring(position, str.length());
        return substr1 + appender + substr2;
    }

    public static int indexOf(String str, String substr) {
        if (!toString(str).equals("")) {
            return str.indexOf(substr);
        }
        return -1;
    }

    public static int lastIndexOf(String str, String substr) {
        if (!toString(str).equals("")) {
            return str.lastIndexOf(substr);
        }
        return -1;
    }

    public static boolean isEmpty(String str) {
        if (toString(str).equals("")) {
            return true;
        }
        return false;
    }

    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    public static String random(int length) {
        return random(length, ALPHA_NUMERIC_CHARS);
    }

    public static String random(int length, String seed) {
        if (length < 1) {
            return "";
        }

        Random random = new Random();
        char[] chars = new char[length];
        char[] letters = seed.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = letters[random.nextInt(seed.length())];
        }
        return new String(chars);
    }

    public static String space(String str, char spacer, int slotter) {
        if (!toString(str).equals("")) {
            if (str.length() >= slotter) {
                return str;
            } else {
                String result = str;
                slotter = slotter - str.length();
                for (int i = 0; i < slotter; i++) {
                    result = spacer + result;
                }
                return result;
            }
        }
        return "";
    }

    public static Map<Integer, String> split(String str) {
        Map<Integer, String> strs = new HashMap<Integer, String>();
        if (!toString(str).equals("")) {
            String[] _strs = str.split(SPLITOR_COMMON);
            for (int i = 0; i < _strs.length; i++) {
                strs.put(i, _strs[i]);
            }
        }
        return strs;
    }

    public static String[] split(String str, String splitter) {
        if (!toString(str).equals("")) {
            return str.split(splitter);
        }
        return new String[0];
    }

    public static int splitLength(String str, String splitter) {
        return split(str, splitter).length;
    }

    public static String substring(String str, int limit) {
        if (!toString(str).equals("")) {
            if (str.length() <= limit) {
                return str;
            } else {
                return str.substring(0, limit) + "...";
            }
        }
        return "";
    }

    public static String toString(Object obj) {
        if (obj != null) {
            return obj.toString().trim();
        }
        return "";
    }

    public static List<String> toList(String str) throws IOException {
        List<String> strs = new ArrayList<String>();
        if (!toString(str).equals("")) {
            StringReader stringReader = new StringReader(str);
            BufferedReader bufferedReader = new BufferedReader(stringReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                strs.add(line);
            }
        }
        return strs;
    }

    public static String toChineseMoney(String str) {
        String integerStr;
        String decimalStr;
        str = toString(str);
        str = str.replaceAll(",", "");
        if (str.indexOf(".") > 0) {
            integerStr = str.substring(0, str.indexOf("."));
            decimalStr = str.substring(str.indexOf(".") + 1);
        } else if (str.indexOf(".") == 0) {
            integerStr = "";
            decimalStr = str.substring(1);
        } else {
            integerStr = str;
            decimalStr = "";
        }

        if (!integerStr.equals("")) {
            integerStr = Long.toString(Long.parseLong(integerStr));
            if (integerStr.equals("0")) {
                integerStr = "";
            }
        }

        if (integerStr.length() > CHINESE_MONEY_INTEGER.length) {
            return str;
        }

        int[] integers = new int[integerStr.length()];
        for (int i = 0; i < integerStr.length(); i++) {
            integers[i] = Integer.parseInt(integerStr.substring(i, i + 1));
        }

        boolean isMust5 = false;
        int length = integerStr.length();
        if (length > 4) {
            String subInteger = "";
            if (length > 8) {
                subInteger = integerStr.substring(length - 8, length - 4);
            } else {
                subInteger = integerStr.substring(0, length - 4);
            }

            if (Integer.parseInt(subInteger) > 0) {
                isMust5 = true;
            }
        }

        int[] decimals = new int[decimalStr.length()];
        for (int i = 0; i < decimalStr.length(); i++) {
            decimals[i] = Integer.parseInt(decimalStr.substring(i, i + 1));
        }

        StringBuffer chineseInteger = new StringBuffer("");
        length = integers.length;
        for (int i = 0; i < length; i++) {
            String key = "";
            if (integers[i] == 0) {
                if ((length - i) == 13) {
                    key = CHINESE_MONEY_INTEGER[4];
                } else if ((length - i) == 9) {
                    key = CHINESE_MONEY_INTEGER[8];
                } else if ((length - i) == 5 && isMust5) {
                    key = CHINESE_MONEY_INTEGER[4];
                } else if ((length - i) == 1) {
                    key = CHINESE_MONEY_INTEGER[0];
                }

                if ((length - i) > 1 && integers[i + 1] != 0) {
                    key += CHINESE_MONEY_NUMBERS[0];
                }
            }

            chineseInteger.append(integers[i] == 0 ? key : (CHINESE_MONEY_NUMBERS[integers[i]] +
					CHINESE_MONEY_INTEGER[length - i - 1]));
        }

        StringBuffer chineseDecimal = new StringBuffer("");
        for (int i = 0; i < decimals.length; i++) {
            if (i == 3) {
                break;
            }

            chineseDecimal.append((decimals[i] == 0) ? "" : (CHINESE_MONEY_NUMBERS[decimals[i]] + CHINESE_MONEY_DECIMAL[i]));
        }

        return chineseInteger.toString() + chineseDecimal.toString();
    }

    public static String generateOrderNumber(String prefix, String orderNumber) {
        long subNumber = 1;
        if (!StringUtil.toString(orderNumber).equals("")) {
            subNumber = NumberUtil.toLong(orderNumber.substring(prefix.length())) + 1;
        }

        orderNumber = prefix + StringUtil.space(subNumber + "", '0', 10);
        return orderNumber;
    }

}