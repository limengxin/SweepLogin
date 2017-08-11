package com.lmx.ErWeiMa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lmx.ErWeiMa.Http.HttpUtil;
import com.lmx.ErWeiMa.Http.VerifyLogin;
import com.lmx.library.zxing.CaptureActivity;
import com.lmx.library.zxing.ZXingConstants;
import com.lmx.library.zxing.utils.ZXingUtils;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

public class MainActivity extends AppCompatActivity implements VerifyLogin{

    private TextView textView;

    private ImageView imageView;
    private EditText editText;
    private CheckBox checkbox;
    String pass,user,URLS;
    Button Scanning;
    ZLoadingDialog dialog;
    HttpUtil httpUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent login = this.getIntent();
        user=login.getStringExtra("username");
        pass=login.getStringExtra("password");
        initView();
        Scanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode(view);
            }
        });


    }


    public void setLogin(String URL){
        dialog.setLoadingBuilder(Z_TYPE.STAR_LOADING)
                .setCanceledOnTouchOutside(false)
                .setLoadingColor(Color.BLACK)
                .setHintText("Loading...")
                .show();
        httpUtil.VerifyLogin(URL,user,pass);
    }


    private void initView(){
        dialog = new ZLoadingDialog(MainActivity.this);
        Scanning= (Button) findViewById(R.id.Scanning);
        textView = (TextView) findViewById(R.id.tv_show);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        httpUtil.setVerifyCallBack(this);
    }


//    扫描
    public void scanCode(View view) {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        intent.putExtra(ZXingConstants.ScanIsShowHistory, true);
        startActivityForResult(intent, 0x001);
    }



//    创建二维码图片
    public void createQRImage(View view) {
        String str = editText.getText().toString();

        if (TextUtils.isEmpty(str)) {
            Toast.makeText(this, "字符串不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Bitmap qrImage;
        if (checkbox.isChecked()) {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            qrImage = ZXingUtils.createQRCodeWithLogo(str, logo);
        } else {
            qrImage = ZXingUtils.createQRImage(str);
        }

        if (qrImage != null) {
            imageView.setImageBitmap(qrImage);
        } else {
            Toast.makeText(this, "生成失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == ZXingConstants.ScanRequltCode) {
            /**
             * 拿到解析完成的字符串
             */
            String result = data.getStringExtra(ZXingConstants.ScanResult);
            URLS=result;
            setLogin(result);
            textView.setText(result);
        } else if (resultCode == ZXingConstants.ScanHistoryResultCode) {
            /**
             * 历史记录
             */
            //自己实现历史页面
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        }
    }

//    扫描成功做什么
    @Override
    public void Success(Object o) {
        dialog.dismiss();
    }

//    失败做什么
    @Override
    public void Filed(Throwable t) {
        dialog.dismiss();
    }
}
