package com.mahak.order.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.mahak.order.BaseActivity;
import com.mahak.order.common.SharedPreferencesHelper;

import java.util.Locale;


/**
 * Created by mostafavi on 1/12/2016.
 */
public class BoldPrintTextView extends androidx.appcompat.widget.AppCompatTextView {
    public BoldPrintTextView(Context context) {
        super(context);
        init(context);
    }

    public BoldPrintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoldPrintTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        float size = SharedPreferencesHelper.getCurrentFontSize(context);
        float Bold_size = size + 2;
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, Bold_size);
        if (Locale.getDefault().getLanguage().equals("de"))
            this.setTypeface(BaseActivity.font_openSans);
        else
            this.setTypeface(BaseActivity.font_yekan);
    }

}
