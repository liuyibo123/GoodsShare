package com.upc.help_system.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.upc.help_system.R;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by Administrator on 2017/4/24.
 */

public class PubActivity extends FragmentActivity {


    private final String TAG = "PubActivity";
    @BindView(R.id.goods_name)
    EditText goodsName;
    @BindView(R.id.trade_type)
    AutoCompleteTextView tradeType;
    @BindView(R.id.price)
    EditText price;
    @BindView(R.id.type)
    AutoCompleteTextView Type;
    @BindView(R.id.qq)
    EditText qq;
    @BindView(R.id.phonenumber)
    EditText phonenumber;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.btn_pub)
    Button btnPub;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    private Map<String,Integer> map;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        userid = SharedPreferenceUtil.getInt("user","id");
        ButterKnife.bind(this);
        map = new HashMap<>();
        map.put("食物",1);
        map.put("日用品",2);
        map.put("学习用品",3);
        map.put("电子产品",4);
        map.put("体育器材",5);
        map.put("其他",6);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown, new String[]{"租","买"});
        tradeType.setAdapter(adapter);
        tradeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tradeType.showDropDown();
            }
        });
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.dropdown, new String[]{"食物","日用品","学习用品","电子产品","体育器材","其他"});
        Type.setAdapter(adapter2);
        Type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type.showDropDown();
            }
        });
    }

    @OnClick(R.id.back_btn)
    public void onClick() {
        this.finish();
    }


    @OnClick(R.id.btn_pub)
    public void onViewClicked() {
        String name = goodsName.getText().toString();
        String tradetype = tradeType.getText().toString();
        String type = Type.getText().toString();
        String goods_price = price.getText().toString();
        String goods_qq = qq.getText().toString();
        String goods_phonenumber = phonenumber.getText().toString();
        String goods_description = description.getText().toString();
        JsonObject object = new JsonObject();
        object.addProperty("name",name);
        object.addProperty("trade_type",tradetype.equals("租")?1:2);
        object.addProperty("price",goods_price);
        object.addProperty("qq",goods_qq);
        object.addProperty("phone",goods_phonenumber);
        object.addProperty("description",goods_description);
        object.addProperty("type",map.get(type));
        object.addProperty("publisher",userid);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestService requestService = retrofit.create(RequestService.class);
        Call<String> call = requestService.pub(object);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: response" +response.body());
                Toast.makeText(PubActivity.this,"发布成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
