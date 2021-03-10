package com.mahak.order.common;

/**
 * Created by Saeid.mhd-at-Gmail.com on 11/14/17.
 */

public class VerifyTransfer {

    private Long transferCode;
    private boolean isAccepted;

    public Long getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(Long transferCode) {
        this.transferCode = transferCode;
    }

    public boolean isIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

}
