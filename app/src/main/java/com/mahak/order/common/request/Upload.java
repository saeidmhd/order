package com.mahak.order.common.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Upload {

    @SerializedName("UserToken")
    @Expose
    private String userToken;

    @SerializedName("ItemType")
    @Expose
    private String ItemType;

    @SerializedName("ItemID")
    @Expose
    private int ItemID;

    @SerializedName("FileName")
    @Expose
    private int FileName;


}
