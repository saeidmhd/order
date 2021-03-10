package com.mahak.order.common;

/**
 * Created by Saeid.mhd-at-Gmail.com on 11/26/17.
 */

public class ResultOfTransfers {

    public static String TAG_transferCode = "TransferCode";
    public static String TAG_isAccepted = "isAccepted";
    public static String TAG_comment = "Comment";


    private long transferCode;
    private String isAccepted;
    private String comment;


    public long getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(long transferCode) {
        this.transferCode = transferCode;
    }

    public String isIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(String isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
