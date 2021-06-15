package com.mahak.order.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.datatransport.runtime.dagger.multibindings.ElementsIntoSet;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.ProductPickerListActivity;
import com.mahak.order.R;
import com.mahak.order.autoSync.SyncAlarmReceiver;
import com.mahak.order.libs.BadgeDrawable;
import com.mahak.order.storage.DbAdapter;
import com.mahak.order.storage.DbSchema;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.persheh.libraries.dateutil.CivilDate;
import com.persheh.libraries.dateutil.DateConverter;
import com.persheh.libraries.dateutil.PersianDate;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.mahak.order.BaseActivity.baseUrlImage;
import static com.mahak.order.BaseActivity.getPrefUsername;
import static com.mahak.order.common.ProjectInfo.DIRECTORY_ORDER_SIGNS;

public class ServiceTools {

    public static String formatCount(double amountParam) {
        int DecimalPointNumber = ServiceTools.toInt(BaseActivity.getPrefCountDecimalPoint());
        DecimalFormat myFormatter;
        StringBuilder sb = new StringBuilder();
        if (DecimalPointNumber > 0) {
            for (int i = 0; i < DecimalPointNumber; i++) {
                sb.append("#");
            }
            myFormatter = new DecimalFormat("#." + sb.toString());
        } else {
            myFormatter = new DecimalFormat("#" + sb.toString());
        }
        return myFormatter.format(Double.valueOf(amountParam));
    }

    public static String formatPrice(double value) {
        int DecimalPointNumber = ServiceTools.toInt(BaseActivity.getPrefPriceDecimalPoint());
        Locale locale = Locale.getDefault();
        DecimalFormat myFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        myFormatter.setRoundingMode(RoundingMode.FLOOR);
        StringBuilder sb = new StringBuilder();
        if (DecimalPointNumber > 0) {
            for (int i = 0; i < DecimalPointNumber; i++) {
                sb.append("#");
            }
            myFormatter.applyPattern("###,###." + sb.toString());
        } else
            myFormatter.applyPattern("###,###" + sb.toString());
        if (Locale.getDefault().getDisplayLanguage().equals("Deutsch"))
            return getCurrency().getSymbol() + " " + myFormatter.format(value);
        else
            return myFormatter.format(value);
    }

    public static String formatPriceIgnorLocal(double value) {
        int DecimalPointNumber = ServiceTools.toInt(BaseActivity.getPrefPriceDecimalPoint());
        DecimalFormat myFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        if (Locale.getDefault().getDisplayLanguage().equals("Deutsch")) {
            myFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.GERMAN);
        }
        DecimalFormatSymbols symbols = myFormatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        myFormatter.setDecimalFormatSymbols(symbols);

