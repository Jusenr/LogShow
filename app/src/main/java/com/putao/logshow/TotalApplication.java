package com.putao.logshow;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.jusenr.toolslibrary.AndroidTools;

/**
 * Description:
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/09/21
 * Time       : 17:20
 * Project    ：LogShow.
 */
public class TotalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(getApplicationContext());

        AndroidTools.init(getApplicationContext(), "log_show");
    }
}
