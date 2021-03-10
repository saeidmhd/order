package com.mahak.order.common;

public class Information {


    // TAG
    public static String TAG_ID = "Id";
    public static String TAG_DBVERSION = "DBVersion";
    ///////////////////////////////////////////////

    private long Id;
    private int DBVersion;

    public Information() {
        Id = 1;
        DBVersion = 1;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public void setDBVersion(int dBVersion) {
        DBVersion = dBVersion;
    }

    public int getDBVersion() {
        return DBVersion;
    }
}
