package com.mahak.order.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;
import java.util.List;

public class Product implements Parcelable {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_NAME = "Name";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_MASTER_ID = "productCode";
    public static String TAG_DATABASE_ID = "DatabaseId";


    ////////////////////////////

    private long Id;
    private long ModifyDate;
    private long UserId;
    private String MahakId;
    private String DatabaseId;
    private int Publish;
    private int Count;
    private String Image;
    private String Code;

    //from picturesProduct
    private List<PicturesProduct> pictures = null;

    //from product detail
    private String RealPrice;
    private String Barcode;
    private double selectedCount;
    private String DiscountPercent;


    private double SumCount2 = 0;
    private double SumCount1 = 0;
    private double price = 0;
    private double priceVisitor = 0;
    private double customerPrice = 0;

    private int PromotionId;



    @SerializedName("UnitRatio")
    @Expose
    private double unitRatio;

    @SerializedName("TaxPercent")
    @Expose
    private BigDecimal taxPercent;
    @SerializedName("ChargePercent")
    @Expose
    private BigDecimal chargePercent;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    @SerializedName("ProductCode")
    @Expose
    private long productCode;

    @SerializedName("ProductCategoryId")
    @Expose
    private long productCategoryId;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("UnitName")
    @Expose
    private String UnitName;

    @SerializedName("UnitName2")
    @Expose
    private String UnitName2;

    @SerializedName("Tags")
    @Expose
    private String Tags;

    @SerializedName("Min")
    @Expose
    private double Min;

    @SerializedName("ProductId")
    @Expose
    private int productId;

    @SerializedName("ProductClientId")
    @Expose
    private long productClientId;

    @SerializedName("DataHash")
    @Expose
    private String dataHash;
    @SerializedName("CreateDate")
    @Expose
    private String createDate;
    @SerializedName("UpdateDate")
    @Expose
    private String updateDate;
    @SerializedName("CreateSyncId")
    @Expose
    private int createSyncId;
    @SerializedName("UpdateSyncId")
    @Expose
    private int updateSyncId;
    @SerializedName("RowVersion")
    @Expose
    private long rowVersion;

    @SerializedName("Width")
    @Expose
    private float width;
    @SerializedName("Height")
    @Expose
    private float height;

    @SerializedName("Length")
    @Expose
    private float length;

    @SerializedName("Weight")
    @Expose
    private double weight;

    public Product() {
        this.Tags = "";
        this.Name = "";
        this.RealPrice = "";
        this.Code = "";
        this.Image = "";
        this.unitRatio = 0;
        this.Min = 0;
        this.Count = 0;
        this.Publish = ProjectInfo.DONT_PUBLISH;
        this.DiscountPercent = "0";
        this.setMahakId(BaseActivity.getPrefMahakId());
        this.setDatabaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }

