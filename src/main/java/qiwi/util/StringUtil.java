package qiwi.util;

import java.math.BigDecimal;

public class StringUtil {
    public String removeTrailingZeros(BigDecimal bigDecimalValue) {
        String value = bigDecimalValue.toString();
        String substringAfterPoint = value.split("\\.")[1];

        int index;
        for (index = value.length() - 1; index > 0; index--) {
            if (value.charAt(index) != '0') {
                break;
            }
        }

        return substringAfterPoint.matches("[0]+")
                ? value.substring(0, index)
                : value.substring(0, index + 1);
    }
}
