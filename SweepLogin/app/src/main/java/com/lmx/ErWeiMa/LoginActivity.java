package com.lmx.ErWeiMa;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lmx.ErWeiMa.Http.Login;
import com.lmx.ErWeiMa.Http.HttpUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;


import static android.R.attr.type;


public class LoginActivity extends AppCompatActivity implements Login {
    Button login;
    EditText username,password;
    String user,pass;
    HttpUtil httpUtil;
    static String URL;
    ZLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                httpUtil.HttpPost(URL,LoginActivity.this,username.getText().toString().trim(),password.getText().toString().trim());
                user=username.getText().toString().trim();
                pass=password.getText().toString().trim();
                if (!user.isEmpty()&&!user.equals("")&&!pass.equals("")&&!pass.isEmpty()) {
                    dialog.setLoadingBuilder(Z_TYPE.STAR_LOADING)
                            .setCanceledOnTouchOutside(false)
                            .setLoadingColor(Color.BLACK)
                            .setHintText("Loading...")
                            .show();
                    httpUtil.PostLogin(URL, user, pass);
                }else {
                    Toast.makeText(LoginActivity.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    private void initView(){
        dialog = new ZLoadingDialog(LoginActivity.this);
        login= (Button) findViewById(R.id.login);
        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);
        httpUtil=new HttpUtil();
        httpUtil.setLoginCallBack(this);
        URL="http://192.168.123.96:8080/";

    }



//    访问成功
    @Override
    public void Success(Object o) {
//        访问成功后跳转
        dialog.dismiss();
        Intent home=new Intent(LoginActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("username", user);
        bundle.putSerializable("password", pass);
        home.putExtras(bundle);
        startActivity(home);
        finish();
    }

//    访问失败
    @Override
    public void Filed(Throwable t) {
//        失败不跳转，因接口无法访问所以在这跳转，以后能访问以后关闭
        dialog.dismiss();
        Intent home=new Intent(LoginActivity.this,MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", user);
        bundle.putString("password", pass);
        home.putExtras(bundle);
        startActivity(home);
        finish();
    }

}
