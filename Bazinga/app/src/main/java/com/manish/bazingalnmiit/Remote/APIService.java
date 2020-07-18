package com.manish.bazingalnmiit.Remote;

import com.manish.bazingalnmiit.Model.DataMessage;
import com.manish.bazingalnmiit.Model.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;

import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAIs9tw34:APA91bFBc8uaSE8Xk-SLEBQLLWrdGRqN1lgUJQUqMOBfqb3c_6Wf__3KFqlY77Q05WpckoRBGGot3Xr2dgleCIUC1HEEhzCBm5qWsalVe2qDW54jraa3paiSTt74oiY1Z-n17O7lJGts"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);

}
