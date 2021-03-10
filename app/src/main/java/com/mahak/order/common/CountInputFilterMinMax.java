package com.mahak.order.common;

import android.text.InputFilter;
import android.text.Spanned;

import com.mahak.order.BaseActivity;

public class CountInputFilterMinMax implements InputFilter {

    private final double min;
    private final double max;

    public CountInputFilterMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            double input = ServiceTools.toDouble(dest.toString() + source.toString());
            if (isInRange(min, max, input) && checkDecimal(source, start, end, dest, dstart, dend))
                return null;
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(double min, double max, double input) {
        return max > min ? input >= min && input <= max : input >= max && input <= min;
    }

    private boolean checkDecimal(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dotPos = -1;
        int len = dest.length();
        for (int decimals = 0; decimals < len; decimals++) {
            char c = dest.charAt(decimals);
            if (c == ServiceTools.getDecimalSeparator()) {
                dotPos = decimals;
                break;
            }
        }
        if (dotPos >= 0) {
            if (source.equals(String.valueOf(ServiceTools.getDecimalSeparator()))) return false;
            else if (dend <= dotPos) return false;
            else return len - dotPos <= ServiceTools.toInt(BaseActivity.getPrefCountDecimalPoint());
        } else
            return true;
    }
}
