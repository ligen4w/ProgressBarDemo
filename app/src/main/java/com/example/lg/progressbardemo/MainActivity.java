package com.example.lg.progressbardemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_circle_pb, R.id.btn_circle_wave, R.id.btn_linear_balls_loading, R.id.btn_circle_balls_loading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_circle_pb:
                startActivity(new Intent(MainActivity.this,CirclePbActivity.class));
                break;
            case R.id.btn_circle_wave:
                startActivity(new Intent(MainActivity.this,CircleWaveActivity.class));
                break;
            case R.id.btn_linear_balls_loading:
                startActivity(new Intent(MainActivity.this,LinearBallPbActivity.class));
                break;
            case R.id.btn_circle_balls_loading:
                startActivity(new Intent(MainActivity.this,CircleBallPbActivity.class));
                break;
        }
    }
}
