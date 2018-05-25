package com.upc.help_system.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.baidu.location.LocationClient;
import com.facebook.stetho.Stetho;
import com.upc.help_system.NewOrderEvent;
import com.upc.help_system.R;
import com.upc.help_system.presenter.MainPresenter;

import com.upc.help_system.utils.BDLocationUtil;
import com.upc.help_system.utils.SharedPreferenceUtil;
import com.upc.help_system.utils.widgetutil.SnackbarUtil;
import com.upc.help_system.view.fragment.OrdersFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;


public class MainActivity extends FragmentActivity {
    private final int SDK_PERMISSION_REQUEST = 127;
    @BindView(R.id.order)
    RadioButton order;
    @BindView(R.id.help)
    RadioButton help;
    @BindView(R.id.community)
    RadioButton community;
    private String permissionInfo;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fragment)
    public FrameLayout frameLayout;

    View headerView;
    MainPresenter presenter;
    public LocationClient mLocationClient = null;
    Button register;
    Button login;
    ImageButton img_btn;
    TextView nickname;
    String username = null;
    String sex;
    String nickname_string;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private OrdersFragment ordersFragment;
    private final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();

        headerView = navigationView.getHeaderView(0);
        register = (Button) headerView.findViewById(R.id.register);
        login = (Button) headerView.findViewById(R.id.login);
        img_btn = (ImageButton) headerView.findViewById(R.id.head_button);
        nickname = (TextView) headerView.findViewById(R.id.nickname);
        init();
        showOrders();
        //发布
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username == null || username.equals("")) {
                    Snackbar snackbar = SnackbarUtil.ShortSnackbar(drawerLayout, "发帖需要先登录", SnackbarUtil.Info).setActionTextColor(Color.RED).setAction("去登录", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                    snackbar.show();
                } else {
                    startActivity(new Intent(MainActivity.this, PubActivity.class));
                }
            }
        });
        //侧滑栏
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item_signout:
                        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear().apply();
                        clearString();
                        Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                        onResume();
                        break;
                    case R.id.navigation_item_change_password:

                        if (username == null || username.equals("")) {
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        startActivity(new Intent(MainActivity.this, ChangPasswordActivity.class));
                        break;
                    case R.id.navigation_item_setip:
                        EditText editText = new EditText(MainActivity.this);
                        editText.setText(SharedPreferenceUtil.getString("network","ip"));
                            new  AlertDialog.Builder(MainActivity.this)
                                    .setTitle("请输入IP地址:" )
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setView(editText)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String ip = editText.getText().toString();
                                            SharedPreferenceUtil.setString("network","ip",ip);
                                        }
                                    })
                                    .setNegativeButton("取消" ,  null )
                                    .show();
                }
                return true;
            }
        });


    }

    private void clearString() {
        this.username = "";
        sex = "";
        nickname_string = "";
    }

    private void init() {
       //presenter = new MainPresenterImpl(this);
        EventBus.getDefault().register(this);
        getPersimmions();
        Stetho.initializeWithDefaults(this);
        SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
        username = sharedPreferences1.getString("username", "");
        help.toggle();
        BDLocationUtil.getCurrentLoc();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username != null && !username.equals("")) {
                    Toast.makeText(MainActivity.this, "需要更换新的账号请先注销登陆", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            }
        });
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UpdateUserActivity.class));
            }
        });

    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Subscribe
    public void onNewOrder(NewOrderEvent event) {
        TSnackbar.make(this.getWindow().getDecorView(), event.msg, TSnackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.order, R.id.help, R.id.community, R.id.collect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order:

                break;
            case R.id.help:
                showOrders();
                break;
            case R.id.community:
                break;
            case R.id.collect:

                break;
        }
    }

    private void showOrders() {
        if (username != null && !username.equals("")) {
            transaction  = fragmentManager.beginTransaction();
            if(ordersFragment==null){
                ordersFragment = new OrdersFragment();

            }
            Log.d(TAG, "onClick: before fragment");
            transaction.replace(R.id.fragment,ordersFragment);
            Log.d(TAG, "onClick: end of transaction.replace");
            transaction.commit();
            Log.d(TAG, "onClick: end of commit");

        } else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            order.toggle();
            help.toggle();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        sex = sharedPreferences.getString("sex", "");
        username = sharedPreferences.getString("username", "");
        if (username == null || username.equals("")) {
            nickname.setText("未登录");
        } else {
            nickname.setText(username);
        }
        if (sex.equals("男")) {
            Resources resources = getApplicationContext().getResources();
            Drawable drawable = resources.getDrawable(R.drawable.ic_boy_128);
            img_btn.setBackground(drawable);
        }
        if (sex.equals("女")) {
            Resources resources = getApplicationContext().getResources();
            Drawable drawable = resources.getDrawable(R.drawable.ic_girl_48);
            img_btn.setBackground(drawable);
        }

    }
}



