package com.example.lg.progressbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.lg.progressbardemo.view.CircleProgressBarView;


public class CirclePbActivity extends AppCompatActivity {

    private CircleProgressBarView cpb;
    private Button btn_cpb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_pb);

        cpb = (CircleProgressBarView) findViewById(R.id.cpb);
        cpb.setmMaxProgress(100);
        btn_cpb = (Button) findViewById(R.id.btn_cpb);
        btn_cpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_cpb.setClickable(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for(int i = 0;i<100;i++){
                                Thread.sleep(50);
                                cpb.setProgress(i+1);
//                        Log.i("lg","progress = "+i);
                            }
                            btn_cpb.setClickable(true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
