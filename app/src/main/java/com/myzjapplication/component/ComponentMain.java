package com.myzjapplication.component;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.CCUtil;
import com.billy.cc.core.component.IComponent;
import com.myzjapplication.activity.SecondActivity;
import com.myzjapplication.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ComponentMain implements IComponent {
    @Override
    public String getName() {
        //指定组件的名称
        return "ComponentMain";
    }

    @Override
    public boolean onCall(CC cc) {
        String actionName = cc.getActionName();
        switch (actionName) {
            case "SecondActivity": //响应actionName为"showActivity"的组件调用
                //跳转到页面：ActivityA
                CCUtil.navigateTo(cc, SecondActivity.class);
                //返回处理结果给调用方
                CC.sendCCResult(cc.getCallId(), CCResult.success());
                //同步方式实现（在return之前听过CC.sendCCResult()返回组件调用结果），return false
                return false;

            case "getAppUtilsToCC": //响应actionName为"getAppUtilsToCC"的组件调用
                String str = Utils.getStringFromCC();
                JSONObject jsonObject = new JSONObject();

                JSONObject jsonData = new JSONObject();
                try {
                    jsonData.put("strName",str);
                    jsonObject.put("data",jsonData);
//                    jsonObject.put("success",CCResult.success());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String jsonStr = jsonObject.toString();
                CCResult mCCResult = CCResult.fromString(jsonStr);
                //返回处理结果给调用方
                CC.sendCCResult(cc.getCallId(), mCCResult);
                //同步方式实现（在return之前听过CC.sendCCResult()返回组件调用结果），return false
                return false;
            default:
                //其它actionName当前组件暂时不能响应，可以通过如下方式返回状态码为-12的CCResult给调用方
                CC.sendCCResult(cc.getCallId(), CCResult.errorUnsupportedActionName());
                return false;
        }
    }
}
