package com.bwie.hhww.yunifang.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.activity.CommodityDetails;
import com.bwie.hhww.yunifang.adapter.RShoppingAdapter;
import com.bwie.hhww.yunifang.bean.UserDaoBean;
import com.bwie.hhww.yunifang.dao.UserDao;
import com.bwie.hhww.yunifang.holder.RShppingHolder;

import java.util.List;

import static android.R.id.list;

/**
 * Created by dell on 2017/3/15.
 */

public class ShoppingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private View view;
    private RecyclerView shopping_recyclerView;
    private UserDao dao;
    private SwipeRefreshLayout swipeRefreshLayout_shop;
    private RShoppingAdapter rShoppingAdapter;
    private List<UserDaoBean> select;
    private TextView tv_shop_sum;
    private CheckBox checkBox_selecter;
    private Button bt_shopping_bianji;
    private Button bt_shopping_jiesuan;
    private TextView tv_shop_sum1;
    private Button bt_shopping_deleter;
    private boolean flag = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.shopping_fragmnet, null);
        initView();
        dao = UserDao.getInstance(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout_shop.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW);
        swipeRefreshLayout_shop.setOnRefreshListener(this);

        initData();
    }

    //刷新fragment
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }


    public void initData() {
        select = dao.select();
//         Log.i("TAG", "initData: "+select.get(0).getName());
        shopping_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rShoppingAdapter = new RShoppingAdapter();
        shopping_recyclerView.setAdapter(rShoppingAdapter);
        checkAll();


    }

    //全选
    public void checkAll() {
        checkBox_selecter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    for (int i = 0; i < select.size(); i++) {
                        select.get(i).setChecked(true);
                    }
                }else {
                    for (int i = 0; i < select.size(); i++) {
                        select.get(i).setChecked(false);
                    }
                }
                rShoppingAdapter.notifyDataSetChanged();
                setPrice();
            }
        });

    }

    /**
     * 修改价格
     */
    public void setPrice() {
        float price = 0;
        for (int i = 0; i < select.size(); i++) {
            boolean checked = select.get(i).isChecked();
            if (checked) {
                price = price + select.get(i).getPrice() * select.get(i).getNum();
            }
        }
        tv_shop_sum.setText("总计:" + price);
    }


    //下拉刷新
    @Override
    public void onRefresh() {
        initData();
        swipeRefreshLayout_shop.setRefreshing(false);
    }

    public void initView() {
        shopping_recyclerView = (RecyclerView) view.findViewById(R.id.shopping_recyclerView);
        swipeRefreshLayout_shop = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_shop);
        tv_shop_sum = (TextView) view.findViewById(R.id.tv_shop_sum);
        checkBox_selecter = (CheckBox) view.findViewById(R.id.checkBox_selecter);
        bt_shopping_bianji = (Button) view.findViewById(R.id.bt_shopping_bianji);
        bt_shopping_jiesuan = (Button) view.findViewById(R.id.bt_shopping_jiesuan);
        tv_shop_sum1 = (TextView) view.findViewById(R.id.tv_shop_sum);
        bt_shopping_deleter = (Button) view.findViewById(R.id.bt_shopping_deleter);

        bt_shopping_bianji.setOnClickListener(this);
        bt_shopping_jiesuan.setOnClickListener(this);
        tv_shop_sum.setOnClickListener(this);
        bt_shopping_deleter.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_shopping_bianji:
                if (flag) {
                    bt_shopping_bianji.setText("完成");
                    bt_shopping_deleter.setVisibility(View.VISIBLE);
                    bt_shopping_jiesuan.setVisibility(View.GONE);
                    tv_shop_sum.setVisibility(View.GONE);
                    flag = false;
                    rShoppingAdapter.notifyDataSetChanged();
                } else {
                    bt_shopping_bianji.setText("编辑");
                    bt_shopping_deleter.setVisibility(View.GONE);
                    bt_shopping_jiesuan.setVisibility(View.VISIBLE);
                    tv_shop_sum.setVisibility(View.VISIBLE);
                    flag = true;
                    rShoppingAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.bt_shopping_deleter:
                for (int i = 0; i < select.size(); i++) {
                    if (checkBox_selecter.isChecked() == true) {
                        select.clear();

                    } else {
                        select.remove(i);

//                        dao.delete(select.get(i).getId());
                    }
                    rShoppingAdapter.notifyDataSetChanged();
                }

                break;
        }
    }

    //适配器
    public class RShoppingAdapter extends RecyclerView.Adapter<RShppingHolder> {
        @Override
        public RShppingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_shop_item, parent, false);
            RShppingHolder shppingHolder = new RShppingHolder(view);
            return shppingHolder;
        }

        @Override
        public void onBindViewHolder(final RShppingHolder holder, final int position) {
            Glide.with(getActivity()).load(select.get(position).getImage()).into(holder.image_shop_);
            holder.tv_shopping_titler.setText(select.get(position).getName());
            holder.tv_shopping_price.setText("￥" + select.get(position).getPrice());
            holder.tv_shopping_num.setText("数量：" + select.get(position).getNum());
            holder.tv_shopping_nums.setText("" + select.get(position).getNum());
            holder.rl_shop_recycler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(getActivity(), CommodityDetails.class);
                    in.putExtra("id", select.get(position).getId());
                    startActivity(in);
                }
            });

            if (flag == true) {
                holder.lin_shopping_item.setVisibility(View.GONE);
                holder.tv_shopping_num.setVisibility(View.VISIBLE);
            } else {
                holder.lin_shopping_item.setVisibility(View.VISIBLE);
                holder.tv_shopping_num.setVisibility(View.GONE);
            }
            holder.checkBox_selecter_rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = holder.checkBox_selecter_rv.isChecked();
                    select.get(position).setChecked(checked);
                    setPrice();
                }
            });
            //设置是否选中
            holder.checkBox_selecter_rv.setChecked(select.get(position).isChecked());
        }

        @Override
        public int getItemCount() {
            return select.size();
        }
    }

}
