package com.bwie.hhww.yunifang.holder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bwie.hhww.yunifang.R;

/**
 * Created by dell on 2017/3/22.
 */

public class RWeekHolder extends RecyclerView.ViewHolder{

    public final ImageView image_week;
    public final TextView text_week_name;
    public final TextView text_week_newprice;
    public final TextView text_week_oldprice;
    public final LinearLayout lin_recycle_week;
    public final ImageView image_week_small;

    public RWeekHolder(View itemView) {
        super(itemView);
        lin_recycle_week = (LinearLayout) itemView.findViewById(R.id.lin_recycle_week);
        image_week = (ImageView) itemView.findViewById(R.id.image_week);
        image_week_small = (ImageView) itemView.findViewById(R.id.image_week_small);
        text_week_name = (TextView) itemView.findViewById(R.id.text_week_name);
        text_week_newprice = (TextView) itemView.findViewById(R.id.text_week_newprice);
        text_week_oldprice = (TextView) itemView.findViewById(R.id.text_week_oldprice);

    }
}
