package com.bwie.hhww.yunifang.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.bean.BeanDesc;
import com.bwie.hhww.yunifang.fragment.Details_XQ_Fragment;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by dell on 2017/3/28.
 */
public class XQ_lv_Adapter extends BaseAdapter {
    private Context context;
    private BeanDesc[] beanDesc;

    public XQ_lv_Adapter(Context context, BeanDesc[] beanDesc) {
        this.context = context;
        this.beanDesc = beanDesc;
    }

    @Override
    public int getCount() {
        return beanDesc.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.xq_fragment_lv_item,null);
            hold = new ViewHold();
            hold.image = (ImageView) convertView.findViewById(R.id.image_xq_item);
            convertView.setTag(hold);
        }else {
            hold = (ViewHold) convertView.getTag();
        }
//        Log.i("TAG", "getView: -----------"+beanDesc[position].getUrl());
//        Glide.with(context).load(beanDesc[position].getUrl()).
//                into(hold.image);
//        Glide.with(context).load(beanDesc[position].getUrl()).into(hold.image);
        ImageLoader.getInstance().displayImage(beanDesc[position].getUrl(),hold.image);
        return convertView;
    }
    class ViewHold{
        ImageView image;
    }
}
