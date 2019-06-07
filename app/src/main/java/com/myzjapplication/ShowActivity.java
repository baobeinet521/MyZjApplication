package com.myzjapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;

public class ShowActivity extends AppCompatActivity {
    private Button mJumpBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_layout);
        mJumpBtn = findViewById(R.id.jump_to_a);
        mJumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String callId = CC.obtainBuilder("ComponentMine")
                        .setActionName("ActivityA")
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                //此onResult在主线程中运行
                                String toast = "showActivity " + (result.isSuccess() ? "success" : "failed");
                                Toast.makeText(ShowActivity.this, toast, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
