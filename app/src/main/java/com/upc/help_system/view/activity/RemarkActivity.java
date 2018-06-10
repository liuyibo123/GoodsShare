package com.upc.help_system.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.upc.help_system.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemarkActivity extends Activity {
    @BindView(R.id.imageButton)
    ImageButton back;
    @BindView(R.id.smile_rating)
    SmileRating smileRating;
    @BindView(R.id.editText)
    EditText remark;
    @BindView(R.id.makesure)
    Button makesure;
    @BindView(R.id.constraint_layout)
    ConstraintLayout constraintLayout;
    private int score;
    private final String TAG = "RemarkActivity";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "before set contentview ");
        setContentView(R.layout.activity_remark);
        ButterKnife.bind(this);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                switch (smiley) {
                    case SmileRating.BAD:
                        score =1;
                        break;
                    case SmileRating.GOOD:
                        score =2;
                        break;
                    case SmileRating.GREAT:
                        score =3;
                        break;
                    case SmileRating.OKAY:
                        score =4;
                        break;
                    case SmileRating.TERRIBLE:
                        score =5;
                        break;
                }
            }
        });
    }

    @OnClick({R.id.imageButton, R.id.makesure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                RemarkActivity.this.finish();
                break;
            case R.id.makesure:
                //TODO 点击确定
                break;
        }
    }
}
