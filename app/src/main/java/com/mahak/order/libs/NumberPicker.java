/*
 * Copyright (c) 2014 Mehrdad Safar Mohammad Loo
 * All rights reserved.
 * Author: Mehrdadsml@gmail.com
 * Web: www.Mehrdadsml.com
 */

package com.mahak.order.libs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mahak.order.R;
import com.mahak.order.common.ITextChangend;
import com.mahak.order.common.ServiceTools;

public class NumberPicker extends LinearLayout {

    private ITextChangend mTextChanged;
    private final long REPEAT_DELAY = 50;

    //private final int btnELEMENT_HEIGHT = 60;
    private final int btnELEMENT_WIDTH = 45;
    private final int etELEMENT_WIDTH = 60;

    private final int maxInputLength = 4;

    private int MINIMUM = 0;
    private int MAXIMUM = 9999;
    private boolean Result = false;
    private boolean FirstLoad = false;

    private final int TEXT_SIZE = 30;

    public Integer value = 0;
    public long ProductId = 0;
    Button decrement;
    Button increment;
    public EditText valueText;

    private final Handler repeatUpdateHandler = new Handler();

    private boolean autoIncrement = false;
    private boolean autoDecrement = false;

    class RepetetiveUpdater implements Runnable {
        public void run() {
            if (autoIncrement) {
                increment();
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
            } else if (autoDecrement) {
                decrement();
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
            }
        }
    }

    public int DipToPx(int dip) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return px;
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        Result = false;
        this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        this.setGravity(Gravity.CENTER);
        LayoutParams btnElementParams = new LayoutParams(DipToPx(btnELEMENT_WIDTH), LayoutParams.WRAP_CONTENT);
        LayoutParams etElementParams = new LayoutParams(DipToPx(etELEMENT_WIDTH), LayoutParams.WRAP_CONTENT);

        // init the individual elements
        initDecrementButton(context);
        initValueEditText(context);
        initIncrementButton(context);

        if (getOrientation() == VERTICAL) {
            addView(increment, btnElementParams);
            addView(valueText, etElementParams);
            addView(decrement, btnElementParams);
        } else {
            addView(decrement, btnElementParams);
            addView(valueText, etElementParams);
            addView(increment, btnElementParams);
        }


        valueText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                String str = s.toString();
                if (!str.equals("")) {
                    //int num = ServiceTools.toInt(str);
                    //if(num < MINIMUM && num != 0)
                    //valueText.setText(String.valueOf(MINIMUM));
                    //if(num > MAXIMUM)
                    //valueText.setText(String.valueOf(MAXIMUM));
                }
                mTextChanged.onValueChangedListener(s.toString());

            }
        });

        valueText.setEnabled(false);
    }

    private void initIncrementButton(Context context) {
        increment = new Button(context);
        Drawable dr = getResources().getDrawable(R.drawable.forms_btn_plus_selector);
        increment.setBackgroundDrawable(dr);

        // Increment once for a click
        increment.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                increment();
            }
        });

        // Auto increment for a long click
        increment.setOnLongClickListener(new OnLongClickListener() {
                                             public boolean onLongClick(View arg0) {
                                                 autoIncrement = true;
                                                 repeatUpdateHandler.post(new RepetetiveUpdater());
                                                 return false;
                                             }
                                         }
        );

        // When the button is released, if we're auto incrementing, stop
        increment.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                    autoIncrement = false;
                }
                return false;
            }
        });
    }

    private void initValueEditText(Context context) {

        value = new Integer(0);

        valueText = new EditText(context);
        valueText.setTextSize(TEXT_SIZE);

        valueText.setBackgroundResource(R.drawable.img_edittext_center_normal);
        valueText.setTextSize(19.0f);

        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxInputLength);
        valueText.setFilters(fArray);

        valueText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int arg1, KeyEvent event) {
                int backupValue = value;
                try {
                    value = ServiceTools.toInt(((EditText) v).getText().toString());
                } catch (NumberFormatException nfe) {
                    value = backupValue;
                }
                return false;
            }
        });

        // Highlight the number when we get focus
        valueText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });
        valueText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        valueText.setText(value.toString());
        valueText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void initDecrementButton(Context context) {
        decrement = new Button(context);
        Drawable dr = getResources().getDrawable(R.drawable.forms_btn_minus_selector);
        decrement.setBackgroundDrawable(dr);


        // Decrement once for a click
        decrement.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                decrement();
            }
        });


        // Auto Decrement for a long click
        decrement.setOnLongClickListener(
                new OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        autoDecrement = true;
                        repeatUpdateHandler.post(new RepetetiveUpdater());
                        return false;
                    }
                }
        );

        // When the button is released, if we're auto decrementing, stop
        decrement.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                    autoDecrement = false;
                }
                return false;
            }
        });
    }

    public void increment() {

        if (value == 0) {
            value = MINIMUM;
            if (value > MAXIMUM) {
                value = MAXIMUM;
                Result = true;
            }

            valueText.setText(value.toString());
            FirstLoad = true;
        } else {
            if (value < MAXIMUM) {
                value = value + 1;
                valueText.setText(value.toString());
            }
        }
    }

    public void decrement() {

        if (value > MINIMUM) {
            value = value - 1;
            valueText.setText(value.toString());
        } else if (value == MINIMUM || Result == true) {
            value = 0;
            valueText.setText("0");
            FirstLoad = false;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value > MAXIMUM) value = MAXIMUM;
        if (value >= 0) {
            this.value = value;
            valueText.setText(this.value.toString());
        }
    }


    public void setMinValue(int value) {
        MINIMUM = value;
    }

    public void setMaxValue(int value) {
        MAXIMUM = value;

    }

    public void setonChangedValueListener(ITextChangend itextchanged) {
        mTextChanged = itextchanged;
    }

    public void setProductId(long productid) {
        ProductId = productid;
    }

    public long getProductId() {
        return ProductId;
    }


}
