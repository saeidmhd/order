package com.mahak.order.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.mahak.order.R;

public class BoldTextView extends androidx.appcompat.widget.AppCompatTextView {
    public BoldTextView(Context context) {
        super(context);
        init(context);
    }

    public BoldTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoldTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        /*this.setTypeface(BaseActivity.font_yekan_bold);
        float size = SharedPreferencesHelper.getCurrentFontSize(context);*/
        // this.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        this.setTextColor(getResources().getColor(R.color.black));
    }

}
