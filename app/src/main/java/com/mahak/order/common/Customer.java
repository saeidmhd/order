package com.mahak.order.common;


import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.BaseActivity;

import java.math.BigDecimal;


public class Customer {

    //TAG
    public static String TAG_ID = "Id";
    public static String TAG_NAME = "Name";
    public static String TAG_GROUP_ID = "personGroupId";
    public static String TAG_MARKET_NAME = "Organization";
    public static String TAG_CREDIT = "Credit";
    public static String TAG_REMAINED = "Balance";
    public static String TAG_STATE = "State";
    public static String TAG_CITY = "City";
    public static String TAG_ADDRESS = "Address";
    public static String TAG_ZONE = "Zone";
    public static String TAG_TELL = "Tell";
    public static String TAG_MOBILE = "Mobile";
    public static String TAG_SHIFT = "Shift";
    public static String TAG_LATITUDE = "Latitude";
    public static String TAG_LONGITUDE = "Longitude";
    public static String TAG_IS_DELETE = "IsDelete";
    public static String TAG_MAHAK_ID = "MahakId";
    public static String TAG_MASTER_ID = "personCode";
    public static String TAG_DATABASE_ID = "DatabaseId";
    public static String TAG_USER_ID = "UserId";
    public static String TAG_DiscountPercent = "DiscountPercent";
    public static String TAG_Sell_DefaultCostLevel = "sellPriceLevel";
    //////////////////////////////

    private long Id;
    private String MahakId;
    private String DatabaseId;
    private String Barcode;
    private int Publish;
    private long ModifyDate;

    private long UserId;
    private String Name;
    private String Group;
    private String Shift;

    private int orderCount;
    private int promotionId;

    @SerializedName("FirstName")
    @Expose
    private String firstName;

    @SerializedName("LastName")
    @Expose
    private String lastName;

    @SerializedName("SellPriceLevel")
    @Expose
    private String sellPriceLevel;

    @SerializedName("Organization")
    @Expose
    private String Organization;

    @SerializedName("Phone")
    @Expose
    private String Tell;

    @SerializedName("Balance")
    @Expose
    private BigDecimal Balance;

    @SerializedName("Credit")
    @Expose
    private BigDecimal Credit;

    @SerializedName("PersonGroupId")
    @Expose
    private long personGroupId;

    @SerializedName("PersonGroupCode")
    @Expose
    private long PersonGroupCode;

    @SerializedName("Deleted")
    @Expose
    private boolean deleted;

    @SerializedName("PersonCode")
    @Expose
    private long personCode;

    @SerializedName("DiscountPercent")
    @Expose
    private String DiscountPercent;

    @SerializedName("State")
    @Expose
    private String State;

    @SerializedName("Zone")
    @Expose
    private String Zone;
    @SerializedName("Address")
    @Expose
    private String Address;

    @SerializedName("Latitude")
    @Expose
    private BigDecimal Latitude;

    @SerializedName("Longitude")
    @Expose
    private BigDecimal Longitude;

    @SerializedName("City")
    @Expose
    private String City;

    @SerializedName("Mobile")
    @Expose
    private String Mobile;
    //new
    @SerializedName("PersonId")
    @Expose
    private int personId;
    @SerializedName("PersonClientId")
    @Expose
    private long personClientId;

    @SerializedName("PersonType")
    @Expose
    private int personType;

    @SerializedName("Gender")
    @Expose
    private int gender;
    @SerializedName("NationalCode")
    @Expose
    private String nationalCode;

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;

    @SerializedName("CityCode")
    @Expose
    private String cityCode;

    @SerializedName("Fax")
    @Expose
    private String fax;

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

    public Customer() {
        this.setGroup("");
        this.setOrganization("");
        this.setState("");
        this.setCity("");
        this.setAddress("");
        this.setZone("");
        this.setTell("");
        this.setMobile("");
        this.setLatitude(0);
        this.setLongitude(0);
        this.setCredit(0);
        this.setBalance(0);
        this.setPublish(ProjectInfo.DONT_PUBLISH);
        this.setSellPriceLevel("1");
        this.setDiscountPercent("0.00");
        setMahakId(BaseActivity.getPrefMahakId());
        setDatabaseId(BaseActivity.getPrefDatabaseId());
        setUserId(BaseActivity.getPrefUserId());
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getModifyDate() {
        return ModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        ModifyDate = modifyDate;
    }

    public long getPersonCode() {
        return personCode;
    }

    public void setPersonCode(long personCode) {
        this.personCode = personCode;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public String getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getOrganization() {
        return Organization;
    }

    public void setOrganization(String organization) {
        Organization = organization;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    public String getTell() {
        return Tell;
    }

    public void setTell(String tell) {
        Tell = tell;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getShift() {
        return Shift;
    }

    public void setShift(String shift) {
        Shift = shift;
    }

    public double getLatitude() {
        if (Latitude != null)
            return Latitude.doubleValue();
        else
            return 0;
    }

    public void setLatitude(double latitude) {
        try {
            Latitude = new BigDecimal(latitude);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Latitude = new BigDecimal("0.0");
        }

    }

    public double getLongitude() {
        if (Longitude != null)
            return Longitude.doubleValue();
        else
            return 0;
    }

    public void setLongitude(double longitude) {
        try {
            Longitude = new BigDecimal(longitude);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Longitude = new BigDecimal("0.0");
        }

    }

    public double getCredit() {
        return Credit.doubleValue();
    }

    public void setCredit(double credit) {
        Credit = new BigDecimal(credit);
    }

    public double getBalance() {
        return Balance.doubleValue();
    }

    public void setBalance(double balance) {
        Balance = new BigDecimal(balance);
    }

    public String getMahakId() {
        return MahakId;
    }

    public void setMahakId(String mahakId) {
        MahakId = mahakId;
    }

    public String getDatabaseId() {
        return DatabaseId;
    }

    public void setDatabaseId(String databaseId) {
        DatabaseId = databaseId;
    }

    public String getSellPriceLevel() {
        return sellPriceLevel;
    }

    public void setSellPriceLevel(String sellPriceLevel) {
        this.sellPriceLevel = sellPriceLevel;
    }

    public int getPublish() {
        return Publish;
    }

    public void setPublish(int publish) {
        Publish = publish;
    }

    public int getDeleted() {
        return deleted ? 1 : 0;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public long getPersonClientId() {
        return personClientId;
    }

    public void setPersonClientId(long personClientId) {
        this.personClientId = personClientId;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
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

    public long getPersonGroupId() {
        return personGroupId;
    }

    public void setPersonGroupId(long personGroupId) {
        this.personGroupId = personGroupId;
    }

    public long getPersonGroupCode() {
        return PersonGroupCode;
    }

    public void setPersonGroupCode(long personGroupCode) {
        PersonGroupCode = personGroupCode;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }
}
