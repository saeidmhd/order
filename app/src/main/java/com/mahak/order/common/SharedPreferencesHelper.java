package com.mahak.order.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.mahak.order.BaseActivity;

public class SharedPreferencesHelper {

    private static final String PREF_FILE_NAME = "shMahak";
    private static final String PREF_SignUnderFactor = "shSignUnderFactor";
    private static final String PREF_CompactPrinter = "CompactPrinter";
    private static final String PREF_DetailUnderFactor = "DetailUnderFactor";
    private static final String PREF_BelowPrice = "BelowPrice";
    private static String PREF_REDUCE_ASSET = "ReduceAsset";
    private static String PREF_SHOW_FIELD_ORDER = "";
    private static String PREF_PRINTER_BRAND = "";
    public static String __pref_current_Width = "CurrentWidth";
    public static String __pref_current_font = "CurrentFont";
    public static String __pref_current_language = "language";
    private static String PREF_Bank_Pos = "";

    private static String PREF_chk_tracking_code = "chk_tracking_code";
    private static String PREF_chk_market_name = "chk_market_name";
    private static String PREF_chk_customer_name = "chk_customer_name";
    private static String PREF_chk_count_product = "chk_count_product";

    private static String PREF_chk_show_consumer_fee = "chk_show_consumer_fee";
    private static String PREF_chk_show_receipt_detail = "chk_show_receipt_detail";


    public static int getCurrentWidthSize(Context ctx) {
        SharedPreferences sh = ctx.getSharedPreferences(__pref_current_Width, Context.MODE_PRIVATE);
        return sh.getInt(__pref_current_Width, 65);
    }

    public static void setCurrentWidthSize(Context ctx, int size) {
        SharedPreferences sh = ctx.getSharedPreferences(__pref_current_Width, Context.MODE_PRIVATE);
        Editor editor = sh.edit();
        editor.putInt(__pref_current_Width, size).commit();
    }

    public static float getCurrentFontSize(Context ctx) {
        SharedPreferences sh = ctx.getSharedPreferences(__pref_current_font, Context.MODE_PRIVATE);
        return sh.getFloat(__pref_current_font, 12);
    }

    public static void setCurrentFontSize(Context ctx, float size) {
        SharedPreferences sh = ctx.getSharedPreferences(__pref_current_font, Context.MODE_PRIVATE);
        Editor editor = sh.edit();
        editor.putFloat(__pref_current_font, size).commit();
    }


    public static String getCurrentLanguage(Context ctx) {
        SharedPreferences sh = ctx.getSharedPreferences(__pref_current_language, Context.MODE_PRIVATE);
        return sh.getString(__pref_current_language, "null");
    }

    public static void setCurrentLanguage(Context ctx, String language) {
        SharedPreferences sh = ctx.getSharedPreferences(__pref_current_language, Context.MODE_PRIVATE);
        Editor editor = sh.edit();
        editor.putString(__pref_current_language, language).commit();
    }


