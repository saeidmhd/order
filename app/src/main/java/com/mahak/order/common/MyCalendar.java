package com.mahak.order.common;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mahak.order.R;
import com.mahak.order.libs.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyCalendar {

    static long date;
    static Context context;
    static OnDateSetListener mDateSetListener;
    static MyDatePickerDialog myDatePickerDialog;
    static FragmentManager fragmentManager;

    public interface OnDateSetListener {
        void onDateSet(DatePicker datePicker);

        void onGregorianSet(int year, int monthOfYear, int dayOfMonth);
    }

    public MyCalendar(long date, Context context, FragmentManager fragmentManager, OnDateSetListener listener) {
        MyCalendar.date = date;
        MyCalendar.context = context;
        MyCalendar.fragmentManager = fragmentManager;
        mDateSetListener = listener;
    }

    public static class MyDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private Date dt;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            Dialog dialog;

            if (Locale.getDefault().getLanguage().equals("de")) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(date);
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(context, this, year, month, day);
            } else {
                dt = new Date();
                dt.setTime(date);

                CharSequence title;
                LayoutInflater inflater;
                View dialogLayout;
                AlertDialog.Builder builder;
                title = getString(R.string.str_datepicker);
                inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                dialogLayout = inflater.inflate(R.layout.dialog_single_datepicker, null);

                final DatePicker dp = dialogLayout.findViewById(R.id.datePicker1);
                dp.setDate(dt);

                builder = new AlertDialog.Builder(context);
                builder.setView(dialogLayout)
                        .setTitle(title)
                        .setIcon(R.drawable.ic_datepicker_inverse)
                        .setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mDateSetListener.onDateSet(dp);
                            }
                        })

                        .setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                dialog = builder.create();
            }

            return dialog;
        }

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateSetListener.onGregorianSet(year, monthOfYear, dayOfMonth);
        }
    }

    public void showDialog() {
        myDatePickerDialog = new MyDatePickerDialog();
        myDatePickerDialog.show(fragmentManager, "");
    }
}
