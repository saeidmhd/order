/***********************************************************************
 * Module:  RunInternetService.java
 * Author:  RCC
 * Purpose: Defines the Class RunInternetService
 ***********************************************************************/

package com.mahak.order.webService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.ProjectInfo;
import com.mahak.order.common.ServiceTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * @pdOid dce11e0c-1cf0-4a05-a5e9-e7a3d08941aa
 */
public class RunInternetService {
    private static StringBuffer Cookies;


    public void Do(Context context, String authToken, String url, String methodName, JSONObject params, RequestSender.ResponseReceiver receiver) {
        Do(context, authToken, url, methodName, params, receiver, getCookies() != null ? getCookies().toString() : null);
    }

    public void Do(Context context, String authToken, String url, String methodName, JSONObject params, RequestSender.ResponseReceiver receiver, int timeout) {
        Do(context, authToken, url, methodName, params, receiver, getCookies() != null ? getCookies().toString() : null, timeout);
    }

    public void Do(Context context, String authToken, String url, String methodName, JSONObject params, RequestSender.ResponseReceiver receiver, String cookies) {
        DoIt(context, authToken, url, methodName, params, receiver, cookies);
    }

    public void Do(Context context, String authToken, String url, String methodName, JSONObject params, RequestSender.ResponseReceiver receiver, String cookies, int timeout) {
        DoIt(context, authToken, url, methodName, params, receiver, cookies, timeout);
    }

    public void Do(Context context, String authToken, String url, String methodName, HashMap<String, Object> params, RequestSender.ResponseReceiver receiver, String cookies, int timeout) {
        DoIt(context, authToken, url, methodName, params, receiver, cookies, timeout);
    }

    private void DoIt(Context context, String authToken, String url, String methodName, JSONObject params, RequestSender.ResponseReceiver receiver, String cookies) {
        try {
            params.put("authToken", (authToken != null ? authToken : ""));
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        String param = /*methodName +*/ params.toString();
        Receiver sender = new Receiver(context, url, param, getRunDoReceiver(receiver), Receiver._WEBSERVICE, "", methodName, false, null);
        sender.start();
    }

    private void DoIt(Context context, String authToken, String url, String methodName, HashMap<String, Object> params, RequestSender.ResponseReceiver receiver, String cookies, int timeout) {
        try {
            setDefaultParam(context, params);
//            params.put("authToken", (authToken != null ? authToken : ""));
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
        }

        Receiver sender = new Receiver(context, url, params, getRunDoReceiver(receiver), Receiver._WEBSERVICE, "", methodName, false, null, timeout);
        sender.start();
    }

    private void setDefaultParam(Context context, HashMap<String, Object> params) {
        params.put("MahakId", BaseActivity.getPrefMahakId(context));
        params.put("DatabaseId", BaseActivity.getPrefDatabaseId(context));
        params.put("UserId", BaseActivity.getPrefUserId(context));
        params.put("deviceToken", ServiceTools.getKeyFromSharedPreferences(context, ProjectInfo.pre_device_token));
        params.put("AppSign", ProjectInfo.APPSIGN);
    }

    private void DoIt(Context context, String authToken, String url, String methodName, JSONObject params, RequestSender.ResponseReceiver receiver, String cookies, int timeout) {
        // try {
        // new DatabaseHelper(context).createDataBase();
        // } catch (IOException e) {
        // FirebaseCrashlytics.getInstance().setCustomKey("user_tell",BaseActivity.getPrefname() +"_"+ BaseActivity.getPrefTell());
        //FirebaseCrashlytics.getInstance().recordException(e);
        // e.printStackTrace();
        // receiver.gotResponse(null, new StringBuffer(e.getMessage()),
        // null);
        // return;
        // }
//            String param = "authToken=" + (authToken != null ? authToken : "") + "&className=" + url + "&methodName=" + methodName + "&params=" + params;
        try {
            params.put("authToken", (authToken != null ? authToken : ""));
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        String param =/* methodName +*/ params.toString();
        Receiver sender = new Receiver(context, url, param, getRunDoReceiver(receiver), Receiver._WEBSERVICE, "", methodName, false, null, timeout);
        sender.start();
    }


    private RequestSender.ResponseReceiver getRunDoReceiver(final RequestSender.ResponseReceiver receiver) {
        return new RequestSender.ResponseReceiver() {

            @Override
            public void gotResponse(Object sender, StringBuffer result, StringBuffer cookies) {

                if (cookies != null && !cookies.toString().equals(""))
                    setCookies(cookies);
                receiver.gotResponse(sender, result, cookies);
            }
        };

    }


    public void run(Context context, String clientId, String authToken, String className, String packageName, String params, int requestCode) {
        try {
            Intent intent = new Intent();
            intent.setClassName(packageName, className);
            intent.putExtra("clientId", clientId);
            intent.putExtra("authToken", authToken);
            intent.putExtra("params", params);
            ((Activity) context).startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }

    public static void resetCookie() {
        setCookies(null);
    }

    public static StringBuffer getCookies() {
        return Cookies;
    }

    public static void setCookies(StringBuffer cookies) {
        Cookies = cookies;
    }

}