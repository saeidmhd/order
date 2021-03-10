/***********************************************************************
 * Module:  Receiver.java
 * Author:  RCC
 * Purpose: Defines the Class Receiver
 ***********************************************************************/

package com.mahak.order.webService;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

/**
 * @pdOid a2a08745-9658-41f1-a588-55cb52a04d55
 */
public class Receiver extends RequestSender {
    public Receiver(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, boolean responseIsImage) {
        super(context, url, param, receiver, sendMode, soapAction, responseIsImage);
        Log.i("URL:", url + param);
    }

    public Receiver(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, boolean responseIsImage, String cookie) {
        super(context, url, param, receiver, sendMode, soapAction, responseIsImage, cookie);
        Log.i("URL:", url + param);
    }

    public Receiver(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, String methodName, boolean responseIsImage, String cookie) {
        super(context, url, param, receiver, sendMode, soapAction, responseIsImage, cookie);
        Log.i("URL:", url + param);
    }

    public Receiver(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, boolean responseIsImage, String cookie, int timeout) {
        super(context, url, param, receiver, sendMode, soapAction, responseIsImage, cookie, timeout);
        Log.i("URL:", url + param);
    }


    public Receiver(Context context, String url, String param, ResponseReceiver receiver, int sendMode, String soapAction, String methodName, boolean responseIsImage, String cookie, int timeout) {
        super(context, url, param, receiver, sendMode, soapAction, methodName, responseIsImage, cookie, timeout);
        Log.i("URL:", url + param);
    }

    public Receiver(Context context, String url, HashMap<String, Object> param, ResponseReceiver receiver, int sendMode, String soapAction, String methodName, boolean responseIsImage, String cookie, int timeout) {
        super(context, url, param, receiver, sendMode, soapAction, methodName, responseIsImage, cookie, timeout);
        Log.i("URL:", url + param);
    }

    /**
     * @param appKey
     * @param userTocken
     * @param methodName
     * @param params     []
     * @param receiver
     * @pdOid e94dd2b2-c7f7-44b2-ac87-8f9dbdc23ef6
     */
    public void doRequest(String appKey, String userTocken, String methodName, String params[], ResponseReceiver receiver) {
        // TODO: implement

    }

    @Override
    protected void checkProxy() {
        // TODO Auto-generated method stub
    }
}