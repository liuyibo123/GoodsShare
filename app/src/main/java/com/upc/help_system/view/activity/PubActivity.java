package com.upc.help_system.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageButton;
import android.widget.ScrollView;

import com.upc.help_system.R;
import com.upc.help_system.presenter.PubPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PubActivity extends FragmentActivity {
//    @BindView(R.id.tabs)
//    public TabLayout tabs;

    @BindView(R.id.frame_pub)
    public ScrollView framePub;
    PubPresenter presenter;
    @BindView(R.id.back_btn)
    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);
        ButterKnife.bind(this);

//        presenter = new PubPresenterImpl(this);
//        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                String s = tab.getText().toString();
//                presenter.OnTabClicked(s);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                presenter.OnTabClicked("取快递");
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        }
//        );

    }

    @OnClick(R.id.back_btn)
    public void onClick() {
        this.finish();
    }
}