    public Product(long id, long categoryid, double asset, int inbox, int min, String name, String tags, String price, String customerprice, String code, int delete) {

        this.setId(id);
        this.setProductCategoryId(categoryid);
        this.unitRatio = inbox;
        this.Name = name;
        this.Tags = tags;
        this.RealPrice = price;
        this.Min = min;
        this.Code = code;

    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setId(Long id) {
        Id = id;
    }

    public long getId() {
        return Id;
    }

    public void setUnitRatio(double unitRatio) {
        this.unitRatio = unitRatio;
    }

    public double getUnitRatio() {
        return unitRatio;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getTags() {
        return Tags;
    }

    public void setRealPrice(String price) {
        RealPrice = price;
    }

    public String getRealPrice() {
        return RealPrice;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getCode() {
        return Code;
    }

    public double getMin() {
        return Min;
    }

    public void setMin(double min) {
        Min = min;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getCount() {
        return Count;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getPublish() {
        return Publish;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted == 1;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName2(String unitName2) {
        UnitName2 = unitName2;
    }

    public String getUnitName2() {
        return UnitName2;
    }

    public double getTaxPercent() {
        if (taxPercent != null)
            return taxPercent.doubleValue();
        else
            return 0;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = new BigDecimal(taxPercent);
    }

    public double getChargePercent() {
        if (chargePercent != null)
            return chargePercent.doubleValue();
        else
            return 0;
    }

    public void setChargePercent(double chargePercent) {
        this.chargePercent = new BigDecimal(chargePercent);
    }

    private Product(Parcel in) {
        setId(in.readLong());
        setProductCategoryId(in.readLong());
        unitRatio = in.readDouble();
        Min = in.readDouble();
        Count = in.readInt();
        Publish = in.readInt();
        DatabaseId = in.readString();
        Name = in.readString();
        Tags = in.readString();
        RealPrice = in.readString();
        Code = in.readString();
        Image = in.readString();
        MahakId = in.readString();
        Image = in.readString();
        setProductCategoryId(in.readLong());
        setProductCode(in.readLong());
        setModifyDate(in.readLong());
        setUserId(in.readLong());
        UnitName = in.readString();
        UnitName2 = in.readString();
        taxPercent = BigDecimal.valueOf(in.readDouble());
        chargePercent = BigDecimal.valueOf(in.readDouble());
    }


    public List<PicturesProduct> getPictures() {
        return pictures;
    }

    public void setPictures(List<PicturesProduct> pictures) {
        this.pictures = pictures;
    }

    public double getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(double selectedCount) {
        this.selectedCount = selectedCount;
    }

    public String getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getProductGroupId() {
        return productCategoryId;
    }

    public void setProductCategoryId(long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getProductCode() {
        return productCode;
    }

    public void setProductCode(long productCode) {
        this.productCode = productCode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    //new

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getProductClientId() {
        return productClientId;
    }

    public void setProductClientId(long productClientId) {
        this.productClientId = productClientId;
    }

    public String getDataHash() {
        return dataHash;
    }

    public void setDataHash(String dataHash) {
        this.dataHash = dataHash;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getCreateSyncId() {
        return createSyncId;
    }

    public void setCreateSyncId(int createSyncId) {
        this.createSyncId = createSyncId;
    }

    public int getUpdateSyncId() {
        return updateSyncId;
    }

    public void setUpdateSyncId(int updateSyncId) {
        this.updateSyncId = updateSyncId;
    }

    public long getRowVersion() {
        return rowVersion;
    }

    public void setRowVersion(long rowVersion) {
        this.rowVersion = rowVersion;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(Id);
        dest.writeLong(productCategoryId);
        dest.writeLong(UserId);
        dest.writeDouble(unitRatio);
        dest.writeString(Name);
        dest.writeString(Tags);
        dest.writeString(RealPrice);
        dest.writeString(Image);
        dest.writeString(UnitName);
        dest.writeString(UnitName2);
        dest.writeDouble(Min);
        dest.writeInt(Count);
        dest.writeInt(Publish);
        dest.writeString(DatabaseId);
        dest.writeString(Code);
        dest.writeString(Image);
        dest.writeString(MahakId);
        dest.writeLong(productCode);
        dest.writeLong(ModifyDate);
        dest.writeDouble(taxPercent.doubleValue());
        dest.writeDouble(chargePercent.doubleValue());

    }

    public double getSumCount2() {
        return SumCount2;
    }

    public void setSumCount2(double sumCount2) {
        SumCount2 = sumCount2;
    }

    public double getSumCount1() {
        return SumCount1;
    }

    public void setSumCount1(double sumCount1) {
        SumCount1 = sumCount1;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCustomerPrice() {
        return customerPrice;
    }

    public void setCustomerPrice(double customerPrice) {
        this.customerPrice = customerPrice;
    }

    public double getPriceVisitor() {
        return priceVisitor;
    }

    public void setPriceVisitor(double priceVisitor) {
        this.priceVisitor = priceVisitor;
    }

    public int getPromotionId() {
        return PromotionId;
    }

    public void setPromotionId(int promotionId) {
        PromotionId = promotionId;
    }
}
