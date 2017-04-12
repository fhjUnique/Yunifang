package com.bwie.hhww.yunifang.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.activity.CommodityDetails;
import com.bwie.hhww.yunifang.bean.BeanDetails;

import java.util.List;

/**
 * Created by dell on 2017/3/23.
 */
public class DetailsPageAdapter extends PagerAdapter {
    private BeanDetails.Goods goods;
    private Context context;

    public DetailsPageAdapter(BeanDetails.Goods goods, Context context) {
        this.goods = goods;
        this.context = context;
    }

    @Override
    public int getCount() {
        return goods.gallery.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.page_layout, null);
        ImageView image_page_big = (ImageView) view.findViewById(R.id.image_page_big);
        ImageView image_page_small = (ImageView) view.findViewById(R.id.image_page_small);

        Glide.with(context).load(goods.gallery.get(position).normal_url).fitCenter().into(image_page_big);
        Glide.with(context).load(goods.watermarkUrl).fitCenter().into(image_page_small);
        container.addView(view);
        return view;
    }


}
