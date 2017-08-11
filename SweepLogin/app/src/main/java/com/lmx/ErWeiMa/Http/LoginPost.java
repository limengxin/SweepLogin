package com.lmx.ErWeiMa.Http;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by limen on 2017/8/5.
 */

public interface LoginPost {
    @POST("bj52/mobile/MobileloginAction_login")
    Call<LoginEntity> Login(@Query("username") String username, @Query("password") String password);

    @POST()
    Call<LoginEntity> Scan(@Query("username") String username);

}
