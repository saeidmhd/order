package com.mahak.order.common.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllDataBody {

    @SerializedName("UserToken")
    @Expose
    private String userToken;
    @SerializedName("FromBankVersion")
    @Expose
    private long fromBankVersion;
    @SerializedName("FromChecklistVersion")
    @Expose
    private long fromChecklistVersion;
    @SerializedName("FromChequeVersion")
    @Expose
    private long fromChequeVersion;
    @SerializedName("FromExtraDataVersion")
    @Expose
    private long fromExtraDataVersion;
    @SerializedName("FromOrderDetailVersion")
    @Expose
    private long fromOrderDetailVersion;
    @SerializedName("FromOrderVersion")
    @Expose
    private long fromOrderVersion;
    @SerializedName("FromPersonGroupVersion")
    @Expose
    private long fromPersonGroupVersion;
    @SerializedName("FromPersonVersion")
    @Expose
    private long fromPersonVersion;
    @SerializedName("FromPictureVersion")
    @Expose
    private long fromPictureVersion;
    @SerializedName("FromProductCategoryVersion")
    @Expose
    private long fromProductCategoryVersion;
    @SerializedName("FromProductDetailVersion")
    @Expose
    private long fromProductDetailVersion;
    @SerializedName("FromProductVersion")
    @Expose
    private long fromProductVersion;
    @SerializedName("FromReceiptVersion")
    @Expose
    private long fromReceiptVersion;
    @SerializedName("FromSettingVersion")
    @Expose
    private long fromSettingVersion;
    @SerializedName("FromTransactionVersion")
    @Expose
    private long fromTransactionVersion;
    @SerializedName("FromVisitorVersion")
    @Expose
    private long fromVisitorVersion;
    @SerializedName("FromReturnReasonVersion")
    @Expose
    private long fromReturnReasonVersion;

    @SerializedName("FromCostLevelNameVersion")
    @Expose
    private long fromCostLevelNameVersion;

    @SerializedName("FromPromotionVersion")
    @Expose
    private long fromPromotionVersion;

    @SerializedName("FromPromotionDetailVersion")
    @Expose
    private long fromPromotionDetailVersion;

    @SerializedName("FromPromotionEntityVersion")
    @Expose
    private long fromPromotionEntityVersion;

    @SerializedName("FromTransferStoreVersion")
    @Expose
    private long fromTransferStoreVersion;

    @SerializedName("FromTransferStoreDetailVersion")
    @Expose
    private long fromTransferStoreDetailVersion;

    @SerializedName("OrderTypes")
    @Expose
    private ArrayList<Integer> orderTypes;

    @SerializedName("FromPropertyDescriptionVersion")
    @Expose
    private long FromPropertyDescriptionVersion;
    @SerializedName("FromVisitorProductVersion")
    @Expose
    private long fromVisitorProductVersion;

    @SerializedName("FromVisitorPersonVersion")
    @Expose
    private long fromVisitorPersonVersion;

    @SerializedName("FromPhotoGalleryVersion")
    @Expose
    private long fromPhotoGalleryVersion;


    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public long getFromBankVersion() {
        return fromBankVersion;
    }

    public void setFromBankVersion(long fromBankVersion) {
        this.fromBankVersion = fromBankVersion;
    }

    public long getFromChecklistVersion() {
        return fromChecklistVersion;
    }

    public void setFromChecklistVersion(long fromChecklistVersion) {
        this.fromChecklistVersion = fromChecklistVersion;
    }

    public long getFromChequeVersion() {
        return fromChequeVersion;
    }

    public void setFromChequeVersion(long fromChequeVersion) {
        this.fromChequeVersion = fromChequeVersion;
    }

    public long getFromExtraDataVersion() {
        return fromExtraDataVersion;
    }

    public void setFromExtraDataVersion(long fromExtraDataVersion) {
        this.fromExtraDataVersion = fromExtraDataVersion;
    }

    public long getFromOrderDetailVersion() {
        return fromOrderDetailVersion;
    }

    public void setFromOrderDetailVersion(long fromOrderDetailVersion) {
        this.fromOrderDetailVersion = fromOrderDetailVersion;
    }

    public long getFromOrderVersion() {
        return fromOrderVersion;
    }

    public void setFromOrderVersion(long fromOrderVersion) {
        this.fromOrderVersion = fromOrderVersion;
    }

    public long getFromPersonGroupVersion() {
        return fromPersonGroupVersion;
    }

    public void setFromPersonGroupVersion(long fromPersonGroupVersion) {
        this.fromPersonGroupVersion = fromPersonGroupVersion;
    }

    public long getFromPersonVersion() {
        return fromPersonVersion;
    }

    public void setFromPersonVersion(long fromPersonVersion) {
        this.fromPersonVersion = fromPersonVersion;
    }

    public long getFromPictureVersion() {
        return fromPictureVersion;
    }

    public void setFromPictureVersion(long fromPictureVersion) {
        this.fromPictureVersion = fromPictureVersion;
    }

    public long getFromProductCategoryVersion() {
        return fromProductCategoryVersion;
    }

    public void setFromProductCategoryVersion(long fromProductCategoryVersion) {
        this.fromProductCategoryVersion = fromProductCategoryVersion;
    }

    public long getFromProductDetailVersion() {
        return fromProductDetailVersion;
    }

    public void setFromProductDetailVersion(long fromProductDetailVersion) {
        this.fromProductDetailVersion = fromProductDetailVersion;
    }

    public long getFromProductVersion() {
        return fromProductVersion;
    }

    public void setFromProductVersion(long fromProductVersion) {
        this.fromProductVersion = fromProductVersion;
    }

    public long getFromReceiptVersion() {
        return fromReceiptVersion;
    }

    public void setFromReceiptVersion(long fromReceiptVersion) {
        this.fromReceiptVersion = fromReceiptVersion;
    }

    public long getFromSettingVersion() {
        return fromSettingVersion;
    }

    public void setFromSettingVersion(long fromSettingVersion) {
        this.fromSettingVersion = fromSettingVersion;
    }

    public long getFromTransactionVersion() {
        return fromTransactionVersion;
    }

    public void setFromTransactionVersion(long fromTransactionVersion) {
        this.fromTransactionVersion = fromTransactionVersion;
    }

    public long getFromVisitorVersion() {
        return fromVisitorVersion;
    }

    public void setFromVisitorVersion(long fromVisitorVersion) {
        this.fromVisitorVersion = fromVisitorVersion;
    }

    public long getFromReturnReasonVersion() {
        return fromReturnReasonVersion;
    }

    public void setFromReturnReasonVersion(long fromReturnReasonVersion) {
        this.fromReturnReasonVersion = fromReturnReasonVersion;
    }

    public long getFromCostLevelNameVersion() {
        return fromCostLevelNameVersion;
    }

    public void setFromCostLevelNameVersion(long fromCostLevelNameVersion) {
        this.fromCostLevelNameVersion = fromCostLevelNameVersion;
    }

    public long getFromPromotionVersion() {
        return fromPromotionVersion;
    }

    public void setFromPromotionVersion(long fromPromotionVersion) {
        this.fromPromotionVersion = fromPromotionVersion;
    }

    public long getFromPromotionDetailVersion() {
        return fromPromotionDetailVersion;
    }

    public void setFromPromotionDetailVersion(long fromPromotionDetailVersion) {
        this.fromPromotionDetailVersion = fromPromotionDetailVersion;
    }

    public long getFromPromotionEntityVersion() {
        return fromPromotionEntityVersion;
    }

    public void setFromPromotionEntityVersion(long fromPromotionEntityVersion) {
        this.fromPromotionEntityVersion = fromPromotionEntityVersion;
    }

    public long getFromTransferStoreVersion() {
        return fromTransferStoreVersion;
    }

    public void setFromTransferStoreVersion(long fromTransferStoreVersion) {
        this.fromTransferStoreVersion = fromTransferStoreVersion;
    }

    public long getFromTransferStoreDetailVersion() {
        return fromTransferStoreDetailVersion;
    }

    public void setFromTransferStoreDetailVersion(long fromTransferStoreDetailVersion) {
        this.fromTransferStoreDetailVersion = fromTransferStoreDetailVersion;
    }

    public ArrayList<Integer> getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(ArrayList<Integer> orderTypes) {
        this.orderTypes = orderTypes;
    }

    public long getFromPropertyDescriptionVersion() {
        return FromPropertyDescriptionVersion;
    }

    public void setFromPropertyDescriptionVersion(long fromPropertyDescriptionVersion) {
        FromPropertyDescriptionVersion = fromPropertyDescriptionVersion;
    }

    public long getFromVisitorProductVersion() {
        return fromVisitorProductVersion;
    }

    public void setFromVisitorProductVersion(long fromVisitorProductVersion) {
        this.fromVisitorProductVersion = fromVisitorProductVersion;
    }

    public long getFromVisitorPersonVersion() {
        return fromVisitorPersonVersion;
    }

    public void setFromVisitorPersonVersion(long fromVisitorPersonVersion) {
        this.fromVisitorPersonVersion = fromVisitorPersonVersion;
    }

    public long getFromPhotoGalleryVersion() {
        return fromPhotoGalleryVersion;
    }

    public void setFromPhotoGalleryVersion(long fromPhotoGalleryVersion) {
        this.fromPhotoGalleryVersion = fromPhotoGalleryVersion;
    }
}
