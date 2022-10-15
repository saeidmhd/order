package com.mahak.order.libs;

import android.content.Context;
import android.graphics.Typeface;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.ServiceTools;

import java.lang.reflect.Field;

public final class FontsOverride {

    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field StaticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            StaticField.setAccessible(true);
            StaticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            ServiceTools.logToFireBase(e);
            e.printStackTrace();
        }
    }
}