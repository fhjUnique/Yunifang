package com.bwie.hhww.yunifang.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.activity.CommodityDetails;
import com.bwie.hhww.yunifang.bean.BeanDetails;
import com.bwie.hhww.yunifang.bean.GoodsList;
import com.bwie.hhww.yunifang.holder.RWeekHolder;

import java.util.List;

/**
 * Created by dell on 2017/3/22.
 */

public class RSpecialAdapter extends RecyclerView.Adapter<RWeekHolder>{
    private Context context;
    private List<BeanDetails.GoodsRelDetails> goodsList;

    public RSpecialAdapter(List<BeanDetails.GoodsRelDetails> goodsList, Context context) {
        this.goodsList = goodsList;
        this.context = context;
    }

    @Override
    public RWeekHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_week_item, parent, false);
        RWeekHolder weekHolder = new RWeekHolder(view);
        return weekHolder;
    }

    @Override
    public void onBindViewHolder(RWeekHolder holder, final int position) {
        Glide.with(context).load(goodsList.get(position).goods_img).into(holder.image_week);
        holder.text_week_name.setText(goodsList.get(position).goods_name);
        holder.text_week_newprice.setText("￥"+goodsList.get(position).shop_price);
        holder.text_week_oldprice.setText("￥ 0");
        holder.text_week_newprice.setTextColor(Color.RED);
        holder.text_week_oldprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        holder.lin_recycle_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, CommodityDetails.class);
                it.putExtra("id",goodsList.get(position).id);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsList.size();
    }
}
