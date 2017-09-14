package de.imolli.mywarp.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class MathUtils {

    public static double round(double value, int radius) {
        NumberFormat n = NumberFormat.getInstance(Locale.US);
        n.setMaximumFractionDigits(radius);

        return Double.parseDouble(n.format(new BigDecimal(value)).replaceAll(",", ""));
    }

}
