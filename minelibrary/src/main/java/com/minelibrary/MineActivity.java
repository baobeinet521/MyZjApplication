package com.minelibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baseinterfacelibrary.BaseInterface;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;

import org.json.JSONException;
import org.json.JSONObject;

import io.github.prototypez.appjoint.AppJoint;

public class MineActivity extends AppCompatActivity {
    public static String TAG = "cclog";

    private TextView mTest;
    private TextView mCallAppMethod;
    private Button mCallAppMethodBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_layout);
        mTest = findViewById(R.id.test_text);
        mCallAppMethod = findViewById(R.id.call_app_method);
        mCallAppMethodBtn = findViewById(R.id.call_app_method_btn);
        String textStr = AppJoint.service(BaseInterface.class).getNowString();
        if (!TextUtils.isEmpty(textStr)) {
            mTest.setText(textStr);
        }

        mCallAppMethodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String callId = CC.obtainBuilder("ComponentMain")
                        .setActionName("getAppUtilsToCC")
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                //此onResult在主线程中运行
                                String toast = "getAppUtilsToCC " + (result.isSuccess() ? "success" : "failed");
                                Toast.makeText(MineActivity.this, toast, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onResult:    "  + result.toString());
                                String resultStr = result.toString();
                                if (!TextUtils.isEmpty(resultStr)) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(resultStr);
                                        JSONObject dataObject = (JSONObject) jsonObject.get("data");
                                        if(dataObject != null){
                                            String strName = (String) dataObject.get("strName");
                                            mCallAppMethod.setText(strName);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });


            }
        });

    }
}