        myFormatter.setRoundingMode(RoundingMode.FLOOR);
        StringBuilder sb = new StringBuilder();
        if (DecimalPointNumber > 0) {
            for (int i = 0; i < DecimalPointNumber; i++) {
                sb.append("#");
            }
            myFormatter.applyPattern("###,###." + sb.toString());
        } else
            myFormatter.applyPattern("###,###" + sb.toString());
        if (Locale.getDefault().getDisplayLanguage().equals("Deutsch"))
            return getCurrency().getSymbol() + " " + myFormatter.format(value);
        else
            return myFormatter.format(value);
    }

    public static String formatPriceWithoutSymbol(double value) {
        int DecimalPointNumber = ServiceTools.toInt(BaseActivity.getPrefPriceDecimalPoint());
        Locale locale = Locale.getDefault();
        DecimalFormat myFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        //myFormatter.setRoundingMode(RoundingMode.FLOOR);
        StringBuilder sb = new StringBuilder();
        if (DecimalPointNumber > 0) {
            for (int i = 0; i < DecimalPointNumber; i++) {
                sb.append("#");
            }
            myFormatter.applyPattern("###,###." + sb.toString());
        } else
            myFormatter.applyPattern("###,###" + sb.toString());
        return myFormatter.format(value);
    }

    public static String formatOff(double value) {
        int DecimalPointNumber = 2;
        Locale locale = Locale.getDefault();
        DecimalFormat myFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        //myFormatter.setRoundingMode(RoundingMode.FLOOR);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DecimalPointNumber; i++) {
            sb.append("#");
        }
        myFormatter.applyPattern("###,###." + sb.toString());
        return myFormatter.format(value);
    }

    public static int getPrefDefPrice(DbAdapter db , int CustomerId , long GroupId ) {
        int defVisitor = db.getDefVisitorPriceLevel();
        int defCustomer = db.getDefCustomerPriceLevel(CustomerId);
        int defGroupCustomer = db.getDefGroupCustomerPriceLevel(GroupId);
        if(defVisitor != 0)
            return defVisitor;
        else if (defCustomer != 0)
            return defCustomer;
        else return defGroupCustomer;
    }

    private double roundDouble(double d) {
        BigDecimal bd = null;
        try {
            bd = new BigDecimal(Double.toString(d));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
        if (bd != null) {
            return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return 0;
    }

    private static Currency getCurrency() {
        return Currency.getInstance(Locale.GERMANY);
    }

    public static String MoneyFormatToNumber(String amountParam) {
        if (Locale.getDefault().getLanguage().equals("fa")) {
            amountParam = convertToEnglishDigits(amountParam);
        }
        if (amountParam.contains(getCurrency().getSymbol())) {
            amountParam = amountParam.replace(getCurrency().getSymbol() + " ", "");
        }
        StringBuilder str = new StringBuilder();
        if (String.valueOf(getGroupingSeparator()).equals(".")) {
            String[] list = amountParam.split("\\.");
            for (String aList : list) {
                str.append(aList);
            }
            return str.toString();
        } else if (String.valueOf(getGroupingSeparator()).equals(",")) {
            String[] list = amountParam.split(",");
            for (String aList : list) {
                str.append(aList);
            }
            return str.toString();
        }
        return amountParam;
    }

    public static char getGroupingSeparator() {
        if (Locale.getDefault().getLanguage().equals("fa")) {
            return ',';
        }
        return DecimalFormatSymbols.getInstance().getGroupingSeparator();
    }

    public static char getDecimalSeparator() {
        return DecimalFormatSymbols.getInstance().getDecimalSeparator();
    }

    public static String getMonthNameForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }

    public static String getWeekDayForInt(int num) {
        String weekDayName = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] weekdays = dfs.getWeekdays();
        if (num >= 0 && num <= 7) {
            weekDayName = weekdays[num];
        }
        return weekDayName;
    }

    public static String getDateForLong(Context context, long date) {
        int day;
        String DayofWeek = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SATURDAY:
                DayofWeek = context.getResources().getString(R.string.str_saturday);
                break;
            case Calendar.SUNDAY:
                DayofWeek = context.getResources().getString(R.string.str_sunday);
                break;
            case Calendar.MONDAY:
                DayofWeek = context.getResources().getString(R.string.str_monday);
                break;
            case Calendar.TUESDAY:
                DayofWeek = context.getResources().getString(R.string.str_tuesday);
                break;
            case Calendar.WEDNESDAY:
                DayofWeek = context.getResources().getString(R.string.str_wednesday);
                break;
            case Calendar.THURSDAY:
                DayofWeek = context.getResources().getString(R.string.str_thursday);
                break;
            case Calendar.FRIDAY:
                DayofWeek = context.getResources().getString(R.string.str_friday);
                break;
        }

        if (Locale.getDefault().getLanguage().equals("de")) {
            return DayofWeek + ", " + calendar.get(Calendar.DAY_OF_MONTH) + ". " + ServiceTools.getMonthNameForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
        } else {
            CivilDate cv = new CivilDate();
            cv.setCalendar(calendar);
            PersianDate persian = DateConverter.civilToPersian(cv);
            return DayofWeek + " " + persian.getDayOfMonth() + " " + persian.getMonthName() + " " + persian.getYear();
        }

    }

    public static final int TYPE_ORDER = 203;
    public static final int TYPE_INVOCIE = 201;
    public static final int TYPE_Delivery = 299;
    public static final int TYPE_SEND_TRANSFERENCE = 3;
    public static final int TYPE_RECEIVE_TRANSFERENCE = 4;
    public static final int TYPE_RETURN_OF_SALE = 202;

    public static String getOrderTitle(int OrderType, Context context) {

        if (BaseActivity.getTitleStatus(context)) {
            if (!BaseActivity.getTitleText(context).trim().equals("")) {
                return BaseActivity.getTitleText(context);
            }
        }

        switch (OrderType) {
            case 203:
                return context.getString(R.string.str_type_past_invoice);
            case 201:
                return context.getString(R.string.str_type_invoice);
            case 202:
                return context.getString(R.string.return_of_sale);
        }
        return "";
    }

    public static String getDateAndTimeForLong(long date) {
        int day;
        String DayofWeek = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (Locale.getDefault().getLanguage().equals("de")) {
            return calendar.get(Calendar.DAY_OF_MONTH) + "." + month + "." + calendar.get(Calendar.YEAR) + " - " + hour + ":" + minute;
        } else {
            CivilDate cv = new CivilDate();
            cv.setCalendar(calendar);
            PersianDate persian = DateConverter.civilToPersian(cv);
            return persian.getYear() + "/" + persian.getMonth() + "/" + persian.getDayOfMonth() + " - " + hour + ":" + minute;
        }

    }

    public static String getDateForLong(long date) {
        int day;
        String DayofWeek = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (Locale.getDefault().getLanguage().equals("de")) {
            return calendar.get(Calendar.DAY_OF_MONTH) + "." + month + "." + calendar.get(Calendar.YEAR);
        } else {
            CivilDate cv = new CivilDate();
            cv.setCalendar(calendar);
            PersianDate persian = DateConverter.civilToPersian(cv);

            String monthStr = String.valueOf(persian.getMonth());
            String getDayOfMonthStr = String.valueOf(persian.getDayOfMonth());
            if (monthStr.length() == 1)
                monthStr = "0" + monthStr;
            if (getDayOfMonthStr.length() == 1)
                getDayOfMonthStr = "0" + getDayOfMonthStr;

            return persian.getYear() + "/" + monthStr + "/" + getDayOfMonthStr;
        }

    }

    public static String getFileName(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int month = calendar.get(Calendar.MONTH) + 1;
        if (Locale.getDefault().getLanguage().equals("de")) {
            return calendar.get(Calendar.DAY_OF_MONTH) + "_" + month + "_" + calendar.get(Calendar.YEAR) + " _" + hour + "-" + minute + "-" + second;
        } else {
            CivilDate cv = new CivilDate();
            cv.setCalendar(calendar);
            PersianDate persian = DateConverter.civilToPersian(cv);
            return persian.getYear() + "_" + persian.getMonth() + "_" + persian.getDayOfMonth() + " _ " + hour + "-" + minute + "-" + second;
        }

    }

    public static String GetMoneyFormat(String amountParam) {
        if (Locale.getDefault().getLanguage().equals("fa")) {
            amountParam = convertToEnglishDigits(amountParam);
        }
        String strText = amountParam.replace(",", "");
        int strTextlen = strText.length();
        String Result = "";
        if (strTextlen > 3) {
            int point = 0;
            for (int j = 1; j <= (strText.length() / 3); j++) {
                point = strText.length() - (j * 3);
                String str = strText.substring(point, strTextlen);
                Result = str + Result;
                if (strTextlen > 0)
                    Result = "," + Result;
                strTextlen -= 3;
            }
        }
        Result = strText.substring(0, strTextlen) + Result;
        if (Result.startsWith(",")) {
            Result = Result.substring(1);
        } else if (Result.startsWith("-,")) {
            Result = Result.substring(2);
            Result = "-" + Result;
        }

        return Result;
    }


    public static int getCheckListTypeIcon(int Type) {
        switch (Type) {
            case 1: //CHECKLIST_TYPE_Order
                return R.drawable.ic_visit_set_checklist;
            case 2://CHECKLIST_TYPE_Delivery
                return R.drawable.ic_truck_set_checklist;
            case 3://CHECKLIST_TYPE_RECEIPT
                return R.drawable.ic_settlement_set_checklist;
            default:
                return R.drawable.ic_truck_set_checklist;
        }
    }

    public static String getGenerationCode() {
        String Code = "";
        Calendar NowCalendar = Calendar.getInstance();
        Calendar OutCalendar = Calendar.getInstance();
        CivilDate NowCivil = new CivilDate();
        CivilDate OutCivil = new CivilDate();
        long NowTime = 0, OutTime = 0, FinalTime, UserId;

        NowTime = NowCalendar.getTimeInMillis() / 1000;
        UserId = BaseActivity.getPrefUserMasterId();
        NowCivil.setCalendar(NowCalendar);
        PersianDate persian = DateConverter.civilToPersian(NowCivil);
        persian.setMonth(1);
        persian.setDayOfMonth(1);
        OutCivil = DateConverter.persianToCivil(persian);
        OutCalendar = OutCivil.getCalendar();
        OutTime = OutCalendar.getTimeInMillis() / 1000;

        FinalTime = NowTime - OutTime;
        Code = String.valueOf(UserId) + FinalTime;

        String Year = String.valueOf(persian.getYear());
        String Month = String.valueOf(persian.getMonth());
        String Day = String.valueOf(persian.getDayOfMonth());
        Log.e("Date", Year + "/" + Month + "/" + Day);
        return Code;
//
//		Random random = new Random();
//		int res = random.nextInt();
//		return String.valueOf(res);
    }

    public static String GenerationCodeOrderProperty() {
        String Code = "";
        Calendar NowCalendar = Calendar.getInstance();
        Calendar OutCalendar = Calendar.getInstance();
        CivilDate NowCivil = new CivilDate();
        CivilDate OutCivil = new CivilDate();
        long NowTime = 0, OutTime = 0, FinalTime, UserId;

        NowTime = NowCalendar.getTimeInMillis() / 1000;
        UserId = BaseActivity.getPrefUserMasterId();
        NowCivil.setCalendar(NowCalendar);
        PersianDate persian = DateConverter.civilToPersian(NowCivil);
        persian.setMonth(1);
        persian.setDayOfMonth(1);
        OutCivil = DateConverter.persianToCivil(persian);
        OutCalendar = OutCivil.getCalendar();
        OutTime = OutCalendar.getTimeInMillis() / 1000;

        FinalTime = NowTime - OutTime;
        Code = String.valueOf(FinalTime) + UserId;

        String Year = String.valueOf(persian.getYear());
        String Month = String.valueOf(persian.getMonth());
        String Day = String.valueOf(persian.getDayOfMonth());
        Log.e("Date", Year + "/" + Month + "/" + Day);
        return Code;
//
//		Random random = new Random();
//		int res = random.nextInt();
//		return String.valueOf(res);
    }


    public static double getTax(Product product) {
        if (product.getTaxPercent() == -1)
            return 0;
        else if (product.getTaxPercent() == 0)
            return RegulartoDouble(BaseActivity.getPrefTaxPercent());
        else
            return product.getTaxPercent();

    }

    public static double getCharge(Product product) {
        if (product.getChargePercent() == -1)
            return 0;
        else if (product.getChargePercent() == 0)
            return RegulartoDouble(BaseActivity.getPrefChargePercent());
        else
            return product.getChargePercent();

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        if(listAdapter.getCount()>0){
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() * listAdapter.getCount();
        }
        /*for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }*/

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setListViewChequeHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + 25 + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static String getDeviceID(Context context) {
        String DeviceId = "000000000000000";
        try {
            DeviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.getMessage();
        }
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (!telephonyManager.getDeviceId().equals("000000000000000"))
                DeviceId = telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return DeviceId;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean checkUserID(Context context) {
        DbAdapter db = new DbAdapter(context);
        Calendar calNow = Calendar.getInstance();
        boolean check = false;

        db.open();
        User user = null;

        try {
            user = db.getUser();
            if (user != null) {
                check = true;

                Calendar calLogin = Calendar.getInstance();
                calLogin.setTimeInMillis(user.getLoginDate());

                check = calLogin.get(Calendar.DAY_OF_YEAR) == calNow.get(Calendar.DAY_OF_YEAR);

            } else {
                check = false;
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        db.close();

        return check;
    }

    public static double getSumCount1(int productId, Context context) {
        DbAdapter db = new DbAdapter(context);
        db.open();
        double sum = 0;
        ArrayList<ProductDetail> productDetails = db.getAllProductDetail(productId);
        for (ProductDetail productDetail : productDetails) {
            sum += productDetail.getCount1();
        }
        db.close();
        return sum;

    }
    public static double getSumCount11(ArrayList<ProductDetail> productDetails) {
        if(productDetails.size() == 1)
            return productDetails.get(0).getCount1();
        double sum = 0;
        for (ProductDetail productDetail : productDetails) {
            sum += productDetail.getCount1();
        }
        return sum;

    }

    public static double getSumCount2(int productId, Context context) {
        DbAdapter db = new DbAdapter(context);
        db.open();
        double sum = 0;
        ArrayList<ProductDetail> productDetails = db.getAllProductDetail(productId);
        for (ProductDetail productDetail : productDetails) {
            sum += productDetail.getCount2();
        }
        db.close();
        return sum;

    }
    public static double getSumCount12(ArrayList<ProductDetail> productDetails) {
        if(productDetails.size() == 1)
            return productDetails.get(0).getCount2();
        double sum = 0;
        for (ProductDetail productDetail : productDetails) {
            sum += productDetail.getCount2();
        }
        return sum;

    }




    public static double getSumCount1prop(int productId, ArrayList<OrderDetailProperty> orderDetailProperties, Context context) {
        DbAdapter db = new DbAdapter(context);
        db.open();
        double sum = 0;
        ArrayList<ProductDetail> productDetails = db.getAllProductDetail(productId);
        for (ProductDetail productDetail : productDetails) {
            sum += productDetail.getCount1();
        }
        for (OrderDetailProperty orderDetailProperty : orderDetailProperties) {
            sum += orderDetailProperty.getSumCountBaJoz();
        }
        db.close();
        return sum;

    }

    public static double getSumGiftCount12(double giftCount1, double giftCount2, Context context) {
        if (BaseActivity.getPrefUnit2Setting(context) == BaseActivity.MODE_MeghdarJoz)
            return giftCount1 + giftCount2;
        else
            return giftCount1;
    }

    public static double getExistCount1(OrderDetail orderDetail, ProductDetail productDetail) {
        return orderDetail.getSumCountBaJoz() + productDetail.getCount1() + orderDetail.getGiftCount1();
    }

    public static double getExistCount2(OrderDetail orderDetail, ProductDetail productDetail) {
        return orderDetail.getCount2() + productDetail.getCount2() + orderDetail.getGiftCount2();
    }

    public static double getExistCount1Prop(OrderDetailProperty orderDetailProperty, ProductDetail productDetail) {
        return orderDetailProperty.getSumCountBaJoz() + productDetail.getCount1();
    }

    public static double getExistCount2Prop(OrderDetailProperty orderDetailProperty, ProductDetail productDetail) {
        return orderDetailProperty.getCount2() + productDetail.getCount2();
    }

    public static double getExistSumCountProp(OrderDetailProperty orderDetailProperty, ProductDetail productDetail) {
        return orderDetailProperty.getSumCountBaJoz() + productDetail.getCount2();
    }



    public static String computeMD5Hash(String password) {
        StringBuffer MD5Hash = null;
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte[] messageDigest = digest.digest();

            MD5Hash = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                MD5Hash.append(h);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return MD5Hash.toString();

    }

    public static LatLng getLatLang(Context mContext) {
        GPSTracker gpsTracker;
        double Latitude = 0;
        double Longitude = 0;
        gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.canGetLocation()) {
            Latitude = gpsTracker.getLatitude();
            Longitude = gpsTracker.getLongitude();
        }
        return new LatLng(Latitude, Longitude);
    }

    public static void setSettingPreferences(DbAdapter db, Context mContext) {

        BaseActivity.setPrefUnit2Setting(BaseActivity.MODE_YekVahedi);
        BaseActivity.setPrefTaxAndChargeIsActive(BaseActivity.InActive);
        BaseActivity.setPrefTaxPercent(BaseActivity.InActive);
        BaseActivity.setPrefChargePercent(BaseActivity.InActive);
        BaseActivity.setPrefRowDiscountIsActive(BaseActivity.invisible);
        BaseActivity.setPrefAutoSyncValue(BaseActivity.InActive);

        db.open();
        ArrayList<Setting> settings = db.getAllSettings();

        BaseActivity.setPrefRowDiscountIsActive(BaseActivity.invisible);
        for (int i = 0; i < settings.size(); i++) {
            switch (settings.get(i).getSettingCode()) {
                case BaseActivity.TwoUnitKolJozCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue().equals(BaseActivity.Active))
                        BaseActivity.setPrefUnit2Setting(BaseActivity.MODE_MeghdarJoz);
                    break;
                case BaseActivity.TwoUnitActiveCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue().equals(BaseActivity.Active))
                        BaseActivity.setPrefUnit2Setting(BaseActivity.Mode_DoVahedi);
                    break;
                case BaseActivity.OneUnitActiveCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue().equals(BaseActivity.Active))
                        BaseActivity.setPrefUnit2Setting(BaseActivity.MODE_YekVahedi);
                    break;
                case BaseActivity.SHOW_ROW_DISCOUNT:
                    if (settings.get(i).getDeleted() != 1)
                        BaseActivity.setPrefRowDiscountIsActive(settings.get(i).getValue());
                    else
                        BaseActivity.setPrefRowDiscountIsActive(BaseActivity.invisible);
                    break;
                case BaseActivity.APPLY_DISCOUNT:
                    if (settings.get(i).getDeleted() != 1)
                        BaseActivity.setPrefApplyRowDiscount(settings.get(i).getValue());
                    else
                        BaseActivity.setPrefApplyRowDiscount(BaseActivity.InActive);
                    break;
                case BaseActivity.TaxAndChargeIsActiveCode:
                    if (settings.get(i).getDeleted() != 1)
                        BaseActivity.setPrefTaxAndChargeIsActive(settings.get(i).getValue());
                    else
                        BaseActivity.setPrefTaxAndChargeIsActive(BaseActivity.InActive);
                    break;
                case BaseActivity.TaxPercentCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue() != null)
                        BaseActivity.setPrefTaxPercent(settings.get(i).getValue());
                    else
                        BaseActivity.setPrefTaxPercent(BaseActivity.InActive);
                    break;
                case BaseActivity.ChargePercentCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue() != null)
                        BaseActivity.setPrefChargePercent(settings.get(i).getValue());
                    else
                        BaseActivity.setPrefChargePercent(BaseActivity.InActive);
                    break;
                case BaseActivity.AutoSyncCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue() != null) {
                        BaseActivity.setPrefAutoSyncValue(settings.get(i).getValue());
                        ServiceTools.scheduleAlarm(mContext);
                    } else
                        BaseActivity.setPrefAutoSyncValue(BaseActivity.InActive);
                    break;
                case BaseActivity.CountDecimalPointCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue() != null) {
                        String value = settings.get(i).getValue();
                        BaseActivity.setPrefCountDecimalPoint(value.substring(0, value.indexOf(".")));
                    } else
                        BaseActivity.setPrefCountDecimalPoint("0");
                    break;
                case BaseActivity.PriceDecimalPointCode:
                    if (settings.get(i).getDeleted() != 1 && settings.get(i).getValue() != null) {
                        String value = settings.get(i).getValue();
                        BaseActivity.setPrefPriceDecimalPoint(value.substring(0, value.indexOf(".")));
                    } else
                        BaseActivity.setPrefPriceDecimalPoint("0");
                    break;
            }
        }

    }

    public static String getFaDayofWeek(String day) {
        if (day.equals("Sat"))
            day = "شنبه";
        else if (day.equals("Sun"))
            day = "یکشنبه";
        else if (day.equals("Mon"))
            day = "دوشنبه";
        else if (day.equals("Tue"))
            day = "سه شنبه";
        else if (day.equals("Wed"))
            day = "چهارشنبه";
        else if (day.equals("Thu"))
            day = "پنج شنبه";
        else if (day.equals("Fri"))
            day = "جمعه";
        return day;
    }

    public static boolean checkDate(String strDateServer) {

        Date dateNow = new Date();

        String format = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";
        SimpleDateFormat simpleDate = new SimpleDateFormat(format, Locale.US);
        String strNowDate = simpleDate.format(dateNow);
        DateFormat timeDf = new SimpleDateFormat("hh:mm:ss");
        long diff = 0;

        String[] arrayServerDate = strDateServer.split("T");
        String[] arrayNowDate = strNowDate.split("T");

        if (arrayNowDate[0].equals(arrayServerDate[0])) {

            String[] arrayServerTimeZone = arrayServerDate[1].split("\\.");
            String[] arrayNowTimeZone = arrayNowDate[1].split("\\.");

            String[] STimeZone = arrayServerTimeZone[1].split("\\+");
            String[] NowTimeZone = arrayNowTimeZone[1].split("\\+");

            String[] arrayServerTime = arrayServerTimeZone[0].split(":");
            String[] arrayNowTime = arrayNowTimeZone[0].split(":");

            if (STimeZone[1].equals(NowTimeZone[1])) {
                try {
                    diff = timeDf.parse(arrayServerTimeZone[0]).getTime() - timeDf.parse(arrayNowTimeZone[0]).getTime();
                } catch (ParseException e) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().recordException(e);
                    e.printStackTrace();
                }
                if (ServiceTools.toInt(arrayServerTime[1]) == ServiceTools.toInt(arrayNowTime[1]))
                    return true;

                return diff <= 600000 && diff >= -600000;
            }
        }//End Of If Date
        return true;
    }

    public static boolean checkDate2(long serverTime) {
        DateTime nowTehran = new DateTime(DateTimeZone.forID("Asia/Tehran"));
        long lTehran = nowTehran.getMillis();
        long diff = lTehran - serverTime;
        return diff <= 780000 && diff >= -780000;
    }


    public static long getOffset() {

        TimeZone tehranZone = TimeZone.getTimeZone("Asia/Tehran");
        TimeZone localZone = TimeZone.getDefault();

        Calendar tehranCal = Calendar.getInstance(tehranZone);

        Calendar localCal = Calendar.getInstance(localZone);

        return tehranCal.getTimeInMillis() - localCal.getTimeInMillis();
    }

    public static double getCalculateFinalPrice(OrderDetail object, Context mContext) {

        DbAdapter db = new DbAdapter(mContext);
        db.open();


        double count = object.getSumCountBaJoz();
        double off = 0;
        double Tax = 0;
        double Charge = 0;
        double finalPrice = object.getPrice();
        double offValue = (object.getDiscount());
        double TaxPercent = (object.getTaxPercent());
        double ChargePercent = (object.getChargePercent());
        finalPrice = (count * finalPrice);
        off = (offValue * 1);
        finalPrice = finalPrice - off;
        Tax = ((finalPrice * TaxPercent) / 100);
        Charge = ((finalPrice * ChargePercent) / 100);
        double TaxAndCharge = Tax + Charge;
        finalPrice = finalPrice + TaxAndCharge;
        return finalPrice;
    }

    public static double getTotalCount(OrderDetail object) {

        double count = object.getSumCountBaJoz();
        return count;
    }

    public static double getPriceFromPriceLevel(int productDetailid, Context mContext) {
        DbAdapter db = new DbAdapter(mContext);
        db.open();
        double price = 0;
        ProductDetail productDetail = db.getProductDetail(productDetailid);

        switch (productDetail.getDefaultSellPriceLevel()) {
            case 1:
                price = productDetail.getPrice1();
                break;
            case 2:
                price = productDetail.getPrice2();
                break;
            case 3:
                price = productDetail.getPrice3();
                break;
            case 4:
                price = productDetail.getPrice4();
                break;
            case 5:
                price = productDetail.getPrice5();
                break;
            case 6:
                price = productDetail.getPrice6();
                break;
            case 7:
                price = productDetail.getPrice7();
                break;
            case 8:
                price = productDetail.getPrice8();
                break;
            case 9:
                price = productDetail.getPrice9();
                break;
            case 10:
                price = productDetail.getPrice10();
                break;
        }
        return price;
    }
    public static double getPriceFromPriceLevel2(ProductDetail productDetail) {
        double price = 0;
        switch (productDetail.getDefaultSellPriceLevel()) {
            case 1:
                price = productDetail.getPrice1();
                break;
            case 2:
                price = productDetail.getPrice2();
                break;
            case 3:
                price = productDetail.getPrice3();
                break;
            case 4:
                price = productDetail.getPrice4();
                break;
            case 5:
                price = productDetail.getPrice5();
                break;
            case 6:
                price = productDetail.getPrice6();
                break;
            case 7:
                price = productDetail.getPrice7();
                break;
            case 8:
                price = productDetail.getPrice8();
                break;
            case 9:
                price = productDetail.getPrice9();
                break;
            case 10:
                price = productDetail.getPrice10();
                break;
        }
        return price;
    }

    public static String getPriceFormated(int productDetailid, Context mContext) {
        DbAdapter db = new DbAdapter(mContext);
        db.open();
        double price = 0;
        ProductDetail productDetail = db.getProductDetail(productDetailid);

        switch (productDetail.getDefaultSellPriceLevel()) {
            case 1:
                price = productDetail.getPrice1();
                break;
            case 2:
                price = productDetail.getPrice2();
                break;
            case 3:
                price = productDetail.getPrice3();
                break;
            case 4:
                price = productDetail.getPrice4();
                break;
            case 5:
                price = productDetail.getPrice5();
                break;
            case 6:
                price = productDetail.getPrice6();
                break;
            case 7:
                price = productDetail.getPrice7();
                break;
            case 8:
                price = productDetail.getPrice8();
                break;
            case 9:
                price = productDetail.getPrice9();
                break;
            case 10:
                price = productDetail.getPrice10();
                break;
        }
        return formatPrice(price);
    }


    public static double getCalculateFinalPrice(ProductInDeliveryOrder object) {

        double count = object.getCount();
        double Tax;
        double Charge;
        double Price = ServiceTools.toDouble(object.getPrice());
        double OffPercent = ServiceTools.toFloat(object.getOffPercent());
        double TaxPercent = ServiceTools.toFloat(object.getTaxPercent());
        double ChargePercent = ServiceTools.toFloat(object.getChargePercent());
        Price = (count * Price);
        double Discount = ((Price * OffPercent) / 100);
        Price = Price - Discount;
        Tax = ((Price * TaxPercent) / 100);
        Charge = ((Price * ChargePercent) / 100);
        double TaxAndCharge = Tax + Charge;
        Price = Price + TaxAndCharge;
        return Price;
    }

    public static double getCalculateDeliveryFinalPrice(OrderDetail object) {

        double off = 0;
        double Tax = 0;
        double Charge = 0;
        double count = (object.getSumCountBaJoz());
        double Price = object.getPrice();
        double offValue = object.getDiscount();
        double TaxPercent = object.getTaxPercent();
        double ChargePercent = object.getChargePercent();
        Price = (count * Price);
        off = (offValue * 1);
        Price = Price - off;
        Tax = ((Price * TaxPercent) / 100);
        Charge = ((Price * ChargePercent) / 100);
        double TaxAndCharge = Tax + Charge;
        Price = Price + TaxAndCharge;
        return Price;
    }

    public static String getDayofWeekPersian(String day) {
        if (day.equals("Sat") || day.equals("SAT"))
            day = "شنبه";
        else if (day.equals("Sun") || day.equals("SUN"))
            day = "یکشنبه";
        else if (day.equals("Mon") || day.equals("MON"))
            day = "دوشنبه";
        else if (day.equals("Tue") || day.equals("TUE"))
            day = "سه شنبه";
        else if (day.equals("Wed") || day.equals("WED"))
            day = "چهارشنبه";
        else if (day.equals("Thu") || day.equals("THU"))
            day = "پنجشنبه";
        else if (day.equals("Fri") || day.equals("FRI"))
            day = "جمعه";
        return day;
    }

    public static double getRowOffPercent(double off, double price, double sumCountBaJoz) {
        return off * 100 / (price * sumCountBaJoz);
    }

    public static double getRowOff(double percentOff, double price, double sumCountBaJoz) {
        return ((percentOff * price * sumCountBaJoz) / 100);
    }

    public static String replaceWithArabic(String value) {

        String Value = value;

        String[][] simillarChars = {
                {"ي", "ی"},
                {"١", "۱"},
                {"٢", "۲"},
                {"٣", "۳"},
                {"٤", "۴"},
                {"٥", "۵"},
                {"٦", "۶"},
                {"٧", "۷"},
                {"٨", "۸"},
                {"٩", "۹"},
                {"٠", "۰"}
        };

        for (int i = 0; i < value.length(); i++) {
            char CH_in = value.charAt(i);
            for (String[] group : simillarChars)
                for (String ch : group) {
                    if (CH_in == ch.charAt(0)) {
                        Value = Value.replace(CH_in, group[0].charAt(0));
                        break;
                    }
                }
        }

        return Value;
    }
    public static String replaceWithEnglish(String value) {

        String Value = value;

        String[][] simillarChars = {
                {"ي", "ی"},
                {"1", "۱"},
                {"2", "۲"},
                {"3", "۳"},
                {"4", "۴"},
                {"5", "۵"},
                {"6", "۶"},
                {"7", "۷"},
                {"8", "۸"},
                {"9", "۹"},
                {"0", "۰"}
        };

        for (int i = 0; i < value.length(); i++) {
            char CH_in = value.charAt(i);
            for (String[] group : simillarChars)
                for (String ch : group) {
                    if (CH_in == ch.charAt(0)) {
                        Value = Value.replace(CH_in, group[0].charAt(0));
                        break;
                    }
                }
        }

        return Value;
    }

    public static boolean checkArabic(String value) {

        boolean check = false;

        String[][] simillarChars = {
                {"ي", "ی"},
                {"ك", "ک"},
                {"١", "۱"},
                {"٢", "۲"},
                {"٣", "۳"},
                {"٤", "۴"},
                {"٥", "۵"},
                {"٦", "۶"},
                {"٧", "۷"},
                {"٨", "۸"},
                {"٩", "۹"},
                {"٠", "۰"}
        };

        for (int i = 0; i < value.length(); i++) {
            char CH_in = value.charAt(i);
            for (String[] group : simillarChars)
                for (String ch : group) {
                    if (CH_in == ch.charAt(0)) {
                        check = true;
                    }
                }
        }

        return check;
    }

    public static boolean CheckContainsWithSimillar(String value, String searchStr) {
        if(searchStr == null)
            return false;
        searchStr = searchStr.toLowerCase();
        if (Locale.getDefault().getLanguage().equals("de")) {
            return searchStr.contains(value);
        } else {
            String Value = value.trim();
            String Hystack = searchStr;

            String[][] simillarChars = {
                    {"ی", "ي"},
                    {"ک", "ك"},
                    {"۱", "١", "1"},
                    {"۲", "٢", "2"},
                    {"۳", "٣", "3"},
                    {"۴", "٤", "4"},
                    {"۵", "٥", "5"},
                    {"۶", "٦", "6"},
                    {"۷", "٧", "7"},
                    {"۸", "٨", "8"},
                    {"۹", "٩", "9"},
                    {"۰", "٠", "0"}
            };

            for (int i = 0; i < value.length(); i++) {
                char CH_in = value.charAt(i);
                for (String[] group : simillarChars)
                    for (String ch : group) {
                        if (CH_in == ch.charAt(0)) {
                            Value = Value.replace(CH_in, group[0].charAt(0));
                            break;
                        }
                    }
            }

            for (int i = 0; i < searchStr.length(); i++) {
                char CH_in = searchStr.charAt(i);
                for (String[] group : simillarChars)
                    for (String ch : group) {
                        if (CH_in == ch.charAt(0)) {
                            Hystack = Hystack.replace(CH_in, group[0].charAt(0));
                            break;
                        }
                    }
            }
            Hystack = Hystack.replaceAll("\\s+", "");
            Value = Value.replaceAll("\\s+", "");
            return Hystack.contains(Value);
        }
    }

    public static Bitmap getPrintLogo() {
        String fPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_IMAGES + "/" + ProjectInfo.DIRECTORY_ASSETS;
        String sdPath = Environment.getExternalStorageDirectory().toString() + "/" + fPath + "/" + ProjectInfo.PRINT_LOGO_FILE_NAME;
        Bitmap b = BitmapFactory.decodeFile(sdPath);
        return b;
    }

    public static Bitmap mergeBitmap(Bitmap bmp1, Bitmap bmp2, int width) {
        Bitmap bmOverlay = Bitmap.createBitmap(width, bmp1.getHeight() + bmp2.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, bmp1.getHeight(), null);
        return bmOverlay;
    }

    public static int dpToPX(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static String correctFarsiText(String str) {
        if (str == null)
            return "";
        return str.replace('ي', 'ی').replace('ي', 'ى').replace('ك', 'ک');
    }

    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            // TODO: handle exception
        }
        return 1;
    }

    public static void setKeyInSharedPreferences(Context context, String key, String value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (sharedpreferences == null)
            return;
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static String getKeyFromSharedPreferences(Context context, String key) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (sharedpreferences != null && sharedpreferences.contains(key)) {
            return sharedpreferences.getString(key, "");
        }
        return "";
    }

    public static ImageLoadingListener getImageLoaderListener(final int defaultImage) {
        return new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                if (view instanceof ImageView)
                    ((ImageView) view).setImageResource(defaultImage);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (view instanceof ImageView)
                    view.setBackgroundResource(R.drawable.image_white_box);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                if (view instanceof ImageView)
                    ((ImageView) view).setImageResource(defaultImage);
            }
        };
    }

    public static RequestListener<String, GlideDrawable> getGlideListener(final ImageView ivPic) {
        return new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                ivPic.setImageResource(R.drawable.img_default_product);
                ivPic.setBackgroundResource(R.drawable.image_empty_box);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                ivPic.setBackgroundResource(R.drawable.image_white_box);
                return false;
            }
        };
    }

    public static RequestListener<File, GlideDrawable> getGlideFileListener(final ImageView ivPic) {
        return new RequestListener<File, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, File file, Target<GlideDrawable> target, boolean b) {
                ivPic.setImageResource(R.drawable.img_default_product);
                ivPic.setBackgroundResource(R.drawable.image_empty_box);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, File file, Target<GlideDrawable> target, boolean b, boolean b1) {
                ivPic.setBackgroundResource(R.drawable.image_white_box);
                return false;
            }

        };
    }

    public static void scheduleAlarm(Context context) {

        String value = BaseActivity.getPrefAutoSyncValue();
        double doubleValue = ServiceTools.toDouble(value);
        long INTERVAL_MINUTES = (long) doubleValue * 60 * 1000;
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(context, SyncAlarmReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, SyncAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        if (alarm != null) {
            if (INTERVAL_MINUTES > 0) {
                alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                        INTERVAL_MINUTES, pIntent);
            } else {
                alarm.cancel(pIntent);
            }
        }

    }

    public static long toLong(String value) {
        if (value != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                value = convertToEnglishDigits(value);
            }
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                return 0;
            }
        }
        return 0;
    }

    public static int toInt(String value) {
        if (value != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                value = convertToEnglishDigits(value);
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                return 0;
            }
        }
        return 0;
    }

    public static double RegulartoDouble(String value) {
        if (value != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                value = convertToEnglishDigits(value);
            }
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                return 0;
            }
        }
        return 0;
    }

    public static double toFloat(String value) {
        if (value != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                value = convertToEnglishDigits(value);
            }
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException e) {
                FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                FirebaseCrashlytics.getInstance().recordException(e);
                return 0;
            }
        }
        return 0;
    }

    public static double toDouble(String value) {
        if (value != null) {
            if (Locale.getDefault().getLanguage().equals("fa")) {
                value = convertToEnglishDigits(value);
            }
            if (value.contains(",") || value.contains(".")) {
                value = MoneyFormatToNumber(value);
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                    try {
                        /*Number number = format.parse(value);
                        return number.doubleValue();*/
                        return Double.parseDouble(value);
                    } catch (Exception e) {
                        FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                        FirebaseCrashlytics.getInstance().recordException(e);
                        e.printStackTrace();
                    }
                } catch (NumberFormatException e) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().recordException(e);
                    return 0;
                }
            } else {
                try {
                    if (value.contains(getCurrency().getSymbol())) {
                        value = value.replace(getCurrency().getSymbol() + " ", "");
                    }
                    return Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
                    FirebaseCrashlytics.getInstance().recordException(e);
                    return 0;
                }
            }
        }
        return 0;
    }

    /*public static double toDouble(String value) {
        if(value!=null){
            value = MoneyFormatToNumber(value);
            NumberFormat numberFormat = NumberFormat.getInstance();
            try {
                Number number = numberFormat.parse(value);
                return number.doubleValue();
            } catch (ParseException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
FirebaseCrashlytics.getInstance().recordException(e);
                e.printStackTrace();
            }
        }
        return 0;
    }*/

    public static String toString(double value) {
        double tmp = value % 1;
        if (tmp > 0)
            return String.format("%.2f", value);
        return String.valueOf(value);
    }

    public static boolean isNull(String value) {
        return value == null || value.equals("") || value.toLowerCase().equals("null");
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return false;
    }

    public static String ConvertTimestampToPersianFormat(Context ctx, long lDate) {
        StringBuilder strOutput = new StringBuilder();

        String output = "";
        Date date = new Date(lDate);
        Date now = new Date();
        long diff = date.getTime() - now.getTime();
        if (diff < 0)
            diff = diff * -1;
        long diffByHour = diff / (60 * 60 * 1000);
        long diffByMin = diff / (60 * 1000);
        long diffBySec = diff / 1000;
        if (diffByMin < 1)
            output = diffBySec + " " + ctx.getString(R.string.SecondsAgo);
        else if (diffByHour < 1)
            output = diffByMin + " " + ctx.getString(R.string.MinutesAgo);
        else if (diffByHour < 23)
            output = diffByHour + " " + ctx.getString(R.string.HoursAgo);
        else if (diffByHour >= 23 && diffByHour < 47)
            output = ctx.getString(R.string.Yesterday);
        else {
            CivilDate cDate = new CivilDate(lDate);
            PersianDate pDate = DateConverter.civilToPersian(cDate);
            //output = date.getHours() + ":" + date.getMinutes() + " " + pDate.getDayOfMonth() + " " +  pDate.getMonthName() + " " + pDate.getYear();
            String Hour = String.valueOf(date.getHours());
            String Minute = String.valueOf(date.getMinutes());
            String Day = String.valueOf(pDate.getDayOfMonth());
            String Month = pDate.getMonthName();
            String Year = String.valueOf(pDate.getYear());
            strOutput.append(Day);
            strOutput.append(" ");
            strOutput.append(Month);
            strOutput.append(" ");
            strOutput.append(Year);
            strOutput.append("\n");
            strOutput.append(Hour);
            strOutput.append(":");
            strOutput.append(Minute);

            output = strOutput.toString();

        }
        return output;
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static Bitmap getSign(String fileName) {
        String signPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_SIGNS;
        String sdPath = Environment.getExternalStoragePublicDirectory(signPath) + "/" + DIRECTORY_ORDER_SIGNS + "/" + fileName;
        return BitmapFactory.decodeFile(sdPath);
    }

    public static boolean Backup(Context mActivity) {

        String format = "HH_mm_ss";
        SimpleDateFormat simpleDate = new SimpleDateFormat(format, Locale.US);
        String strTime = simpleDate.format(new Date().getTime());
        String date = getBackUpDate(new Date().getTime());
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String backupDBPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_BACKUPS;
        File backupDB = new File(sd, backupDBPath);
        if (!backupDB.exists())
            backupDB.mkdirs();
        String currentDBPath = "/data/" + "com.mahak.order" + "/databases/" + DbSchema.DATABASE_NAME;
        String backDBPath = ProjectInfo.DIRECTORY_MAHAKORDER + "/" + ProjectInfo.DIRECTORY_BACKUPS + "/" + "MahakOrder" + getVersionName(mActivity) + "_" + getPrefUsername() + "_" + date + ".db";
        File currentDB = new File(data, currentDBPath);
        File backup = new File(sd, backDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backup).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            return true;
        } catch (IOException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
            return false;
        }
    }

    public static String getVersionName(Context context) {
        PackageInfo pInfo;
        String version = "Unknown";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    private static String getBackUpDate(long date) {
        return getFileName(date);

    }

    public static String convertToEnglishDigits(String value) {
        String result = value.replaceAll("۰", "0");
        result = result.replaceAll("۱", "1");
        result = result.replaceAll("۲", "2");
        result = result.replaceAll("۳", "3");
        result = result.replaceAll("۴", "4");
        result = result.replaceAll("۵", "5");
        result = result.replaceAll("۶", "6");
        result = result.replaceAll("۷", "7");
        result = result.replaceAll("۸", "8");
        result = result.replaceAll("۹", "9");
        result = result.replaceAll("٬", ",");
        result = result.replaceAll("٫", ".");

        return result;
    }

    public static void openFile(Context context, File url) throws ActivityNotFoundException,
            IOException {
        // Create URI
        //Uri uri = Uri.fromFile(url);

        if (url.exists()) {
            //TODO you want to use this method then create file provider in androidmanifest.xml with fileprovider name

            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", url);

            String urlString = url.toString().toLowerCase();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            /**
             * Security
             */
            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            // Check what kind of file you are trying to open, by comparing the url with extensions.
            // When the if condition is matched, plugin sets the correct intent (mime) type,
            // so Android knew what application to use to open the file
            if (urlString.toLowerCase().toLowerCase().contains(".doc")
                    || urlString.toLowerCase().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (urlString.toLowerCase().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (urlString.toLowerCase().contains(".ppt")
                    || urlString.toLowerCase().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (urlString.toLowerCase().contains(".xls")
                    || urlString.toLowerCase().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (urlString.toLowerCase().contains(".zip")
                    || urlString.toLowerCase().contains(".rar")) {
                // ZIP file
                intent.setDataAndType(uri, "application/trap");
            } else if (urlString.toLowerCase().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (urlString.toLowerCase().contains(".wav")
                    || urlString.toLowerCase().contains(".mp3")) {
                // WAV/MP3 audio file
                intent.setDataAndType(uri, "audio/*");
            } else if (urlString.toLowerCase().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (urlString.toLowerCase().contains(".jpg")
                    || urlString.toLowerCase().contains(".jpeg")
                    || urlString.toLowerCase().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (urlString.toLowerCase().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (urlString.toLowerCase().contains(".3gp")
                    || urlString.toLowerCase().contains(".mpg")
                    || urlString.toLowerCase().contains(".mpeg")
                    || urlString.toLowerCase().contains(".mpe")
                    || urlString.toLowerCase().contains(".mp4")
                    || urlString.toLowerCase().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                // if you want you can also define the intent type for any other file

                // additionally use else clause below, to manage other unknown extensions
                // in this case, Android will show all applications installed on the device
                // so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "File doesn't exists", Toast.LENGTH_SHORT).show();
        }
    }

    public static void writeLog(String str) {
        new StringBuffer();
        try {
            String path = Environment.getExternalStorageDirectory().getPath() + "/ORDER_LOG.txt";
            File f = new File(path);
            long seek = f.length();
            RandomAccessFile raf = new RandomAccessFile(new File(path), "rw");
            raf.seek(seek);
            raf.write("\n----------------------------------\n".getBytes());
            seek += (long) "\n----------------------------------\n".getBytes().length;
            raf.seek(seek);
            raf.writeUTF(str);
            raf.close();
        } catch (Exception var10) {
            FirebaseCrashlytics.getInstance().recordException(var10);
            var10.printStackTrace();
        }
    }

    public static String getLikeString(String searchStr) {
        String[] searchArray = searchStr.toString().split(" ");
        String LikeString = "";
        for (String search : searchArray){
            LikeString += " name " +  " like " + " '%" + search + "%' " + " and ";
        }
        LikeString = removeLastAnd(LikeString);
        return LikeString;
    }
    public static String getCustomerLikeString(String searchStr) {
        String[] searchArray = searchStr.toString().split(" ");
        String LikeString = "";
        for (String search : searchArray){
            LikeString += " Customers.name " +  " like " + " '%" + search + "%' " + " and ";
        }
        LikeString = removeLastAnd(LikeString);
        return LikeString;
    }

    public static String removeLastAnd(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
            result = str.substring(0, str.length() - 5);
        }
        return result;
    }
}
