package com.minelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;

public class ActivityA extends AppCompatActivity {
    private Button mBtn;
    private Button mMine;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_acticity);
        mBtn = findViewById(R.id.goto_main_second);
        mMine = findViewById(R.id.goto_mine);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callId = CC.obtainBuilder("ComponentMain")
                        .setActionName("SecondActivity")
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                //此onResult在主线程中运行
                                String toast = "SecondActivity " + (result.isSuccess() ? "success" : "failed");
                                Toast.makeText(ActivityA.this, toast, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        mMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityA.this,MineActivity.class);
                startActivity(intent);
            }
        });
    }
}
