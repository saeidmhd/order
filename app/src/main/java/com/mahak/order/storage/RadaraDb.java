package com.mahak.order.storage;

import static com.mahak.order.BaseActivity.getPrefUserId;
import static com.mahak.order.BaseActivity.mContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import com.mahak.order.BaseActivity;
import com.mahak.order.common.Notification;

import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.StopLocation.StopLog;
import com.mahak.order.common.VisitorLocation;
import com.mahak.order.common.manageLog.StatusLog;
import com.mahak.order.tracking.visitorZone.Datum;
import com.mahak.order.tracking.visitorZone.ZoneLocation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RadaraDb {

    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public RadaraDb(Context ctx) {
        this.mCtx = ctx;
    }

    public RadaraDb open() {
        this.mDbHelper = new DatabaseHelper(mCtx,DbSchema.RADARA_DB);
        this.mDb = mDbHelper.openDataBase(DbSchema.RADARA_DB);
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //............. zone
    public void AddZoneLocation(List<ZoneLocation> data) {
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (ZoneLocation zoneLocation : data) {
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_id, zoneLocation.getId());
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_zoneId, zoneLocation.getZoneId());
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_latitude, zoneLocation.getLatitude());
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_longitude, zoneLocation.getLongitude());
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_createdBy, zoneLocation.getCreatedBy());
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_created, zoneLocation.getCreated());
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_lastModifiedBy, zoneLocation.getLastModifiedBy());
                initialvalue.put(DbSchema.ZoneLocationSchema.COLUMN_lastModified, zoneLocation.getLastModified());
                boolean result = (mDb.update(DbSchema.ZoneLocationSchema.TABLE_NAME, initialvalue,"id =?" , new String[]{String.valueOf(zoneLocation.getId())})) > 0;
                if(!result)
                    mDb.insert(DbSchema.ZoneLocationSchema.TABLE_NAME, null, initialvalue);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }
    public void AddZone(Datum datum) {
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            initialvalue.put(DbSchema.ZoneSchema.COLUMN_zoneId, datum.getId());
            initialvalue.put(DbSchema.ZoneSchema.COLUMN_title, datum.getTitle());
            initialvalue.put(DbSchema.ZoneSchema.COLUMN_visitorId, getPrefUserId());
            initialvalue.put(DbSchema.ZoneSchema.COLUMN_createdBy, datum.getCreatedBy());
            initialvalue.put(DbSchema.ZoneSchema.COLUMN_created, datum.getCreated());
            initialvalue.put(DbSchema.ZoneSchema.COLUMN_lastModifiedBy, datum.getLastModified());
            initialvalue.put(DbSchema.ZoneSchema.COLUMN_lastModified, datum.getLastModifiedBy());
            boolean result = (mDb.update(DbSchema.ZoneSchema.TABLE_NAME, initialvalue,"zoneId =? and visitorId =? " , new String[]{String.valueOf(datum.getId()), String.valueOf(getPrefUserId())})) > 0;
            if(!result)
                mDb.insert(DbSchema.ZoneSchema.TABLE_NAME, null, initialvalue);
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }
    private Datum getZoneFromCursor(Cursor cursor) {
        Datum datum;
        datum = new Datum();
        datum.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.ZoneSchema.COLUMN_zoneId)));
        datum.setTitle(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneSchema.COLUMN_title)));
        datum.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneSchema.COLUMN_createdBy)));
        datum.setCreated(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneSchema.COLUMN_created)));
        datum.setLastModified(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneSchema.COLUMN_lastModifiedBy)));
        datum.setLastModifiedBy(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneSchema.COLUMN_lastModified)));
        return datum;
    }
    private ZoneLocation getZoneLocationFromCursor(Cursor cursor) {
        ZoneLocation zoneLocation;
        zoneLocation = new ZoneLocation();
        zoneLocation.setId(cursor.getInt(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_id)));
        zoneLocation.setZoneId(cursor.getInt(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_zoneId)));
        zoneLocation.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_latitude)));
        zoneLocation.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_longitude)));
        zoneLocation.setCreatedBy(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_createdBy)));
        zoneLocation.setCreated(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_created)));
        zoneLocation.setLastModified(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_lastModifiedBy)));
        zoneLocation.setLastModifiedBy(cursor.getString(cursor.getColumnIndex(DbSchema.ZoneLocationSchema.COLUMN_lastModified)));
        return zoneLocation;
    }
    public ArrayList<Datum> getAllZone() {
        ArrayList<Datum> array = new ArrayList<>();
        Datum datum = new Datum();
        Cursor cursor;
        try {
            cursor = mDb.rawQuery("select * from zone where visitorid =? " ,new String[]{String.valueOf(getPrefUserId())});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    datum = getZoneFromCursor(cursor);
                    array.add(datum);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetTransfer", e.getMessage());
        }
        return array;
    }
    public ArrayList<ZoneLocation> getAllZoneLocation(int id) {
        ArrayList<ZoneLocation> array = new ArrayList<>();
        ZoneLocation zoneLocation = new ZoneLocation();
        Cursor cursor;
        try {
            cursor = mDb.query(DbSchema.ZoneLocationSchema.TABLE_NAME,null,DbSchema.ZoneLocationSchema.COLUMN_zoneId + " =? ", new String[]{String.valueOf(id)},null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    zoneLocation = getZoneLocationFromCursor(cursor);
                    array.add(zoneLocation);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorGetTransfer", e.getMessage());
        }
        return array;
    }

    //............... notification
    private ContentValues getContentValuesNotification(Notification notification) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.NotificationSchema.COLUMN_DATA, notification.getData());
        values.put(DbSchema.NotificationSchema.COLUMN_DATE, notification.getDate());
        values.put(DbSchema.NotificationSchema.COLUMN_FULLMESSAGE, notification.getFullMessage());
        values.put(DbSchema.NotificationSchema.COLUMN_ISREAD, notification.isRead() ? 1 : 0);
        values.put(DbSchema.NotificationSchema.COLUMN_MESSAGE, notification.getMessage());
        values.put(DbSchema.NotificationSchema.COLUMN_TITLE, notification.getTitle());
        values.put(DbSchema.NotificationSchema.COLUMN_TYPE, notification.getType());
        values.put(DbSchema.NotificationSchema.COLUMN_USER_ID, notification.getUserId());
        return values;
    }
    public Notification GetNotification(long id) {
        Cursor cursor;
        Notification notification = new Notification();
        try {
            cursor = mDb.rawQuery("select * from " + DbSchema.NotificationSchema.TABLE_NAME + " where " + DbSchema.NotificationSchema._ID + " =?", new String[]{String.valueOf(id)});
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    notification = getNotificationFromCursor(cursor);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + "-L:1902" + e.getMessage());
        }
        return notification;
    }
    private Notification getNotificationFromCursor(Cursor cursor) {
        Notification notification;
        notification = new Notification();
        notification.setRead(cursor.getInt(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_ISREAD)) == 1);
        notification.set_id(cursor.getInt(cursor.getColumnIndex("_ID")));
        notification.setData(cursor.getString(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_DATA)));
        notification.setDate(cursor.getLong(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_DATE)));
        notification.setFullMessage(cursor.getString(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_FULLMESSAGE)));
        notification.setMessage(cursor.getString(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_MESSAGE)));
        notification.setTitle(cursor.getString(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_TITLE)));
        notification.setType(cursor.getString(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_TYPE)));
        notification.setUserId(cursor.getLong(cursor.getColumnIndex(DbSchema.NotificationSchema.COLUMN_USER_ID)));
        return notification;
    }
    public int getCountNotReadNotification(String userId) {
        Cursor cursor;
        int Count = 0;
        try {
            cursor = mDb.rawQuery("select count(" + DbSchema.NotificationSchema._ID + ") from " + DbSchema.NotificationSchema.TABLE_NAME + " where " + DbSchema.NotificationSchema.COLUMN_ISREAD + " = 0 And " + DbSchema.NotificationSchema.COLUMN_USER_ID + "=?", new String[]{userId});
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    Count = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + "-L:1902" + e.getMessage());
        }

        return Count;
    }
    public long getMinNotificationId() {
        Cursor cursor;
        long MinId = 0;
        try {
            cursor = mDb.rawQuery("select Min(" + DbSchema.NotificationSchema._ID + ") from " + DbSchema.NotificationSchema.TABLE_NAME, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    MinId = cursor.getLong(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("ErrorgetMinId", e.getMessage());
        }

        return MinId;
    }
    public ArrayList<Notification> getAllNotifications(String userId) {
        Notification notification;
        Cursor cursor;
        ArrayList<Notification> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("Select * from " + DbSchema.NotificationSchema.TABLE_NAME + " where " + DbSchema.NotificationSchema.COLUMN_USER_ID + "=? order by " + DbSchema.NotificationSchema.COLUMN_DATE + " DESC", new String[]{userId});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    notification = getNotificationFromCursor(cursor);
                    array.add(notification);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + " - L:2036 - " + e.getMessage());
        }
        return array;
    }
    public void UpdateNotification(Notification notification) {
        try {
            ContentValues initialvalue = getContentValuesNotification(notification);
            mDb.update(DbSchema.NotificationSchema.TABLE_NAME, initialvalue, DbSchema.NotificationSchema._ID + "=?", new String[]{String.valueOf(notification.get_id())});
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            e.printStackTrace();
        }
    }
    public boolean DeleteNotification(long id) {

        return (mDb.delete(DbSchema.NotificationSchema.TABLE_NAME, DbSchema.NotificationSchema._ID + " =? ", new String[]{id + ""})) > 0;
    }
    public boolean DeleteAllNotification() {
        return (mDb.delete(DbSchema.NotificationSchema.TABLE_NAME, null, null)) > 0;
    }
    public long AddNotification(Notification notification) {
        ContentValues values = getContentValuesNotification(notification);
        return mDb.insert(DbSchema.NotificationSchema.TABLE_NAME, null, values);
    }
    public int getCountNotification(String userId) {
        Cursor cursor;
        int Count = 0;
        try {
            cursor = mDb.rawQuery("select count(" + DbSchema.NotificationSchema._ID + ") from " + DbSchema.NotificationSchema.TABLE_NAME + " where " + DbSchema.NotificationSchema.COLUMN_USER_ID + "=?", new String[]{userId});
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    Count = cursor.getInt(0);
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + "-L:1902" + e.getMessage());
        }

        return Count;
    }


    //................ VisitorLocation
    public boolean AddGpsTracking(List<VisitorLocation> VisitorLocations) {
        boolean result = false;
        mDb.beginTransaction();
        try {
            ContentValues initialvalue = new ContentValues();
            for (VisitorLocation visitorLocation : VisitorLocations) {
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_Create_DATE, visitorLocation.getCreateDate());
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_DATE, visitorLocation.getDate());
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_LATITUDE, visitorLocation.getLatitude());
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_uniqueID, visitorLocation.getUniqueID());
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_VisitorLocationId, visitorLocation.getVisitorLocationId());
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_RowVersion, visitorLocation.getRowVersion());
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_LONGITUDE, visitorLocation.getLongitude());
                initialvalue.put(DbSchema.VisitorLocationSchema.COLUMN_VISITOR_ID, visitorLocation.getVisitorId());
                result =  mDb.insert(DbSchema.VisitorLocationSchema.TABLE_NAME, null, initialvalue) > 0;
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
        return result;
    }
    private VisitorLocation getGpsPointFromCursor(Cursor cursor) {
        VisitorLocation visitorLocation = new VisitorLocation();
        visitorLocation.setCreateDate(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_Create_DATE)));
        visitorLocation.setDate(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_DATE)));
        visitorLocation.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_LATITUDE)));
        visitorLocation.setUniqueID(cursor.getString(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_uniqueID)));
        visitorLocation.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_LONGITUDE)));
        visitorLocation.setVisitorLocationId(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_VisitorLocationId)));
        visitorLocation.setRowVersion(cursor.getInt(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_RowVersion)));
        visitorLocation.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.VisitorLocationSchema.COLUMN_VISITOR_ID)));
        return visitorLocation;
    }
    public List<VisitorLocation> getAllGpsPointsForSending() {
        Cursor cursor;
        ArrayList<VisitorLocation> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("Select * from " + DbSchema.VisitorLocationSchema.TABLE_NAME + " Where VisitorId =? and VisitorLocationId =?", new String[]{String.valueOf(BaseActivity.getPrefUserMasterId()), String.valueOf(0)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    VisitorLocation visitorLocation = getGpsPointFromCursor(cursor);
                    array.add(visitorLocation);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            Log.e("ErrAlGpsPoinFroDate", e.getMessage());
        }

        return array;
    }
    public List<LatLng> getAllLatLngPointsFromDate(long date, long visitorId) {
        Cursor cursor;
        ArrayList<LatLng> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("Select * from " + DbSchema.VisitorLocationSchema.TABLE_NAME + " Where Date >=? and VisitorId =? order by " + DbSchema.VisitorLocationSchema.COLUMN_DATE + " DESC", new String[]{String.valueOf(date), String.valueOf(visitorId)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    VisitorLocation visitorLocation = getGpsPointFromCursor(cursor);
                    array.add(new LatLng(visitorLocation.getLatitude(), visitorLocation.getLongitude()));
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            Log.e("ErrAlGpsPoinFroDate", e.getMessage());
        }

        return array;
    }
    public void updateGpsTrackingForSending(VisitorLocation visitorLocation) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.VisitorLocationSchema.COLUMN_VisitorLocationId, visitorLocation.getVisitorLocationId());
        values.put(DbSchema.VisitorLocationSchema.COLUMN_RowVersion, visitorLocation.getRowVersion());
        mDb.update(DbSchema.VisitorLocationSchema.TABLE_NAME, values, DbSchema.VisitorLocationSchema.COLUMN_uniqueID + "=?", new String[]{visitorLocation.getUniqueID()});
    }

    //................ stop log
    public ArrayList<StopLog> getAllStopLogNotSend() {
        StopLog stopLog;
        Cursor cursor;
        ArrayList<StopLog> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select * from StopLog where UserId =? and sent =? ",new String[]{String.valueOf(getPrefUserId()),String.valueOf(-1)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    stopLog = getStopLogFromCursor(cursor);
                    array.add(stopLog);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + " - L:2036 - " + e.getMessage());
        }
        return array;
    }
    private StopLog getStopLogFromCursor(Cursor cursor) {
        StopLog stopLog = new StopLog();
        stopLog.setEndDate(cursor.getString(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_endDate)));
        stopLog.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_UserId)));
        stopLog.setEntryDate(cursor.getString(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_entryDate)));
        stopLog.setDuration(cursor.getLong(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_duration)));
        stopLog.setStopLocationClientId(cursor.getLong(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_stopLocationClientId)));
        stopLog.setLatitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_lat)));
        stopLog.setLongitude(cursor.getDouble(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_lng)));
        stopLog.setSent(cursor.getInt(cursor.getColumnIndex(DbSchema.StopLogSchema.COLUMN_sent)));
        return stopLog;
    }


    //................ manage log
    public ArrayList<StatusLog> getAllStatusLogNotSend() {
        StatusLog statusLog;
        Cursor cursor;
        ArrayList<StatusLog> array = new ArrayList<>();
        try {
            cursor = mDb.rawQuery("select * from ManageLog where UserId =? and sent =? ",new String[]{String.valueOf(getPrefUserId()),String.valueOf(-1)});
            if (cursor != null) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    statusLog = getManageLogFromCursor(cursor);
                    array.add(statusLog);
                    cursor.moveToNext();
                }
                cursor.close();
            }

        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().setCustomKey("user_tell", BaseActivity.getPrefname() + "_" + BaseActivity.getPrefTell());
            FirebaseCrashlytics.getInstance().recordException(e);
            Log.e("@Error", this.getClass().getName() + " - L:2036 - " + e.getMessage());
        }
        return array;
    }
    private StatusLog getManageLogFromCursor(Cursor cursor) {
        StatusLog statusLog = new StatusLog();
        statusLog.setCreated(cursor.getString(cursor.getColumnIndex(DbSchema.ManageLogSchema.COLUMN_Date_time)));
        statusLog.setVisitorId(cursor.getLong(cursor.getColumnIndex(DbSchema.ManageLogSchema.COLUMN_UserId)));
        statusLog.setValue(cursor.getString(cursor.getColumnIndex(DbSchema.ManageLogSchema.COLUMN_Log_value)));
        statusLog.setType(cursor.getInt(cursor.getColumnIndex(DbSchema.ManageLogSchema.COLUMN_Log_type)));
        statusLog.setSent(cursor.getInt(cursor.getColumnIndex(DbSchema.ManageLogSchema.COLUMN_sent)));
        return statusLog;
    }
    public void updateStopLogs(List<StopLog> stopLogs) {
        mDb.beginTransaction();
        ContentValues contentValues = new ContentValues();
        try {
            for (StopLog stopLog : stopLogs) {
                contentValues.put(DbSchema.StopLogSchema.COLUMN_stopLocationClientId, stopLog.getStopLocationClientId());
                contentValues.put(DbSchema.StopLogSchema.COLUMN_lat, stopLog.getLat());
                contentValues.put(DbSchema.StopLogSchema.COLUMN_lng, stopLog.getLng());
                contentValues.put(DbSchema.StopLogSchema.COLUMN_entryDate, stopLog.getEntryDate());
                contentValues.put(DbSchema.StopLogSchema.COLUMN_duration, stopLog.getDuration());
                contentValues.put(DbSchema.StopLogSchema.COLUMN_endDate, stopLog.getEndDate());
                contentValues.put(DbSchema.StopLogSchema.COLUMN_UserId, getPrefUserId());
                contentValues.put(DbSchema.StopLogSchema.COLUMN_sent, stopLog.getSent());
                int numOfRows = mDb.update(DbSchema.StopLogSchema.TABLE_NAME, contentValues, DbSchema.StopLogSchema.COLUMN_stopLocationClientId + "=? and " + DbSchema.StopLogSchema.COLUMN_UserId + "=? " , new String[]{String.valueOf(stopLog.getStopLocationClientId()) , String.valueOf(stopLog.getVisitorId())});
                if(numOfRows == 0)
                    mDb.insert(DbSchema.StopLogSchema.TABLE_NAME, null, contentValues);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }
    public void updateManageLogs(List<StatusLog> statusLogs) {
        mDb.beginTransaction();
        ContentValues contentValues = new ContentValues();
        try {
            for (StatusLog statusLog : statusLogs) {
                contentValues.put(DbSchema.ManageLogSchema.COLUMN_Log_type, statusLog.getType());
                contentValues.put(DbSchema.ManageLogSchema.COLUMN_Log_value, statusLog.getValue());
                contentValues.put(DbSchema.ManageLogSchema.COLUMN_sent, statusLog.getSent());
                contentValues.put(DbSchema.ManageLogSchema.COLUMN_Date_time, statusLog.getCreated());
                contentValues.put(DbSchema.ManageLogSchema.COLUMN_UserId, getPrefUserId());
                int numOfRows = mDb.update(DbSchema.ManageLogSchema.TABLE_NAME, contentValues,  DbSchema.ManageLogSchema.COLUMN_UserId + "=? and " + DbSchema.ManageLogSchema.COLUMN_Date_time + "=? " , new String[]{String.valueOf(statusLog.getVisitorId()) , String.valueOf(statusLog.getCreated())});
                if(numOfRows == 0)
                    mDb.insert(DbSchema.ManageLogSchema.TABLE_NAME, null, contentValues);
            }
            mDb.setTransactionSuccessful();
        } finally {
            mDb.endTransaction();
        }
    }


    public void DeleteAllData() {

        mDb.delete(DbSchema.ZoneLocationSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.ZoneSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.VisitorLocationSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.NotificationSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.StopLogSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.ManageLogSchema.TABLE_NAME, null, null);

        BaseActivity.setPrefAdminControl(false);
        BaseActivity.setPrefTrackingControl(0);

    }
    public void DeleteZoneLocation() {

        //mDb.delete(DbSchema.ZoneLocationSchema.TABLE_NAME, null, null);
        mDb.delete(DbSchema.ZoneSchema.TABLE_NAME, "visitorId=?", new String[]{String.valueOf(getPrefUserId())});

    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private SQLiteDatabase Db;

        DatabaseHelper(Context context , String DatabaseName) {
            super(context, DatabaseName, null, DbSchema.RADARA_DATABASE_VERSION);
        }

        SQLiteDatabase openDataBase(String DbName) throws SQLException {
            final String mPath = mContext.getDatabasePath(DbName).toString();
            if(CheckDatabase(mPath)){
                try {
                    Db = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.OPEN_READWRITE);
                } catch (SQLiteDatabaseLockedException e) {
                    e.printStackTrace();
                }
            }else {
                Db = this.getWritableDatabase();
            }
            return Db;
        }

        Boolean CheckDatabase(String mPath) {

            Boolean res;
            File file = new File(mPath);
            res = file.exists();
            return res;
        }

        @Override
        public synchronized void close() {
            if (Db != null)
                Db.close();
            super.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DbSchema.NotificationSchema.CREATE_TABLE);
            db.execSQL(DbSchema.VisitorLocationSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ZoneSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ZoneLocationSchema.CREATE_TABLE);
            db.execSQL(DbSchema.StopLogSchema.CREATE_TABLE);
            db.execSQL(DbSchema.ManageLogSchema.CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            ServiceTools.writeLog("oldVersion is " + oldVersion + "newVersion" + newVersion);

            if (newVersion > oldVersion) {
            }
        }

    }

}
