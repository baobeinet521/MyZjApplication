package com.minelibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.baseinterfacelibrary.BaseInterface;

import io.github.prototypez.appjoint.AppJoint;

public class MineActivity extends AppCompatActivity {
    private TextView mTest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout);
        mTest = findViewById(R.id.test_text);
        String textStr = AppJoint.service(BaseInterface.class).getNowString();
        if(!TextUtils.isEmpty(textStr)){
            mTest.setText(textStr);
        }

    }
}
