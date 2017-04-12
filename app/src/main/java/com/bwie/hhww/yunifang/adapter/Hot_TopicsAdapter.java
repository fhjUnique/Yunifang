package com.bwie.hhww.yunifang.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.activity.SpecialActivity;
import com.bwie.hhww.yunifang.bean.SubGoodsList;
import com.bwie.hhww.yunifang.bean.SubjectsBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell on 2017/3/22.
 */
public class Hot_TopicsAdapter extends BaseAdapter {
    private Context context;
    private List<SubjectsBean> subjectsesBean;

    public Hot_TopicsAdapter(Context context, List<SubjectsBean> subjectsesBean) {
        this.context = context;
        this.subjectsesBean = subjectsesBean;
    }

    @Override
    public int getCount() {
        Log.i("TAG", "getCount: ----------"+subjectsesBean.size());
        return subjectsesBean.size();
    }

    @Override
    public Object getItem(int position) {
        return subjectsesBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.hot_topics_item,null);
            viewHold = new ViewHold();
            viewHold.image_hot_topcis = (ImageView) convertView.findViewById(R.id.image_hot_topics_big);
            viewHold.recyclerView_hot_topics = (RecyclerView) convertView.findViewById(R.id.recyclerView_hot_topics);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHold) convertView.getTag();
        }

        Glide.with(context).load(subjectsesBean.get(position).image).into(viewHold.image_hot_topcis);
        List<SubGoodsList> goodsList = subjectsesBean.get(position).goodsList;
        viewHold.recyclerView_hot_topics.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        viewHold.recyclerView_hot_topics.setAdapter(new RHotTopicsAdapter(goodsList,context));

        viewHold.image_hot_topcis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, SpecialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("subjectsesBean", (Serializable) subjectsesBean.get(position).goodsList);
                bundle.putString("title",subjectsesBean.get(position).title);
                bundle.putString("detail",subjectsesBean.get(position).detail);
                it.putExtras(bundle);
                context.startActivity(it);
            }
        });
        return convertView;
    }
    class ViewHold{
        ImageView image_hot_topcis;
        RecyclerView recyclerView_hot_topics;
    }
}
