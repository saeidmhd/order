package com.mahak.order.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.view.MenuItem;

import com.mahak.order.BaseActivity;

public class FontPopUp {
    public static void applyFontToMenuItem(MenuItem mi, Context mContext) {
        CustomTFSpan tfSpan = new CustomTFSpan(BaseActivity.font_yekan);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(tfSpan, 0, mNewTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mi.setTitle(mNewTitle);
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
