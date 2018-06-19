package com.my.utils;

import java.math.BigDecimal;

public class StringTool {

    public static String getFormatSize(final double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal bigDecimal = new BigDecimal(kiloByte);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal bigDecimal = new BigDecimal(megaByte);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraByte = gigaByte / 1024;
        if (teraByte < 1) {
            BigDecimal bigDecimal = new BigDecimal(gigaByte);
            return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal bigDecimal = new BigDecimal(teraByte);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
