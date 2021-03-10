package com.mahak.order.widget;

import android.content.Context;
import android.text.InputFilter;
import android.util.AttributeSet;

import com.mahak.order.BaseActivity;
import com.mahak.order.common.CountInputFilterMinMax;

import java.util.Locale;


public class CountEditText extends androidx.appcompat.widget.AppCompatEditText {

    double max = 999999999999999d;

    public CountEditText(Context context) {
        super(context);
        init(context);
        addFilters();
        //setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
    }

    public CountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        addFilters();
        //setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
    }

    public CountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        addFilters();
        //setKeyListener(DigitsKeyListener.getInstance("0123456789.,"));
    }

    private void init(Context context) {
        /*float size = SharedPreferencesHelper.getCurrentFontSize(context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);*/
        if (Locale.getDefault().getLanguage().equals("de"))
            this.setTypeface(BaseActivity.font_openSans);
        else
            this.setTypeface(BaseActivity.font_yekan);


    }

    private void addFilters() {
        setFilters(new InputFilter[]{new CountInputFilterMinMax(0, max)});
    }

}
