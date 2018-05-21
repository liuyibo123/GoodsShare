package com.upc.help_system.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;
import com.upc.help_system.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by Administrator on 2017/5/31.
 */

public class DetailActivity extends Activity {

    float tip;
    String pub_person;
    String phone_number;
    String content;
    int id;
    String username;
    String accept_person;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.pub_tv)
    TextView pubTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.call)
    ImageButton call;
    @BindView(R.id.tip_tv)
    TextView tipTv;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.collect)
    Button collect;
    @BindView(R.id.accept)
    Button accept;
    ConstraintLayout constraintlayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        ButterKnife.bind(this);
        constraintlayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        Intent intent = getIntent();
        float tip = intent.getFloatExtra("tip", 0);
        pub_person = intent.getStringExtra("pubperson");
        phone_number = intent.getStringExtra("phonenumber");
        content = intent.getStringExtra("content");
        id = intent.getIntExtra("id", 0);
        accept_person = intent.getStringExtra("acceptperson");
        phoneTv.setText(phone_number);
        tipTv.setText(String.valueOf(tip));
        pubTv.setText(pub_person);
        contentTv.setText(content);
        SharedPreferences sharedpreferenrce = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedpreferenrce.getString("name", "");
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        imageView.onDestroy();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        imageView.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        imageView.onPause();
//    }

    @OnClick({R.id.back_btn, R.id.call, R.id.collect, R.id.accept})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                this.finish();
                break;
            case R.id.call:
                if (ContextCompat.checkSelfPermission(DetailActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // 没有获得授权，申请授权
                    if (ActivityCompat.shouldShowRequestPermissionRationale(DetailActivity.this,
                            Manifest.permission.CALL_PHONE)) {
                        // 返回值：
//                          如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
//                          如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
//                          如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                        // 弹窗需要解释为何需要该权限，再次请求授权
                        Toast.makeText(DetailActivity.this, "该操作需要打电话权限", Toast.LENGTH_LONG).show();

                        // 帮跳转到该应用的设置界面，让用户手动授权
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    } else {
                        // 不需要解释为何需要该权限，直接请求授权
                        ActivityCompat.requestPermissions(DetailActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                1);
                    }
                } else {
                    // 已经获得授权，可以打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_number));
                    startActivity(intent);
                }
                break;
            case R.id.collect:
                //todo
                break;
            case R.id.accept:
                if (username == null || username.equals("")) {
                    Snackbar.make(constraintlayout, "接收订单前需要登录", Snackbar.LENGTH_LONG).show();
                }
                if (pub_person.equals(username)) {
                    Snackbar.make(constraintlayout, "不能接收自己的订单", Snackbar.LENGTH_LONG).show();
                } else if (accept_person != null) {
                    Snackbar.make(constraintlayout, "该订单已经被接收", Snackbar.LENGTH_LONG).show();
                } else {
                    acceptOrder();
                }
                break;
        }
    }

    private void acceptOrder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestService requestService = retrofit.create(RequestService.class);
        Call<String> call = requestService.acceptOrder(id, username);
        Log.d("DetailActivity", "id:" + id);
        Log.d("DetailActivity", username);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                switch (response.body()) {
                    case "1":
                        Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintlayout, "接单成功", SnackbarUtil.Confirm).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DetailActivity.this.finish();
                            }
                        });
                        snackbar.show();
                        break;
                    default:
                        Snackbar snackbar1 = SnackbarUtil.ShortSnackbar(constraintlayout, "接单失败", SnackbarUtil.Alert).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DetailActivity.this.finish();
                            }
                        });
                        snackbar1.show();
                        break;

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintlayout, "网络原因失败", SnackbarUtil.Alert).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailActivity.this.finish();
                    }
                });
                snackbar.show();
            }
        });
    }
}
