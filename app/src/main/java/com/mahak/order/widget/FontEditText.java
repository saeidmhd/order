package com.mahak.order.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.mahak.order.BaseActivity;

import java.util.Locale;

public class FontEditText extends androidx.appcompat.widget.AppCompatEditText {
    public FontEditText(Context context) {
        super(context);
        init(context);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        /*float size = SharedPreferencesHelper.getCurrentFontSize(context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);*/
        if (Locale.getDefault().getLanguage().equals("de"))
            this.setTypeface(BaseActivity.font_openSans);
        else
            this.setTypeface(BaseActivity.font_yekan);
    }

}
