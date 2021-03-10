package com.mahak.order.common.loginSignalr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignalLoginResult {


    @SerializedName("result")
    @Expose
    private boolean result;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("userToken")
    @Expose
    private String userToken;

    @SerializedName("data")
    @Expose
    private Data data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String data) {
        this.userToken = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}