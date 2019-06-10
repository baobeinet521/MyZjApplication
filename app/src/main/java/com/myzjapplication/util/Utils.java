package com.myzjapplication.util;

import com.baseinterfacelibrary.BaseInterface;

import io.github.prototypez.appjoint.core.ServiceProvider;

@ServiceProvider
public class Utils implements BaseInterface {

    @Override
    public String getNowString() {
        return "调用我写的getString方法";
    }
}
