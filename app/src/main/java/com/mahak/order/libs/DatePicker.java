//***********************************
//Persian Date Picker
//Author: Mehrdad Safar Mohammad Loo
//e-mail: mehrdadsml@gmail.com
//web: mehrdadsml.com
//***********************************

package com.mahak.order.libs;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.R;
import com.mahak.order.common.ServiceTools;
import com.persheh.libraries.dateutil.CivilDate;
import com.persheh.libraries.dateutil.DateConverter;
import com.persheh.libraries.dateutil.PersianDate;

import java.util.Calendar;
import java.util.Date;

public class DatePicker extends LinearLayout {

    private int startYear = 1380;
    private int endYear = 1450;

    private final int MaxYear = 1500;
    private final int MinYear = 1300;

    private final int MaxMonth = 12;
    private final int MinMonth = 1;

    private View myPickerView;

    private Button month_plus;
    private EditText month_display;
    private Button month_minus;

    private Button date_plus;
    private EditText date_display;
    private Button date_minus;

    private Button year_plus;
    private EditText year_display;
    private Button year_minus;

    private Calendar cal;
    private PersianDate pdt;

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context mContext) {
        LayoutInflater inflator = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myPickerView = inflator.inflate(R.layout.datepicker, null);
        this.addView(myPickerView);

        initializeReference();
    }

    private void initializeReference() {

        month_plus = myPickerView.findViewById(R.id.month_plus);
        month_plus.setOnClickListener(month_plus_listener);
        month_display = myPickerView.findViewById(R.id.month_display);
        month_minus = myPickerView.findViewById(R.id.month_minus);
        month_minus.setOnClickListener(month_minus_listener);

        date_plus = myPickerView.findViewById(R.id.date_plus);
        date_plus.setOnClickListener(date_plus_listener);
        date_display = myPickerView.findViewById(R.id.date_display);
        date_display.addTextChangedListener(date_watcher);
        date_minus = myPickerView.findViewById(R.id.date_minus);
        date_minus.setOnClickListener(date_minus_listener);

        year_plus = myPickerView.findViewById(R.id.year_plus);
        year_plus.setOnClickListener(year_plus_listener);
        year_display = myPickerView.findViewById(R.id.year_display);
        year_display.setOnFocusChangeListener(mLostFocusYear);
        year_display.addTextChangedListener(year_watcher);
        year_minus = myPickerView.findViewById(R.id.year_minus);
        year_minus.setOnClickListener(year_minus_listener);

        initData();
        initFilterNumericDigit();
    }

    private void initData() {
        cal = Calendar.getInstance();
        CivilDate cdt = new CivilDate(cal);
        pdt = DateConverter.civilToPersian(cdt);
        month_display.setText(months[pdt.getMonth() - 1]);
        date_display.setText(String.valueOf(pdt.getDayOfMonth()));
        year_display.setText(String.valueOf(pdt.getYear()));
    }

    public void setDate(Date dt) {
        CivilDate cdt = new CivilDate(dt.getTime());
        pdt = DateConverter.civilToPersian(cdt);
        month_display.setText(months[pdt.getMonth() - 1]);
        date_display.setText(String.valueOf(pdt.getDayOfMonth()));
        year_display.setText(String.valueOf(pdt.getYear()));
    }

    public Date getDate() {
        CivilDate cdt = DateConverter.persianToCivil(pdt);
        cal.set(cdt.getYear(), cdt.getMonth() - 1, cdt.getDayOfMonth());
        Date dt = new Date(cal.getTimeInMillis());
        return dt;
    }

    public PersianDate getPersianDate() {
        return pdt;
    }

    private void initFilterNumericDigit() {

        try {
            date_display.setFilters(new InputFilter[]{new InputFilterMinMax(1, DaysOfMonths[pdt.getMonth() - 1])});

            InputFilter[] filterArray_year = new InputFilter[1];
            filterArray_year[0] = new InputFilter.LengthFilter(4);
            year_display.setFilters(filterArray_year);
        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
    }

    private void changeFilter() {
        try {
            date_display.setFilters(new InputFilter[]{new InputFilterMinMax(1, DaysOfMonths[pdt.getMonth() - 1])});
        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            date_display.setText(String.valueOf(pdt.getDayOfMonth()));
            e.printStackTrace();
        }
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        if (startYear < MaxYear && startYear > MinYear) {
            this.startYear = startYear;
            swapStartEndYear();
        } else {
            throw new NumberFormatException(
                    "StartYear should be in the range of 1900 to 2100");
        }
    }

    public void reset() {
        initData();
    }

    public int getEndYear() {
        return endYear;
    }

    public void setDateChangedListener(DateWatcher listener) {
        this.mDateWatcher = listener;
    }

    public void removeDateChangedListener() {
        this.mDateWatcher = null;
    }

    public void setEndYear(int endYear) {
        if (endYear < MaxYear && endYear > MinYear) {
            this.endYear = endYear;
            swapStartEndYear();
        } else {
            throw new NumberFormatException(
                    "endYear should be in the range of 1900 to 2100");
        }
    }

    String[] months = {"فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان",
            "آذر", "دی", "بهمن", "اسفند"};

    int[] DaysOfMonths = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 30};

    OnClickListener month_plus_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            try {
                if (pdt.getMonth() < MaxMonth) {
                    pdt.setMonth(pdt.getMonth() + 1);
                } else {
                    pdt.setMonth(MinMonth);
                    if (pdt.getYear() >= endYear) {
                        pdt.setYear(startYear);
                    } else {
                        pdt.setYear(pdt.getYear() + 1);
                    }
                }
                if (pdt.getDayOfMonth() > DaysOfMonths[pdt.getMonth() - 1])
                    pdt.setDayOfMonth(DaysOfMonths[pdt.getMonth() - 1]);

                month_display.setText(months[pdt.getMonth() - 1]);
                date_display.setText(String.valueOf(pdt.getDayOfMonth()));
                year_display.setText(String.valueOf(pdt.getYear()));

                changeFilter();
                sendToListener();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                Log.e("", e.toString());
            }
        }
    };
    OnClickListener month_minus_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try {
                if (pdt.getMonth() > MinMonth) {
                    pdt.setMonth(pdt.getMonth() - 1);
                } else {
                    pdt.setMonth(MaxMonth);
                    if (pdt.getYear() <= startYear) {
                        pdt.setYear(endYear);
                    } else {
                        pdt.setYear(pdt.getYear() - 1);
                    }
                }
                if (pdt.getDayOfMonth() > DaysOfMonths[pdt.getMonth() - 1])
                    pdt.setDayOfMonth(DaysOfMonths[pdt.getMonth() - 1]);

                month_display.setText(months[pdt.getMonth() - 1]);
                date_display.setText(String.valueOf(pdt.getDayOfMonth()));
                year_display.setText(String.valueOf(pdt.getYear()));

                changeFilter();
                sendToListener();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                Log.e("", e.toString());
            }
        }
    };

    OnClickListener date_plus_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            try {
                date_display.requestFocus();
                if (pdt.getDayOfMonth() < DaysOfMonths[pdt.getMonth() - 1]) {
                    pdt.setDayOfMonth(pdt.getDayOfMonth() + 1);
                } else {
                    if (pdt.getMonth() < MaxMonth) {
                        pdt.setMonth(pdt.getMonth() + 1);
                    } else {
                        pdt.setMonth(MinMonth);
                        if (pdt.getYear() >= endYear) {
                            pdt.setYear(startYear);
                        } else {
                            pdt.setYear(pdt.getYear() + 1);
                        }
                    }
                    changeFilter();
                    pdt.setDayOfMonth(1);
                }

                month_display.setText(months[pdt.getMonth() - 1]);
                date_display.setText(String.valueOf(pdt.getDayOfMonth()));
                year_display.setText(String.valueOf(pdt.getYear()));

                sendToListener();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }

        }
    };
    OnClickListener date_minus_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            try {
                date_display.requestFocus();
                if (pdt.getDayOfMonth() > 1) {
                    pdt.setDayOfMonth(pdt.getDayOfMonth() - 1);
                } else {
                    if (pdt.getMonth() > MinMonth) {
                        pdt.setMonth(pdt.getMonth() - 1);
                    } else {
                        pdt.setMonth(MaxMonth);
                        if (pdt.getYear() <= startYear) {
                            pdt.setYear(endYear);
                        } else {
                            pdt.setYear(pdt.getYear() - 1);
                        }
                    }
                    changeFilter();
                    pdt.setDayOfMonth(DaysOfMonths[pdt.getMonth() - 1]);
                }

                month_display.setText(months[pdt.getMonth() - 1]);
                date_display.setText(String.valueOf(pdt.getDayOfMonth()));
                year_display.setText(String.valueOf(pdt.getYear()));

                sendToListener();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
        }
    };
    OnClickListener year_plus_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            try {
                year_display.requestFocus();

                if (pdt.getYear() >= endYear) {
                    pdt.setYear(startYear);
                } else {
                    pdt.setYear(pdt.getYear() + 1);
                }

                month_display.setText(months[pdt.getMonth() - 1]);
                date_display.setText(String.valueOf(pdt.getDayOfMonth()));
                year_display.setText(String.valueOf(pdt.getYear()));

                changeFilter();
                sendToListener();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }

        }
    };
    OnClickListener year_minus_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            try {
                year_display.requestFocus();

                if (pdt.getYear() <= startYear) {
                    pdt.setYear(endYear);
                } else {
                    pdt.setYear(pdt.getYear() - 1);
                }

                month_display.setText(months[pdt.getMonth() - 1]);
                date_display.setText(String.valueOf(pdt.getDayOfMonth()));
                year_display.setText(String.valueOf(pdt.getYear()));

                changeFilter();
                sendToListener();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }

        }
    };

    OnFocusChangeListener mLostFocusYear = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                year_display.setText(String.valueOf(pdt.getYear()));
            }
        }
    };

    class InputFilterMinMax implements InputFilter {

        private final int min;
        private final int max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = ServiceTools.toInt(min);
            this.max = ServiceTools.toInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            try {
                int input = ServiceTools.toInt(dest.toString() + source.toString());
                if (isInRange(min, max, input)) {
                    return null;
                }
            } catch (NumberFormatException nfe) {
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }

    TextWatcher date_watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            try {
                if (s.toString().length() > 0) {
                    pdt.setDayOfMonth(ServiceTools.toInt(s.toString()));

                    month_display.setText(months[pdt.getMonth() - 1]);

                    sendToListener();
                }
            } catch (NumberFormatException e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }

        }
    };

    TextWatcher year_watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                if (s.toString().length() == 4) {
                    int year = ServiceTools.toInt(s.toString());

                    if (year > endYear) {
                        pdt.setYear(endYear);
                    } else if (year < startYear) {
                        pdt.setYear(startYear);
                    } else {
                        pdt.setYear(year);
                    }
                }

                sendToListener();
            } catch (Exception e) {
                ServiceTools.logToFireBase(e);
                e.printStackTrace();
            }
        }
    };

    private void sendToListener() {
        if (mDateWatcher != null) {
            mDateWatcher.onDateChanged(cal);
        }
    }

    DateWatcher mDateWatcher = null;

    public interface DateWatcher {
        void onDateChanged(Calendar c);
    }

    private void swapStartEndYear() {
        if (this.startYear > this.endYear) {
            int temp = endYear;
            endYear = startYear;
            startYear = temp;
        }

        pdt.setYear(endYear);
        initDisplay();

    }

    private void initDisplay() {
        month_display.setText(months[pdt.getMonth() - 1]);
        date_display.setText(String.valueOf(pdt.getDayOfMonth()));
        year_display.setText(String.valueOf(pdt.getYear()));
    }

}
