package com.mahak.order.common.RecieveSignalR;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReceiveResult {

    @SerializedName("Result")
    @Expose
    private boolean result;

    @SerializedName("Code")
    @Expose
    private int code;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("UserToken")
    @Expose
    private String userToken;

    @SerializedName("Data")
    @Expose
    private ReceiveData data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ReceiveData getData() {
        return data;
    }

    public void setData(ReceiveData data) {
        this.data = data;
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

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
}