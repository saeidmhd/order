package com.mahak.order.apiHelper;


import com.mahak.order.common.DeviceTokenModel.DeviceTokenBody;
import com.mahak.order.common.DeviceTokenModel.DeviceTokenResponse;
import com.mahak.order.common.StopLocation.StopLocationResponse;
import com.mahak.order.common.StopLocation.StopLog;
import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.loginSignalr.SignalLoginBody;
import com.mahak.order.common.loginSignalr.SignalLoginResult;
import com.mahak.order.common.manageLog.ManageLog;
import com.mahak.order.common.request.GetAllDataBody;
import com.mahak.order.common.request.GetAllDataResult.GetDataResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.common.request.SetSign.setSignImage;
import com.mahak.order.mission.AllMissionDetailBody;
import com.mahak.order.mission.GetAllMission;
import com.mahak.order.mission.AllMissionBody;
import com.mahak.order.mission.GetAllMissionDetail;
import com.mahak.order.tracking.setting.SettingBody;
import com.mahak.order.tracking.setting.TrackingSetting;
import com.mahak.order.tracking.visitorZone.VisitorZoneLocation;
import com.mahak.order.tracking.visitorZone.ZoneBody;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("login")
    Call<LoginResult> Login(@Body LoginBody body);

    @POST("Setting/Get")
    Call<TrackingSetting> GetTrackingSetting(@Header("token") String token, @Body SettingBody body);

    @POST("zone/GetVisitorZoneLocations")
    Call<VisitorZoneLocation> GetZoneLocation(@Body ZoneBody body);

    @POST("Mission/GetAll")
    Call<GetAllMission> GetAllMission(@Header("token") String token,@Body AllMissionBody body);

    @POST("Mission/GetAllMissionDetail")
    Call<GetAllMissionDetail> GetAllMissionDetail(@Header("token") String token, @Body AllMissionDetailBody body);

    @POST("user/login")
    Call<SignalLoginResult> LoginSignalR(@Body SignalLoginBody body);

    @POST("GetAllData")
    Call<GetDataResult> GetAllData(@Body GetAllDataBody body);

    @POST("SaveAllData")
    Call<SaveAllDataResult> SaveAllData(@Body SetAllDataBody body);

    @POST("SetDeviceToken")
    Call<DeviceTokenResponse> SetDeviceToken(@Body DeviceTokenBody body);

    @POST("tracking/savestopLocations")
    Call<StopLocationResponse> SetStopLocation(@Body ArrayList<StopLog> body);

    @POST("statuslog/savestatuslog")
    Call<StopLocationResponse> saveStatusLog(@Body ManageLog body);

    @Multipart
    @POST("Upload")
    Call<setSignImage> uploadSignImage(
            @Part MultipartBody.Part file,
            @Header("X-PictureId") long pictureId,
            @Header("X-FileName") String fileName,
            @Query("userToken") String userToken
    );
}