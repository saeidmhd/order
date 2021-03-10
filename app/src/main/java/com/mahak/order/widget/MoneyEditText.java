package com.mahak.order.widget;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.PriceInputFilterMinMax;
import com.mahak.order.common.ServiceTools;

import java.util.Locale;


public class MoneyEditText extends androidx.appcompat.widget.AppCompatEditText {

    double max = 999999999999999d;

    public MoneyEditText(Context context) {
        super(context);
        init(context);
        addFilters();
        addTextChangedListener(MoneySplitter());
    }

    public MoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        addFilters();
        addTextChangedListener(MoneySplitter());
    }

    public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        addFilters();
        addTextChangedListener(MoneySplitter());
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
        setFilters(new InputFilter[]{new PriceInputFilterMinMax(0, max, false)});
    }

    public TextWatcher MoneySplitter() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    removeTextChangedListener(this);
                    String value = s.toString();
                    if (!value.equals("")) {
                        if (!value.contains(String.valueOf(ServiceTools.getDecimalSeparator()))) {
                            if (!TextUtils.isEmpty(value))
                                setText(ServiceTools.formatPrice(ServiceTools.toDouble(value)));
                            setSelection(getText().toString().length());
                        }
                    }
                    addTextChangedListener(this);
                } catch (Exception ex) {
                    FirebaseCrashlytics.getInstance().recordException(ex);
                    ex.printStackTrace();
                    addTextChangedListener(this);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        return textWatcher;
    }
}
