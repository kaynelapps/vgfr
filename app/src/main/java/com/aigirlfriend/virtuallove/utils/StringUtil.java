package com.aigirlfriend.virtuallove.utils;

import android.text.TextUtils;
import java.util.regex.Pattern;

public class StringUtil {
    private StringUtil() {
    }

    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || TextUtils.isEmpty(charSequence);
    }

    public static boolean isNotEmpty(CharSequence charSequence) {
        return charSequence != null && !TextUtils.isEmpty(charSequence);
    }

    public static boolean isEmail(String str) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(str).matches();
    }

    public static boolean isNumeric(char c) {
        return Pattern.compile("[0-9]*").matcher(String.valueOf(c)).matches();
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static String getNumberByStr(String str) {
        if (isEmpty(str)) {
            return str;
        }
        String str2 = "";
        for (int length = str.length() - 1; length >= 0; length--) {
            char charAt = str.charAt(length);
            if (!isNumeric(charAt)) {
                break;
            }
            str2 = charAt + str2;
        }
        return str2;
    }

    public static String obtainMsgId() {
        return String.valueOf(System.currentTimeMillis()) + ((System.currentTimeMillis() % 99) + 1);
    }
}
