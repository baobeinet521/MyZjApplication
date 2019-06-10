package com.myzjapplication.util;

import com.baseinterfacelibrary.BaseInterface;

import io.github.prototypez.appjoint.core.ServiceProvider;

@ServiceProvider
public class Utils implements BaseInterface {

    @Override
    public String getNowString() {
        return "调用我写的getString方法";
    }


    public static String getStringFromCC(){
        return "通过CC的方式调用app的方法";
    }
}
