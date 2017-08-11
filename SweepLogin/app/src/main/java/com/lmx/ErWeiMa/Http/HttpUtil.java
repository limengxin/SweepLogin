package com.lmx.ErWeiMa.Http;

import android.content.Context;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by limengxin on 2017/8/4.
 */

public class HttpUtil {
    static Login mEnd;

    static VerifyLogin mVerifyLogin;


    public static void HttpPost(String url,final Context context,String username,String password){

    //传值Url
    RequestParams requestParams = new RequestParams(url);
        requestParams.addBodyParameter("username",username);
        requestParams.addParameter("password",password);
        requestParams.addHeader("head","android");
        Log.e("==requestParams===","===="+requestParams);
//        requestParams.addQueryStringParameter("username","123");
//        requestParams.addQueryStringParameter("password","123");
//        requestParams.getConnectTimeout();
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
        //请求成功时调用
        @Override
        public void onSuccess(String result) {
            //在方法中可以做解析、适配等工作
            Log.e("==onSuccess===","====");
        }
        //请求失败时调用
        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Log.e("==onError===","====");
        }
        //取消网络请求时调用
        @Override
        public void onCancelled(CancelledException cex) {
            Log.e("==onCancelled===","====");
        }
        //请求完成时调用
        @Override
        public void onFinished() {
            Log.e("==onFinished===","====");
        }
    });

}

    public static void setLoginCallBack(Login end) {
        mEnd = end;
    }

    public void PostLogin(String URL,String username,String password) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    LoginPost loginPost = retrofit.create(LoginPost.class);
    Call<LoginEntity> call = loginPost.Login(username,password);
    call.enqueue(new retrofit2.Callback<LoginEntity>() {
        @Override
        public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
            Log.e("onResponse",response.body().getMessage());
            mEnd.Success(response.body().getMessage());
        }

        @Override
        public void onFailure(Call<LoginEntity> call, Throwable t) {
            t.printStackTrace();
            Log.e("===onFailure===","======"+t);
            mEnd.Filed(t);
        }
    });
}


    public static void setVerifyCallBack(VerifyLogin verifyLogin) {
        mVerifyLogin = verifyLogin;
    }

    public void VerifyLogin(String URL,String username,String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.e("retrofit","===retrofit=="+retrofit);
        LoginPost loginPost = retrofit.create(LoginPost.class);
        Call<LoginEntity> call = loginPost.Scan(username);
        Log.e("call","===call=="+call.toString());
        call.enqueue(new retrofit2.Callback<LoginEntity>() {
            @Override
            public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                Log.e("onResponse",response.body().getMessage());
                mVerifyLogin.Success(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<LoginEntity> call, Throwable t) {
                t.printStackTrace();
                Log.e("===onFailure===","======"+t);
                mVerifyLogin.Filed(t);
            }
        });
    }

}
