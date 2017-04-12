package com.bwie.hhww.yunifang.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.activity.CarouselDetails;
import com.bwie.hhww.yunifang.activity.CommodityDetails;
import com.bwie.hhww.yunifang.adapter.HomeGridViewAdapter;
import com.bwie.hhww.yunifang.adapter.Hot_TopicsAdapter;
import com.bwie.hhww.yunifang.adapter.RHotTopicsAdapter;
import com.bwie.hhww.yunifang.adapter.RWeekAdapter;
import com.bwie.hhww.yunifang.bean.ActivityInfo;
import com.bwie.hhww.yunifang.bean.ActivityInfoList;
import com.bwie.hhww.yunifang.bean.Ad1Bean;
import com.bwie.hhww.yunifang.bean.Ad5Bean;
import com.bwie.hhww.yunifang.bean.Bean;
import com.bwie.hhww.yunifang.bean.BeanData;
import com.bwie.hhww.yunifang.bean.BestSellersBean;
import com.bwie.hhww.yunifang.bean.DefaultGoodsList;
import com.bwie.hhww.yunifang.bean.GoodsList;
import com.bwie.hhww.yunifang.bean.SubGoodsList;
import com.bwie.hhww.yunifang.bean.SubjectsBean;
import com.bwie.hhww.yunifang.model.GlideImageLoader;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/3/15.
 */

