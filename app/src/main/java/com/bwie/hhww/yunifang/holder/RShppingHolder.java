package com.bwie.hhww.yunifang.holder;

import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bwie.hhww.yunifang.R;

/**
 * Created by dell on 2017/3/29.
 */

public class RShppingHolder extends RecyclerView.ViewHolder {

    public final CheckBox checkBox_selecter_rv;
    public final ImageView image_shop_;
    public final TextView tv_shopping_titler, tv_shopping_price, tv_shopping_num, tv_shopping_nums;
    public final Button bt_shopping_add, bt_shopping_reduce;
    public final RelativeLayout rl_shop_recycler;
    public final LinearLayout lin_shopping_item;

    public RShppingHolder(View itemView) {
        super(itemView);
        checkBox_selecter_rv = (CheckBox) itemView.findViewById(R.id.checkBox_selecter_RV);
        image_shop_ = (ImageView) itemView.findViewById(R.id.image_shop_);
        tv_shopping_titler = (TextView) itemView.findViewById(R.id.tv_shopping_titler);
        tv_shopping_price = (TextView) itemView.findViewById(R.id.tv_shopping_price);
        tv_shopping_num = (TextView) itemView.findViewById(R.id.tv_shopping_num);
        tv_shopping_nums = (TextView) itemView.findViewById(R.id.tv_shopping_nums);
        bt_shopping_reduce = (Button) itemView.findViewById(R.id.bt_shopping_reduce);
        bt_shopping_add = (Button) itemView.findViewById(R.id.bt_shopping_add);
        rl_shop_recycler = (RelativeLayout) itemView.findViewById(R.id.RL_shop_recycler);
        lin_shopping_item = (LinearLayout) itemView.findViewById(R.id.lin_shopping_item);
    }
}
