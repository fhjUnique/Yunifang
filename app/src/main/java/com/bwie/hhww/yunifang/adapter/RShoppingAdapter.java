package com.bwie.hhww.yunifang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.activity.CommodityDetails;
import com.bwie.hhww.yunifang.bean.UserDaoBean;
import com.bwie.hhww.yunifang.fragment.ShoppingFragment;
import com.bwie.hhww.yunifang.holder.RShppingHolder;

import java.util.List;

/**
 * Created by dell on 2017/3/29.
 */

public class RShoppingAdapter extends RecyclerView.Adapter<RShppingHolder>{
    private Context context;
    private List<UserDaoBean> list;

    public RShoppingAdapter(Context context, List<UserDaoBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RShppingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_shop_item,parent,false);
        RShppingHolder shppingHolder = new RShppingHolder(view);
        return shppingHolder;
    }

    @Override
    public void onBindViewHolder(final RShppingHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.image_shop_);
        holder.tv_shopping_titler.setText(list.get(position).getName());
        holder.tv_shopping_price.setText("￥"+list.get(position).getPrice());
        holder.tv_shopping_num.setText("数量："+list.get(position).getNum());
        holder.rl_shop_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, CommodityDetails.class);
                in.putExtra("id",list.get(position).getId());
                context.startActivity(in);
            }
        });

        holder.checkBox_selecter_rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = holder.checkBox_selecter_rv.isChecked();
                list.get(position).setChecked(checked);
                //重新设置价格
                ShoppingFragment fragment = new ShoppingFragment();
                fragment.setPrice();

                if(checked){
                    holder.checkBox_selecter_rv.setBackgroundResource(R.drawable.check_checked);

                }else{
                    holder.checkBox_selecter_rv.setBackgroundResource(R.drawable.check_normal_3);
                }

            }
        });
        //设置是否选中
        holder.checkBox_selecter_rv.setChecked(list.get(position).isChecked());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
