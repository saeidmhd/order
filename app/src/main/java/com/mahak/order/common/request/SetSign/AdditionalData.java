package com.mahak.order.common.request.SetSign;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalData {

    @SerializedName("FileUrl")
    @Expose
    private String fileUrl;
    @SerializedName("FileHash")
    @Expose
    private String fileHash;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

}