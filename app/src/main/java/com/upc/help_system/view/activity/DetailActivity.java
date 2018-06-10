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
import android.support.annotation.NonNull;
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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.upc.help_system.R;
import com.upc.help_system.utils.Converter;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;

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
    @BindView(R.id.qq_tv)
    TextView qqTv;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.collect)
    Button collect;
    @BindView(R.id.accept)
    Button accept;
    @BindView(R.id.goods_type)
    TextView goodsType;
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;
    private int goodsID ;
    private int userID;
    private int publisher;
    private int accepter;
    private int flag;
    private final  String TAG = "DetailActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        ButterKnife.bind(this);
        Intent i = getIntent();
        flag = i.getIntExtra("myorder",0);
        if(flag == 1){
            accept.setText("完成");
            collect.setText("取消");
        }
        userID = SharedPreferenceUtil.getInt("user","id");
        Intent intent = getIntent();
        String info = intent.getStringExtra("object");
        Gson gson = new Gson();
        JsonObject all = gson.fromJson(info,JsonObject.class);
        goodsID = getIntAttr(all,"pk");
        JsonObject obj = all.getAsJsonObject("fields");
        String publishername = getStringAttr(obj,"publishername");
        publisher = getIntAttr(obj,"publisher");
        accepter = getIntAttr(obj,"accepter");
        String phonenumber = getStringAttr(obj,"phone");
        String qq = getStringAttr(obj,"qq");
        int goods_type = getIntAttr(obj,"type");
        String g_type = Converter.convertGoodsType(goods_type);
        int trade_type = getIntAttr(obj,"trade_type");
        String t_type = Converter.convertTradeType(trade_type);
        String price = getStringAttr(obj,"price");
        String description = getStringAttr(obj,"name")+"   "
                +getStringAttr(obj,"description");

        pubTv.setText(publishername);
        phoneTv.setText(phonenumber);
        qqTv.setText(qq);
        goodsType.setText(g_type);
        type.setText(t_type);
        tipTv.setText(price);
        contentTv.setText(description);
    }

    private String getStringAttr(@NonNull JsonObject object, String attrname){
        return object.get(attrname).getAsString();
    }
    private int getIntAttr(@NonNull JsonObject object,String attrname){
        return object.get(attrname).getAsInt();
    }


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
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneTv.getText().toString()));
                    startActivity(intent);
                }
                break;
            case R.id.collect:
                if(flag == 1){
                    // todo 取消
                    acceptOrder(0);
                    break;
                }
                Log.d("DetailActivity", "userId: "+userID);
                JsonObject object = new JsonObject();
                object.addProperty("userid",userID);
                object.addProperty("goodsid",goodsID);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                RequestService requestService = retrofit.create(RequestService.class);
                Call<String> call = requestService.collect(object);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("DetailActivity", "onResponse: response" +response.body());
                        String x = response.body();
                        switch (x){
                            case "1":
                                Toast.makeText(DetailActivity.this,"收藏成功",Toast.LENGTH_LONG).show();
                                break;
                            case "-1":
                                Toast.makeText(DetailActivity.this,"已经收藏过该订单",Toast.LENGTH_LONG).show();
                                break;
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(DetailActivity.this,"收藏出错",Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.accept:
                if(flag == 1){
                    //todo 完成
                    acceptOrder(-1);
                    //todo 评分

                    break;
                }
                if (publisher == userID) {
                    Snackbar.make(constraintLayout, "不能接收自己的订单", Snackbar.LENGTH_LONG).show();
                } else if (accepter != 0) {
                    Snackbar.make(constraintLayout, "该订单已经被接收", Snackbar.LENGTH_LONG).show();
                } else {
                    acceptOrder(userID);
                }
                break;
        }
    }

    private void acceptOrder(int userid) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestService requestService = retrofit.create(RequestService.class);
        Call<String> call = requestService.acceptOrder(goodsID, userid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                switch (response.body()) {
                    case "1":
                        Log.d(TAG, "userid  =  " +userid);
                        if(userid==0){
                            Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintLayout, "取消成功", SnackbarUtil.Confirm).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DetailActivity.this.finish();
                                }
                            });
                            snackbar.show();
                            break;
                        }
                        if(userid ==-1){
//                            Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintLayout, "订单已完成", SnackbarUtil.Confirm).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//
//                                }
//                            });
//                            snackbar.show();
                            Log.d(TAG, "snackbar.show finish ");
                            Intent i = new Intent(DetailActivity.this,LoginActivity.class);
                            i.putExtra("goodsid",goodsID);
                            Log.d(TAG, "be+fore start activity");
                            startActivity(i);
                            Log.d(TAG, "after start activity" );
                            break;
                        }
                        Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintLayout, "接单成功", SnackbarUtil.Confirm).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DetailActivity.this.finish();
                            }
                        });
                        snackbar.show();
                        break;
                    default:
                        Snackbar snackbar1 = SnackbarUtil.ShortSnackbar(constraintLayout, "接单失败", SnackbarUtil.Alert).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
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
                Snackbar snackbar = SnackbarUtil.ShortSnackbar(constraintLayout, "网络原因失败", SnackbarUtil.Alert).setActionTextColor(Color.RED).setAction("确定", new View.OnClickListener() {
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