public class HomePageFragment extends Fragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private View view;
    private String path = "http://m.yunifang.com/yunifang/mobile/home?random=84831&encode=9dd34239798e8cb22bf99a75d1882447";
    private List<String> imageArray = new ArrayList<>();
    private List<String> imageBanner = new ArrayList<>();
    private List<Ad5Bean> ad5Been;
    private List<BestSellersBean> bestSellersBeen;
    private Banner banner ;;
    private LinearLayout lin_qiandao;
    private LinearLayout lin_jifen;
    private LinearLayout lin_duihuan;
    private LinearLayout lin_true;
    private ImageView bt_qiandao;
    private ImageView bt_jifen;
    private ImageView bt_duihuan;
    private ImageView bt_true;
    private List<Ad1Bean> ad1Been;
    private List<SubjectsBean> subjectsesBean;
    private List<DefaultGoodsList> defaultGoodsList;
    private List<ActivityInfoList> activityInfoList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView_week;
    private ListView listView_hot_topics;
    private RecyclerView recyclerView_hot_topics;
    private List<SubGoodsList> goodsList;
    private GridView home_gridView;
    private Banner banner_activities;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bean bean = (Bean) msg.obj;
            BeanData data = bean.data;
            ActivityInfo activityInfo = data.activityInfo;
            activityInfoList = activityInfo.activityInfoList;
            ad1Been = data.ad1;
            ad5Been = data.ad5;
            bestSellersBeen = data.bestSellers;
            subjectsesBean = data.subjects;
            defaultGoodsList = data.defaultGoodsList;
            //无限轮播
            imageArray();
            ////今日签到，积分商城，兑换专区，真伪查询
            ad5();
            //本周热销
            bestSellers();
            //优惠活动
            activityInfo();
            //热门专题
            subjectses();
            //商品展示
            defaults();

        }




    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.homepage_fragment, null);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Network();
        refresh();

    }
    //刷新数据
    public void refresh(){
        //设置小圆圈颜色
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(this);

    }
    @Override
    public void onRefresh() {
        Network();
        swipeRefreshLayout.setRefreshing(false);
    }

    //网络获取
    public void Network(){
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, path, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                Bean bean = gson.fromJson(result, Bean.class);

                Message msg = Message.obtain();
                msg.obj = bean;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    //无限轮播
    public void imageArray() {
        imageArray.clear();
        for (int i = 0; i < ad1Been.size(); i++) {
            imageArray.add(ad1Been.get(i).image);
        }
        banner.setDelayTime(4000);
        banner.setBannerAnimation(Transformer.CubeOut);
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setImages(imageArray).setImageLoader(new GlideImageLoader()).start();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent it = new Intent(getActivity(), CarouselDetails.class);
                it.putExtra("url",ad1Been.get(position).ad_type_dynamic_data);
                it.putExtra("title",ad1Been.get(position).title);
                startActivity(it);
            }
        });

    }

    //今日签到，积分商城，兑换专区，真伪查询
    public void ad5(){
//        Log.i("TAG", "ad5:---------- "+ad5Been.get(1).image);
        Glide.with(getActivity()).load(ad5Been.get(0).image).into(bt_qiandao);
        Glide.with(getActivity()).load(ad5Been.get(1).image).into(bt_jifen);
        Glide.with(getActivity()).load(ad5Been.get(2).image).into(bt_duihuan);
        Glide.with(getActivity()).load(ad5Been.get(3).image).into(bt_true);
    }

    //本周热销
    public void bestSellers(){
        List<GoodsList> goodsList = bestSellersBeen.get(0).goodsList;

        recyclerView_week.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        recyclerView_week.setAdapter(new RWeekAdapter(goodsList,getActivity()));

    }

    //优惠活动
    public void activityInfo(){
        for (int i = 0; i < activityInfoList.size(); i++) {
            imageBanner.add(activityInfoList.get(i).activityImg);
        }
        banner_activities.setBannerAnimation(Transformer.CubeIn);
        banner_activities.setBannerStyle(BannerConfig.NOT_INDICATOR);
        banner_activities.setImages(imageBanner).setImageLoader(new GlideImageLoader()).start();
    }

    //热门专题
    public void subjectses(){
//        Log.i("TAG", "bestSellers: ---------------"+subjectsesBean.get(0).detail);
        listView_hot_topics.setAdapter(new Hot_TopicsAdapter(getActivity(),subjectsesBean));
        setListViewHeightBasedOnChildren(listView_hot_topics);
    }
    //获取listView的高度
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    //商品展示
    private void defaults() {
        home_gridView.setAdapter(new HomeGridViewAdapter(getActivity(),defaultGoodsList));
        setGridViewHeightBasedOnChildren(home_gridView);
        home_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(getActivity(), CommodityDetails.class);
                it.putExtra("id",defaultGoodsList.get(position).id);
                startActivity(it);
            }
        });
    }
    //获取GridView的高度
    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns=0;
        int horizontalBorderHeight=0;
        Class<?> clazz=gridView.getClass();
        try {
            //利用反射，取得每行显示的个数
            Field column=clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns=(Integer)column.get(gridView);
            //利用反射，取得横向分割线高度
            Field horizontalSpacing=clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight=(Integer)horizontalSpacing.get(gridView);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if(listAdapter.getCount()%columns>0){
            rows=listAdapter.getCount()/columns+1;
        }else {
            rows=listAdapter.getCount()/columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight+horizontalBorderHeight*(rows-1);//最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    //今日签到，积分商城，兑换专区，真伪查询 对应的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_fragment_qiandao:
                Intent intent_qiandao = new Intent(getActivity(), CarouselDetails.class);
                intent_qiandao.putExtra("url",ad5Been.get(0).ad_type_dynamic_data);
                intent_qiandao.putExtra("title",ad5Been.get(0).title);
                startActivity(intent_qiandao);
                break;
            case R.id.lin_fragment_jifen:
                Intent intent_jifen = new Intent(getActivity(), CarouselDetails.class);
                intent_jifen.putExtra("url",ad5Been.get(1).ad_type_dynamic_data);
                intent_jifen.putExtra("title",ad5Been.get(1).title);
                startActivity(intent_jifen);
                break;
            case R.id.lin_fragment_duihuan:
                Intent intent_duihuan = new Intent(getActivity(), CarouselDetails.class);
                intent_duihuan.putExtra("url",ad5Been.get(2).ad_type_dynamic_data);
                intent_duihuan.putExtra("title",ad5Been.get(2).title);
                startActivity(intent_duihuan);
                break;
            case R.id.lin_fragment_true:
                Intent intent_true = new Intent(getActivity(), CarouselDetails.class);
                intent_true.putExtra("url",ad5Been.get(3).ad_type_dynamic_data);
                intent_true.putExtra("title",ad5Been.get(3).title);
                startActivity(intent_true);
                break;
        }
    }
    //初始化控件
    public void initView(){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView_week = (RecyclerView) view.findViewById(R.id.recyclerView_week);
        listView_hot_topics = (ListView) view.findViewById(R.id.listView_hot_topics);
        home_gridView = (GridView) view.findViewById(R.id.home_gridView);
//        recyclerView_hot_topics = (RecyclerView) view.findViewById(R.id.recyclerView_hot_topics);

        banner = (Banner) view.findViewById(R.id.banner);
        banner_activities = (Banner) view.findViewById(R.id.banner_activities);

        lin_qiandao = (LinearLayout) view.findViewById(R.id.lin_fragment_qiandao);
        lin_jifen = (LinearLayout) view.findViewById(R.id.lin_fragment_jifen);
        lin_duihuan = (LinearLayout) view.findViewById(R.id.lin_fragment_duihuan);
        lin_true = (LinearLayout) view.findViewById(R.id.lin_fragment_true);
        bt_qiandao = (ImageView) view.findViewById(R.id.bt_qiandao);
        bt_jifen = (ImageView) view.findViewById(R.id.bt_jifen);
        bt_duihuan = (ImageView) view.findViewById(R.id.bt_duihuan);
        bt_true = (ImageView) view.findViewById(R.id.bt_true);

        lin_qiandao.setOnClickListener(this);
        lin_jifen.setOnClickListener(this);
        lin_duihuan.setOnClickListener(this);
        lin_true.setOnClickListener(this);

    }


}
