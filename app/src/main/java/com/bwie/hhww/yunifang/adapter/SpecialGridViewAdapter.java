package com.bwie.hhww.yunifang.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.bean.DefaultGoodsList;
import com.bwie.hhww.yunifang.bean.SubGoodsList;
import com.bwie.hhww.yunifang.bean.SubjectsBean;

import java.util.List;

/**
 * Created by dell on 2017/3/22.
 */
public class SpecialGridViewAdapter extends BaseAdapter{
    private Context context;
    private List<SubGoodsList> goodsLists;

    public SpecialGridViewAdapter(Context context, List<SubGoodsList> goodsLists) {
        this.context = context;
        this.goodsLists = goodsLists;
    }

    @Override
    public int getCount() {
        return goodsLists.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.home_gridview_item,null);
            viewHold = new ViewHold();
            viewHold.imageView = (ImageView) convertView.findViewById(R.id.home_gridView_image);
            viewHold.text_home_grid_efficacy = (TextView) convertView.findViewById(R.id.text_home_grid_efficacy);
            viewHold.text_home_grid_name = (TextView) convertView.findViewById(R.id.text_home_grid_name);
            viewHold.text_home_grid_new = (TextView) convertView.findViewById(R.id.text_home_grid_newprice);
            viewHold.text_home_grid_old = (TextView) convertView.findViewById(R.id.text_home_grid_oldprice);
            viewHold.smallImage = (ImageView) convertView.findViewById(R.id.home_gridView_small_image);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHold) convertView.getTag();
        }
        Glide.with(context).load(goodsLists.get(position).goods_img).into(viewHold.imageView);
        Glide.with(context).load(goodsLists.get(position).watermarkUrl).into(viewHold.smallImage);
        viewHold.text_home_grid_efficacy.setText(goodsLists.get(position).efficacy);
        viewHold.text_home_grid_name.setText(goodsLists.get(position).goods_name);
        viewHold.text_home_grid_old.setText("￥"+goodsLists.get(position).market_price);
        viewHold.text_home_grid_new.setText("￥"+goodsLists.get(position).shop_price);
        viewHold.text_home_grid_new.setTextColor(Color.RED);
        viewHold.text_home_grid_old.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线

        return convertView;
    }
    class ViewHold{
        ImageView imageView,smallImage;
        TextView text_home_grid_efficacy,text_home_grid_name,text_home_grid_old,text_home_grid_new;
    }
}
