package com.upc.help_system.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.upc.help_system.R;
import com.upc.help_system.model.User;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/6/5.
 */

public class PerfectInformation extends Activity {
    private final String TAG = "PerfectInformation";
    @BindView(R.id.sex)
    AutoCompleteTextView sex;
    @BindView(R.id.phone_number)
    TextInputEditText phoneNumber;
    @BindView(R.id.makesure)
    Button makesure;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    private String username_string;
    private String password_string;
    private String sex_string;
    private String phone_number_string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        ButterKnife.bind(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        username_string = intent.getStringExtra("name");
        password_string = intent.getStringExtra("password");
        makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(PerfectInformation.this.getWindow().getDecorView().getWindowToken(), 0);
                makesure.setEnabled(false);
                sex_string = sex.getText().toString();

                phone_number_string = phoneNumber.getText().toString();
                rigisterToCloud(username_string, password_string, sex_string, phone_number_string);
            }
        });
        Resources resources = this.getResources();
        String sex_string[] = resources.getStringArray(R.array.sex);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dropdown, sex_string);
        sex.setAdapter(adapter);
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex.showDropDown();
            }
        });

    }

    private void rigisterToCloud(String username, String password, String sex_string, String phonenubmer) {
        int sex = sex_string.equals("男")?1:2;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConConfig.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        RequestService requestService = retrofit.create(RequestService.class);
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setPhonenumber(phonenubmer);
        Call<JsonObject> call = requestService.register(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject result = response.body();
                Log.d(TAG, "onResponse: "+result.getAsString());
                int a = result.get("code").getAsInt();
                switch (a) {
                    case 0:
                        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username", username_string);
                        editor.putString("password", password_string);
                        editor.putString("sex", sex_string);
                        editor.putString("phonenumber", phone_number_string);
                        editor.putInt("id",result.get("id").getAsInt());
                        editor.commit();
                        Snackbar.make(getWindow().getDecorView(), "注册成功", Snackbar.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(PerfectInformation.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(PerfectInformation.this, "请检查网络", Toast.LENGTH_SHORT).show();
                Log.d("RegisterActivity", "t:" + t);
            }
        });
    }

    @OnClick(R.id.back_btn)
    public void onClick() {
        this.finish();
    }
}
