package com.bwie.hhww.yunifang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.adapter.XQ_lv_Adapter;
import com.bwie.hhww.yunifang.bean.BeanDesc;
import com.google.gson.Gson;

/**
 * Created by dell on 2017/3/28.
 */

public class Details_XQ_Fragment extends Fragment{

    private ListView lv_xq_fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.details_xq_fragment, null);
        lv_xq_fragment = (ListView) view.findViewById(R.id.lv_xq_fragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        String desc = bundle.getString("desc");
        Gson gson = new Gson();
        BeanDesc[] beanDesc = gson.fromJson(desc, BeanDesc[].class);
        Log.i("TAG", "onActivityCreated: "+beanDesc[1].getUrl());
        setListViewHeightBasedOnChildren(lv_xq_fragment);

        lv_xq_fragment.setAdapter(new XQ_lv_Adapter(getActivity(),beanDesc));

    }
    //获取listView的高度
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
