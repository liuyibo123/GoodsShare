package com.upc.help_system.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.upc.help_system.R;
import com.upc.help_system.listener.BaseUiListener;
import com.upc.help_system.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2017/4/8.
 */

public class RegisterActivity extends Activity {
    @BindView(R.id.usr_name)
    TextInputEditText username;
    @BindView(R.id.password)
    TextInputEditText password;
    @BindView(R.id.cofirm_password)
    TextInputEditText cofirmPassword;
    @BindView(R.id.identify_btn)
    Button identifyBtn;
    @BindView(R.id.qq_reg)
    ImageButton qqReg;
    @BindView(R.id.phone_reg)
    ImageButton phoneReg;
    InputMethodManager imm;
    String username_string;
    String password_string;
    String cofirm_password_string;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    public static String mAppid;
    public static Tencent mTencent;
    private UserInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mAppid = "101402889";
 //       mTencent = Tencent.createInstance(mAppid, this);
    }

    @OnClick({R.id.identify_btn, R.id.qq_reg, R.id.phone_reg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.identify_btn:
                imm.hideSoftInputFromWindow(RegisterActivity.this.getWindow().getDecorView().getWindowToken(), 0);
                register();
                break;
            case R.id.qq_reg:
//                qqlogin();
                break;
            case R.id.phone_reg:
                break;
        }
    }

    IUiListener loginListener = new BaseUiListener(this);

    //    private void qqlogin() {
//        if (!mTencent.isSessionValid()) {
//            mTencent.login(this, "all", loginListener);
//            isServerSideLogin = false;
//            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
//        } else {
//            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
//                mTencent.logout(this);
//                mTencent.login(this, "all", loginListener);
//                isServerSideLogin = false;
//                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
//                return;
//            }
//            mTencent.logout(this);
//            updateUserInfo();
//            updateLoginButton();
//        }
//    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {

                }
            } else if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;

            }
        }

    };

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread() {

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject) response;
                            if (json.has("figureurl")) {
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(this, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {

        }
    }
    private void register() {
        username_string = username.getText().toString();
        password_string = password.getText().toString();
        cofirm_password_string = cofirmPassword.getText().toString();
        switch (validate(username_string, password_string, cofirm_password_string)) {
            case 1:
                Intent intent = new Intent(RegisterActivity.this, PerfectInformation.class);
                intent.putExtra("name", username_string);
                intent.putExtra("password", password_string);
                startActivity(intent);
                this.finish();
                break;
            case 2:
                Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "用户名只能由字母,数字和_组成", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 1 成功
     * 2 密码输入不一致
     * 3.用户名不符合规范
     */
    private int validate(String u, String p, String c) {
        int len1 = u.length();
        int len2 = p.length();
        if (u.equals(p)) {
            System.out.println("用户名和密码重复");
            return 3;
        }
        if (!p.equals(c)) {
            System.out.println("两次密码不一致");
            return 2;
        }
        if (len1 < 6 || len1 > 16) {
            System.out.println("用户名限制在6-16");
            return 3;
        } else {
            boolean flag = false;
            Pattern pattern = Pattern.compile("[a-zA-Z0-9_.-s]*");
            if (!u.equals(p)) {
                if (u != null) {
                    Matcher matcher = pattern.matcher(u);
                    flag = matcher.matches();
                    if (flag == false)
                        return 3;
                }
            }

            return 1;
        }
    }


    @OnClick(R.id.back_btn)
    public void onClick() {
        this.finish();
    }
}
