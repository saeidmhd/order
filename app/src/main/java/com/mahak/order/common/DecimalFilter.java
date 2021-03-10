package com.mahak.order.common;

import android.text.InputFilter;
import android.text.Spanned;

public class DecimalFilter implements InputFilter {

    private final int decimalDigits;

    public DecimalFilter(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int dotPos = -1;
        int len = dest.length();
        for (int decimalsI = 0; decimalsI < len; decimalsI++) {
            char c = dest.charAt(decimalsI);
            if (c == '.' || c == ',') {
                dotPos = decimalsI;
                break;
            }
        }
        if (dotPos >= 0) {

            // protects against many dots
            if (source.equals(".") || source.equals(",")) {
                return "";
            }
            // if the text is entered before the dot
            if (dend <= dotPos) {
                return null;
            }
            if (len - dotPos > decimalDigits) {
                return "";
            }
        }

        return null;
    }
}
