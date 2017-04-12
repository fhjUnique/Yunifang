package com.bwie.hhww.yunifang.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.bean.BeanDetails;

import java.util.List;

/**
 * Created by dell on 2017/3/28.
 */
public class DetailsListViewAdapter extends BaseAdapter {
    private Context context;
    private List<BeanDetails.Detailsactivity> list;

    public DetailsListViewAdapter(Context context, List<BeanDetails.Detailsactivity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHold viewHold;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.detais_listview_item,null);
            viewHold = new ViewHold();
            viewHold.title = (TextView) convertView.findViewById(R.id.text_details_item_title);
            convertView.setTag(viewHold);
        }else{
            viewHold = (ViewHold) convertView.getTag();
        }

        viewHold.title.setText(list.get(position).title);

        return convertView;
    }
    class ViewHold{
        TextView title;
    }
}
