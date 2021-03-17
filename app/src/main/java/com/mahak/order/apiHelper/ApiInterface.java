package com.mahak.order.apiHelper;


import com.mahak.order.common.login.LoginBody;
import com.mahak.order.common.login.LoginResult;
import com.mahak.order.common.loginSignalr.SignalLoginBody;
import com.mahak.order.common.loginSignalr.SignalLoginResult;
import com.mahak.order.common.request.GetAllDataBody;
import com.mahak.order.common.request.GetAllDataResult.GetDataResult;
import com.mahak.order.common.request.SetAllDataBody;
import com.mahak.order.common.request.SetAllDataResult.SaveAllDataResult;
import com.mahak.order.common.request.SetSign.setSignImage;
import com.mahak.order.tracking.visitorZone.VisitorZoneLocation;
import com.mahak.order.tracking.visitorZone.ZoneBody;

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

    @POST("zone/GetVisitorZoneLocations")
    Call<VisitorZoneLocation> GetZoneLocation(@Body ZoneBody body);

    @POST("user/login")
    Call<SignalLoginResult> LoginSignalR(@Body SignalLoginBody body);

    @POST("GetAllData")
    Call<GetDataResult> GetAllData(@Body GetAllDataBody body);

    @POST("SaveAllData")
    Call<SaveAllDataResult> SaveAllData(@Body SetAllDataBody body);

    @Multipart
    @POST("Upload")
    Call<setSignImage> uploadSignImage(
            @Part MultipartBody.Part file,
            @Header("X-PictureId") long pictureId,
            @Header("X-FileName") String fileName,
            @Query("userToken") String userToken
    );
}