    public static void setPrefReduceAsset(Context ctx, Boolean value) {
        PREF_REDUCE_ASSET = BaseActivity.getPrefUserId() + "_ReduceAsset";

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_REDUCE_ASSET, value);
        editor.commit();
    }

    public static boolean getPrefReduceAsset(Context ctx, Boolean defValue) {
        boolean res = ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getBoolean("ReduceAsset", defValue);

        PREF_REDUCE_ASSET = BaseActivity.getPrefUserId() + "_ReduceAsset";
        return ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getBoolean(PREF_REDUCE_ASSET, res);
    }

    public static void setPrefShowFieldOrder(Context ctx, Boolean value) {

        PREF_SHOW_FIELD_ORDER = BaseActivity.getPrefUserId() + "_ShowFieldOrder";
        SharedPreferences perfs = ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_SHOW_FIELD_ORDER, value);
        editor.commit();
    }

    public static boolean getPrefShowFieldOrder(Context ctx, Boolean defValue) {
        PREF_SHOW_FIELD_ORDER = BaseActivity.getPrefUserId() + "_ShowFieldOrder";
        return ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getBoolean(PREF_SHOW_FIELD_ORDER, defValue);
    }

    public static void setPrefPrinterBrand(Context ctx, int value) {

        PREF_PRINTER_BRAND = "_PrinterBrand";
        SharedPreferences perfs = ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putInt(PREF_PRINTER_BRAND, value);
        editor.commit();
        BaseActivity.setCurrentPrinter("");
    }

    public static void setPrefBankPos(Context ctx, int value) {
        PREF_Bank_Pos = BaseActivity.getPrefUserId() + "_PosBank";
        SharedPreferences perfs = ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putInt(PREF_Bank_Pos, value);
        editor.commit();
    }

    public static int getPrefBankPos(Context ctx) {
        PREF_Bank_Pos = BaseActivity.getPrefUserId() + "_PosBank";
        return ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getInt(PREF_Bank_Pos, 0);
    }

    public static int getPrefPrinterBrand(Context ctx) {
        PREF_PRINTER_BRAND = "_PrinterBrand";
        return ctx.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE).getInt(PREF_PRINTER_BRAND, 0);
    }

    public static void setSignUnderFactor(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_SignUnderFactor, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_SignUnderFactor, value);
        editor.commit();
    }

    public static boolean getSignUnderFactor(Context ctx) {
        return ctx.getSharedPreferences(PREF_SignUnderFactor, Context.MODE_PRIVATE).getBoolean(PREF_SignUnderFactor, true);
    }


    public static void set_chk_tracking_code(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_chk_tracking_code, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_chk_tracking_code, value);
        editor.commit();
    }

    public static boolean get_chk_tracking_code(Context ctx) {
        return ctx.getSharedPreferences(PREF_chk_tracking_code, Context.MODE_PRIVATE).getBoolean(PREF_chk_tracking_code, true);
    }


    public static void set_chk_market_name(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_chk_market_name, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_chk_market_name, value);
        editor.commit();
    }
    public static void set_chk_show_consumer_fee(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_chk_show_consumer_fee, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_chk_show_consumer_fee, value);
        editor.commit();
    }
    public static void set_chk_show_receipt_detail(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_chk_show_receipt_detail, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_chk_show_receipt_detail, value);
        editor.commit();
    }

    public static boolean get_chk_market_name(Context ctx) {
        return ctx.getSharedPreferences(PREF_chk_market_name, Context.MODE_PRIVATE).getBoolean(PREF_chk_market_name, true);
    }
    public static boolean get_chk_show_consumer_fee(Context ctx) {
        return ctx.getSharedPreferences(PREF_chk_show_consumer_fee, Context.MODE_PRIVATE).getBoolean(PREF_chk_show_consumer_fee, false);
    }
    public static boolean get_chk_show_receipt_detail(Context ctx) {
        return ctx.getSharedPreferences(PREF_chk_show_receipt_detail, Context.MODE_PRIVATE).getBoolean(PREF_chk_show_receipt_detail, false);
    }


    public static void set_chk_customer_name(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_chk_customer_name, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_chk_customer_name, value);
        editor.commit();
    }

    public static boolean get_chk_customer_name(Context ctx) {
        return ctx.getSharedPreferences(PREF_chk_customer_name, Context.MODE_PRIVATE).getBoolean(PREF_chk_customer_name, true);
    }


    public static void set_chk_count_product(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_chk_count_product, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_chk_count_product, value);
        editor.commit();
    }

    public static boolean get_chk_count_product(Context ctx) {
        return ctx.getSharedPreferences(PREF_chk_count_product, Context.MODE_PRIVATE).getBoolean(PREF_chk_count_product, true);
    }

    public static void setDetailUnderFactor(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_DetailUnderFactor, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_DetailUnderFactor, value);
        editor.commit();
    }


    public static boolean getDetailUnderFactor(Context ctx) {
        return ctx.getSharedPreferences(PREF_DetailUnderFactor, Context.MODE_PRIVATE).getBoolean(PREF_DetailUnderFactor, true);
    }

    public static void setBelowPrice(Context ctx, boolean value) {

        SharedPreferences perfs = ctx.getSharedPreferences(PREF_BelowPrice, Context.MODE_PRIVATE);
        Editor editor = perfs.edit();
        editor.putBoolean(PREF_BelowPrice, value);
        editor.apply();
    }

    public static boolean getBelowPrice(Context ctx) {
        return ctx.getSharedPreferences(PREF_BelowPrice, Context.MODE_PRIVATE).getBoolean(PREF_BelowPrice, false);
    }


}

