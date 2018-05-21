package com.upc.help_system.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class ChangPasswordActivity extends Activity {
    @BindView(R.id.new_password_confirm)
    TextInputEditText newPasswordConfirm;
    @BindView(R.id.usr_name)
    TextInputEditText oldPassword;
    @BindView(R.id.new_password)
    TextInputEditText newPassword;
    @BindView(R.id.identify_btn)
    Button identifyBtn;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    ConstraintLayout constraintLayout;
    String username;
    String password;
    String nickname;
    String phone;
    String sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedPreferences.getString("name", "");
        password = sharedPreferences.getString("password", "");
        nickname = sharedPreferences.getString("nickname", "");
        phone = sharedPreferences.getString("phone", "");
        sex = sharedPreferences.getString("sex", "");
        constraintLayout = (ConstraintLayout) findViewById(R.id.constrainlayout);
    }

    @OnClick({R.id.identify_btn, R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identify_btn:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ChangPasswordActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                String password_new = newPassword.getText().toString();
                String password_new_confirm = newPasswordConfirm.getText().toString();
                String password_old = oldPassword.getText().toString();
                if (!password.equals(password_old)) {
                    Toast.makeText(this, "旧密码不正确", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!password_new.equals(password_new_confirm)) {
                    Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    break;
                }
                User user = new User();
                user.setName(username);
                user.setPassword(password_new);
                user.setSex(sex.equals("男")?1:2);
                user.setPhonenumber(phone);
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
                        editor.putString("password", password_new);
                        editor.commit();
                        SnackbarUtil.LongSnackbar(constraintLayout, "更改成功", SnackbarUtil.Confirm).setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ChangPasswordActivity.this.finish();
                            }
                        }).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        SnackbarUtil.ShortSnackbar(constraintLayout, "网络原因导致失败，请稍后重试", SnackbarUtil.Warning).show();
                    }
                });
                break;
            case R.id.back_btn:
                this.finish();
                break;
        }
    }
}
