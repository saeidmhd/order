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
    private Long fromBankVersion = null;
    @SerializedName("FromChecklistVersion")
    @Expose
    private Long fromChecklistVersion = null;
    @SerializedName("FromChequeVersion")
    @Expose
    private Long fromChequeVersion = null;
    @SerializedName("FromExtraDataVersion")
    @Expose
    private Long fromExtraDataVersion = null;
    @SerializedName("FromOrderDetailVersion")
    @Expose
    private Long fromOrderDetailVersion = null;
    @SerializedName("FromOrderVersion")
    @Expose
    private Long fromOrderVersion = null;
    @SerializedName("FromPersonGroupVersion")
    @Expose
    private Long fromPersonGroupVersion = null;
    @SerializedName("FromPersonVersion")
    @Expose
    private Long fromPersonVersion = null;
    @SerializedName("FromPictureVersion")
    @Expose
    private Long fromPictureVersion = null;
    @SerializedName("FromProductCategoryVersion")
    @Expose
    private Long fromProductCategoryVersion = null;
    @SerializedName("FromProductDetailVersion")
    @Expose
    private Long fromProductDetailVersion = null;
    @SerializedName("FromProductVersion")
    @Expose
    private Long fromProductVersion = null;
    @SerializedName("FromReceiptVersion")
    @Expose
    private Long fromReceiptVersion = null;
    @SerializedName("FromSettingVersion")
    @Expose
    private Long fromSettingVersion = null;
    @SerializedName("FromTransactionVersion")
    @Expose
    private Long fromTransactionVersion = null;
    @SerializedName("FromVisitorVersion")
    @Expose
    private Long fromVisitorVersion = null;
    @SerializedName("FromReturnReasonVersion")
    @Expose
    private Long fromReturnReasonVersion = null;

    @SerializedName("FromCostLevelNameVersion")
    @Expose
    private Long fromCostLevelNameVersion = null;

    @SerializedName("FromPromotionVersion")
    @Expose
    private Long fromPromotionVersion = null;

    @SerializedName("FromPromotionDetailVersion")
    @Expose
    private Long fromPromotionDetailVersion = null;

    @SerializedName("FromPromotionEntityVersion")
    @Expose
    private Long fromPromotionEntityVersion = null;

    @SerializedName("FromTransferStoreVersion")
    @Expose
    private Long fromTransferStoreVersion = null;

    @SerializedName("FromTransferStoreDetailVersion")
    @Expose
    private Long fromTransferStoreDetailVersion = null;

    @SerializedName("OrderTypes")
    @Expose
    private ArrayList<Integer> orderTypes = null;

    @SerializedName("FromPropertyDescriptionVersion")
    @Expose
    private Long FromPropertyDescriptionVersion = null;
    @SerializedName("FromVisitorProductVersion")
    @Expose
    private Long fromVisitorProductVersion = null;

    @SerializedName("FromVisitorPersonVersion")
    @Expose
    private Long fromVisitorPersonVersion = null;

    @SerializedName("FromPhotoGalleryVersion")
    @Expose
    private Long fromPhotoGalleryVersion = null;

    @SerializedName("FromRegionVersion")
    @Expose
    private Long fromRegionVersion = null;

    @SerializedName("fromMissionVersion")
    @Expose
    private Long fromMissionVersion = null;

    @SerializedName("fromMissionDetailVersion")
    @Expose
    private Long fromMissionDetailVersion = null;


    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public Long getFromBankVersion() {
        return fromBankVersion;
    }

    public void setFromBankVersion(Long fromBankVersion) {
        this.fromBankVersion = fromBankVersion;
    }

    public Long getFromChecklistVersion() {
        return fromChecklistVersion;
    }

    public void setFromChecklistVersion(Long fromChecklistVersion) {
        this.fromChecklistVersion = fromChecklistVersion;
    }

    public Long getFromChequeVersion() {
        return fromChequeVersion;
    }

    public void setFromChequeVersion(Long fromChequeVersion) {
        this.fromChequeVersion = fromChequeVersion;
    }

    public Long getFromExtraDataVersion() {
        return fromExtraDataVersion;
    }

    public void setFromExtraDataVersion(Long fromExtraDataVersion) {
        this.fromExtraDataVersion = fromExtraDataVersion;
    }

    public Long getFromOrderDetailVersion() {
        return fromOrderDetailVersion;
    }

    public void setFromOrderDetailVersion(Long fromOrderDetailVersion) {
        this.fromOrderDetailVersion = fromOrderDetailVersion;
    }

    public Long getFromOrderVersion() {
        return fromOrderVersion;
    }

    public void setFromOrderVersion(Long fromOrderVersion) {
        this.fromOrderVersion = fromOrderVersion;
    }

    public Long getFromPersonGroupVersion() {
        return fromPersonGroupVersion;
    }

    public void setFromPersonGroupVersion(Long fromPersonGroupVersion) {
        this.fromPersonGroupVersion = fromPersonGroupVersion;
    }

    public Long getFromPersonVersion() {
        return fromPersonVersion;
    }

    public void setFromPersonVersion(Long fromPersonVersion) {
        this.fromPersonVersion = fromPersonVersion;
    }

    public Long getFromPictureVersion() {
        return fromPictureVersion;
    }

    public void setFromPictureVersion(Long fromPictureVersion) {
        this.fromPictureVersion = fromPictureVersion;
    }

    public Long getFromProductCategoryVersion() {
        return fromProductCategoryVersion;
    }

    public void setFromProductCategoryVersion(Long fromProductCategoryVersion) {
        this.fromProductCategoryVersion = fromProductCategoryVersion;
    }

    public Long getFromProductDetailVersion() {
        return fromProductDetailVersion;
    }

    public void setFromProductDetailVersion(Long fromProductDetailVersion) {
        this.fromProductDetailVersion = fromProductDetailVersion;
    }

    public Long getFromProductVersion() {
        return fromProductVersion;
    }

    public void setFromProductVersion(Long fromProductVersion) {
        this.fromProductVersion = fromProductVersion;
    }

    public Long getFromReceiptVersion() {
        return fromReceiptVersion;
    }

    public void setFromReceiptVersion(Long fromReceiptVersion) {
        this.fromReceiptVersion = fromReceiptVersion;
    }

    public Long getFromSettingVersion() {
        return fromSettingVersion;
    }

    public void setFromSettingVersion(Long fromSettingVersion) {
        this.fromSettingVersion = fromSettingVersion;
    }

    public Long getFromTransactionVersion() {
        return fromTransactionVersion;
    }

    public void setFromTransactionVersion(Long fromTransactionVersion) {
        this.fromTransactionVersion = fromTransactionVersion;
    }

    public Long getFromVisitorVersion() {
        return fromVisitorVersion;
    }

    public void setFromVisitorVersion(Long fromVisitorVersion) {
        this.fromVisitorVersion = fromVisitorVersion;
    }

    public Long getFromReturnReasonVersion() {
        return fromReturnReasonVersion;
    }

    public void setFromReturnReasonVersion(Long fromReturnReasonVersion) {
        this.fromReturnReasonVersion = fromReturnReasonVersion;
    }

    public Long getFromCostLevelNameVersion() {
        return fromCostLevelNameVersion;
    }

    public void setFromCostLevelNameVersion(Long fromCostLevelNameVersion) {
        this.fromCostLevelNameVersion = fromCostLevelNameVersion;
    }

    public Long getFromPromotionVersion() {
        return fromPromotionVersion;
    }

    public void setFromPromotionVersion(Long fromPromotionVersion) {
        this.fromPromotionVersion = fromPromotionVersion;
    }

    public Long getFromPromotionDetailVersion() {
        return fromPromotionDetailVersion;
    }

    public void setFromPromotionDetailVersion(Long fromPromotionDetailVersion) {
        this.fromPromotionDetailVersion = fromPromotionDetailVersion;
    }

    public Long getFromPromotionEntityVersion() {
        return fromPromotionEntityVersion;
    }

    public void setFromPromotionEntityVersion(Long fromPromotionEntityVersion) {
        this.fromPromotionEntityVersion = fromPromotionEntityVersion;
    }

    public Long getFromTransferStoreVersion() {
        return fromTransferStoreVersion;
    }

    public void setFromTransferStoreVersion(Long fromTransferStoreVersion) {
        this.fromTransferStoreVersion = fromTransferStoreVersion;
    }

    public Long getFromTransferStoreDetailVersion() {
        return fromTransferStoreDetailVersion;
    }

    public void setFromTransferStoreDetailVersion(Long fromTransferStoreDetailVersion) {
        this.fromTransferStoreDetailVersion = fromTransferStoreDetailVersion;
    }

    public ArrayList<Integer> getOrderTypes() {
        return orderTypes;
    }

    public void setOrderTypes(ArrayList<Integer> orderTypes) {
        this.orderTypes = orderTypes;
    }

    public Long getFromPropertyDescriptionVersion() {
        return FromPropertyDescriptionVersion;
    }

    public void setFromPropertyDescriptionVersion(Long fromPropertyDescriptionVersion) {
        FromPropertyDescriptionVersion = fromPropertyDescriptionVersion;
    }

    public Long getFromVisitorProductVersion() {
        return fromVisitorProductVersion;
    }

    public void setFromVisitorProductVersion(Long fromVisitorProductVersion) {
        this.fromVisitorProductVersion = fromVisitorProductVersion;
    }

    public Long getFromVisitorPersonVersion() {
        return fromVisitorPersonVersion;
    }

    public void setFromVisitorPersonVersion(Long fromVisitorPersonVersion) {
        this.fromVisitorPersonVersion = fromVisitorPersonVersion;
    }

    public Long getFromPhotoGalleryVersion() {
        return fromPhotoGalleryVersion;
    }

    public void setFromPhotoGalleryVersion(Long fromPhotoGalleryVersion) {
        this.fromPhotoGalleryVersion = fromPhotoGalleryVersion;
    }

    public Long getFromRegionVersion() {
        return fromRegionVersion;
    }

    public void setFromRegionVersion(Long fromRegionVersion) {
        this.fromRegionVersion = fromRegionVersion;
    }

    public Long getFromMissionVersion() {
        return fromMissionVersion;
    }

    public void setFromMissionVersion(Long fromMissionVersion) {
        this.fromMissionVersion = fromMissionVersion;
    }

    public Long getFromMissionDetailVersion() {
        return fromMissionDetailVersion;
    }

    public void setFromMissionDetailVersion(Long fromMissionDetailVersion) {
        this.fromMissionDetailVersion = fromMissionDetailVersion;
    }
}
