package com.bwie.hhww.yunifang.application;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by dell on 2017/3/18.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
        imageLoader.init(builder.build());
    }
}
