package com.upc.help_system.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.upc.help_system.R;
import com.upc.help_system.model.User;
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
 * Created by Administrator on 2017/6/7.
 */

public class UpdateUserActivity extends Activity {
    @BindView(R.id.imageButton)
    ImageButton imageButton;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.sex_edt)
    EditText sex;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.makesure)
    Button makesure;
    ConstraintLayout constrainlayout;
    private String phone_string;
    private String sex_string;
    private String nickname_string;
    private String username;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        ButterKnife.bind(this);
        constrainlayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        sex.setText(sharedPreferences.getString("sex", ""));
        phone.setText(sharedPreferences.getString("phone", ""));
        nickname.setText(sharedPreferences.getString("nickname", ""));
        username = sharedPreferences.getString("name", "");
        password = sharedPreferences.getString("password", "");

    }

    @OnClick({R.id.imageButton, R.id.makesure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                this.finish();
                break;
            case R.id.makesure:
                phone_string = phone.getText().toString();
                sex_string = sex.getText().toString();
                nickname_string = nickname.getText().toString();
                User user = new User();
                user.setPassword(password);
                user.setPhonenumber(phone_string);
                user.setSex(sex_string.equals("男")?1:2);
                user.setName(username);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ConConfig.url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
                RequestService requestService = retrofit.create(RequestService.class);
                Call<String> call = requestService.updateUser(user);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("nickname", nickname_string);
                        editor.commit();
                        SnackbarUtil.LongSnackbar(constrainlayout, "更改成功", SnackbarUtil.Confirm).setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UpdateUserActivity.this.finish();
                            }
                        }).show();

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        SnackbarUtil.ShortSnackbar(constrainlayout, "网络原因导致失败，请稍后重试", SnackbarUtil.Warning).show();
                    }
                });
                break;
        }
    }
}
