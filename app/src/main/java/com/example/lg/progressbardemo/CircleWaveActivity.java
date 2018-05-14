package com.example.lg.progressbardemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.lg.progressbardemo.view.CircleWaveView;


public class CircleWaveActivity extends AppCompatActivity {

    private Button btn_wcv;
    private CircleWaveView wcv;
    private boolean toggleWcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_wave);

        wcv = (CircleWaveView) findViewById(R.id.wcv);

        btn_wcv = (Button) findViewById(R.id.btn_wcv);
        btn_wcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(!toggleWcv){
//                    wcv.start();
//                }else{
//                    wcv.stop();
//                }
//                toggleWcv = !toggleWcv;
                wcv.start();
            }
        });
    }
}
