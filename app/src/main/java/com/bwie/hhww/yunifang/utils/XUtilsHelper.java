package com.bwie.hhww.yunifang.utils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by dell on 2017/3/14.
 */

public abstract class XUtilsHelper {
    public static void mHttpXutils(String path) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, path, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
//                getJson(result);
            }
            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    public abstract void getJson(String result);

}
