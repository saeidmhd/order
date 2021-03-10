package com.mahak.order.widget;

import android.app.Dialog;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.widget.Button;
import android.widget.TextView;

import com.mahak.order.BaseActivity;

public class FontAlertDialog {
    public static void FontDialog(Dialog dialog) {
        Button button = dialog.findViewById(android.R.id.button1);
        if (button != null)
            button.setTypeface(BaseActivity.font_yekan);

        Button button2 = dialog.findViewById(android.R.id.button2);
        if (button2 != null)
            button2.setTypeface(BaseActivity.font_yekan);

        Button button3 = dialog.findViewById(android.R.id.button3);
        if (button3 != null)
            button3.setTypeface(BaseActivity.font_yekan);

        TextView message = dialog.findViewById(android.R.id.message);
        if (message != null)
            message.setTypeface(BaseActivity.font_yekan);

    }

    public static SpannableString getFontTitle(String title) {
        CustomTFSpan tfSpan = new CustomTFSpan(BaseActivity.font_yekan);
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(tfSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static class CustomTFSpan extends TypefaceSpan {

        private final Typeface typeface;

        public CustomTFSpan(Typeface typeface) {
            super("");
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyTypeFace(ds, typeface);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyTypeFace(paint, typeface);
        }

        private void applyTypeFace(Paint paint, Typeface tf) {
            paint.setTypeface(tf);
        }
    }
}
