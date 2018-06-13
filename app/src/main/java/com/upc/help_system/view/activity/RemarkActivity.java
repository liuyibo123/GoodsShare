package com.upc.help_system.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.upc.help_system.R;
import com.upc.help_system.utils.network.ConConfig;
import com.upc.help_system.utils.network.RequestService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemarkActivity extends AppCompatActivity {

    @BindView(R.id.imageButton)
    ImageButton back;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.makesure)
    Button makesure;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;
    private float score;
    private  final String TAG = "RemarkActivity";
    private int goodsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        ButterKnife.bind(this);
        Intent i = getIntent();
        goodsId = i.getIntExtra("goodsId",0);
        ratingBar.setNumStars(5);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = rating;
//                Log.d(TAG, "onRatingChanged: "+score);
            }
        });

    }

    @OnClick({R.id.imageButton, R.id.makesure, R.id.ratingBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                RemarkActivity.this.finish();
                break;
            case R.id.makesure:
                JsonObject obj = new JsonObject();
                obj.addProperty("goodsId",goodsId);
                obj.addProperty("score",score);
                obj.addProperty("remark",editText.getText().toString());
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(ConConfig.url)
                        .build();
                RequestService requestService = retrofit.create(RequestService.class);
                Call<String> call = requestService.remark(obj);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Toast.makeText(RemarkActivity.this,"评论成功",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(RemarkActivity.this,"评论失败",Toast.LENGTH_LONG).show();
                    }
                });

                break;
            case R.id.ratingBar:
                break;
        }
    }
}
