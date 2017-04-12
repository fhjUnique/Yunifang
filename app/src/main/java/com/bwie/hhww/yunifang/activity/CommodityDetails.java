package com.bwie.hhww.yunifang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.adapter.DetailsListViewAdapter;
import com.bwie.hhww.yunifang.adapter.DetailsPageAdapter;
import com.bwie.hhww.yunifang.adapter.RSpecialAdapter;
import com.bwie.hhww.yunifang.bean.BeanDetails;
import com.bwie.hhww.yunifang.bean.UserDaoBean;
import com.bwie.hhww.yunifang.dao.UserDao;
import com.bwie.hhww.yunifang.fragment.ClassificationFragment;
import com.bwie.hhww.yunifang.fragment.Details_CS_Fragment;
import com.bwie.hhww.yunifang.fragment.Details_PL_Fragment;
import com.bwie.hhww.yunifang.fragment.Details_XQ_Fragment;
import com.bwie.hhww.yunifang.fragment.HomePageFragment;
import com.bwie.hhww.yunifang.fragment.MyFragment;
import com.bwie.hhww.yunifang.fragment.ShoppingFragment;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.youth.banner.Banner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CommodityDetails extends AppCompatActivity implements View.OnClickListener {
    private BeanDetails beanDetails;
    private List<String> imageArray = new ArrayList<>();
    private String id;
    private ViewPager viewPage_details;
    private LinearLayout ll;
    private int index;
    private int width;
    private int height;
    private RelativeLayout relative_viewpage;
    private BeanDetails.Goods goods;
    private TextView details_name, details_shop_price,
            details_market_price, details_text_yunfei,
            details_text_xiaoliang, details_text_shoucang;
    private Button bt_details_back;
    private RecyclerView recycler_details_tuijian;
    private TextView text_details_tuijian;
    private ListView listView_details;
    private Button bt_details_xiangqing, bt_details_canshu, bt_details_pinglun;
    private FragmentManager manager;
    private Button bt_details_goumai;
    private Button bt_details_gouwuche;
    private UserDao dao;

    private Details_XQ_Fragment xq_fragment;
    private Details_CS_Fragment cs_fragment;
    private Details_PL_Fragment pl_fragment;
    Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            beanDetails = (BeanDetails) msg.obj;


            //图片page
            imageViewpage();
            initData();
            //实满49元包邮。。。。。
            initDatas();
            //推荐商品
            recommend();
            //产品详情  参数 评论
            detailsFragment();
            //添加购物车
            addGouWuChe();
            bt_details_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
        //手机屏幕宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        dao = UserDao.getInstance(this);
        initView();
        Intent it = getIntent();
        id = it.getStringExtra("id");
        Network();
    }
    //添加购物车
    public void addGouWuChe() {
        final BeanDetails.Goods goods = beanDetails.data.goods;
        bt_details_gouwuche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UserDaoBean> select = dao.select(id);
                if (select.size() == 0) {
                    dao.insert(goods.gallery.get(0).normal_url, goods.shop_price, goods.goods_name, id, 1);
                } else {
                    Integer num = select.get(0).getNum();
                    num++;
                    dao.update(id, num);
                }
                Toast.makeText(CommodityDetails.this, "恭喜，添加购物车成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //网络获取
    public void Network() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, "http://m.yunifang.com/yunifang/mobile/goods/detail?random=46389&encode=70ed2ed2facd7a812ef46717b37148d6&id=" + id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Gson gson = new Gson();
                BeanDetails bean = gson.fromJson(result, BeanDetails.class);

                Message msg = Message.obtain();
                msg.obj = bean;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    //详情图片滑动
    public void imageViewpage() {
        goods = beanDetails.data.goods;
        addPoint();
        ViewGroup.LayoutParams params = relative_viewpage.getLayoutParams();
        params.height = height / 2;
        relative_viewpage.setLayoutParams(params);
        viewPage_details.setAdapter(new DetailsPageAdapter(goods, this));
        viewPage_details.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //得到代表当前页的小点
                View childAt = ll.getChildAt(position % goods.gallery.size());
                //把当前页的点变红
                childAt.setEnabled(false);
                //得到代表上一页的小点
                View childAt2 = ll.getChildAt(index % goods.gallery.size());

                childAt2.setEnabled(true);
                //把当前页的角标记录,在下一次使用时,这个值就是上次的角标位置
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //给图片加小圆点
    private void addPoint() {
        // 有多少个图片路径就有几个小圆点
        for (int i = 0; i < goods.gallery.size(); i++) {
            ImageView imageView = new ImageView(this);
            //给图片设置背景
            imageView.setBackgroundResource(R.drawable.selector);
            //得到布局属性对象.设置布局的宽和高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
            //设置右边距的距离是20个像素
            layoutParams.rightMargin = 20;
            //给控件设置属性
            imageView.setLayoutParams(layoutParams);
            if (i == 0) {
                imageView.setEnabled(false);
            }
            //把imageView添加到线性布局里
            ll.addView(imageView);
        }
    }

    //实满49元包邮。。。。。
    public void initDatas() {
        listView_details.setAdapter(new DetailsListViewAdapter(this, beanDetails.data.activity));
        listView_details.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(CommodityDetails.this, CarouselDetails.class);
                in.putExtra("title", beanDetails.data.activity.get(position).title);
                in.putExtra("url", beanDetails.data.activity.get(position).description);
                startActivity(in);
            }
        });
    }

    //推荐商品
    public void recommend() {
        if (null != beanDetails.data.goodsRelDetails) {
            text_details_tuijian.setVisibility(View.VISIBLE);
            recycler_details_tuijian.setLayoutManager(new GridLayoutManager(this, 3, RecyclerView.VERTICAL, false));
            recycler_details_tuijian.setAdapter(new RSpecialAdapter(beanDetails.data.goodsRelDetails, this));
        } else {
            text_details_tuijian.setVisibility(View.GONE);
        }

    }

    ////产品详情  参数 评论
    public void detailsFragment() {
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        xq_fragment = new Details_XQ_Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("desc", beanDetails.data.goods.goods_desc);
        xq_fragment.setArguments(bundle);
        transaction1.add(R.id.fl_details, xq_fragment);
        transaction1.commit();
    }

    public void initFragment(int index) {
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hidefragment(transaction);
        switch (index) {
            case 0:
                if (xq_fragment == null) {
                    xq_fragment = new Details_XQ_Fragment();
                    transaction.add(R.id.fl_details, xq_fragment);
                } else {
                    transaction.show(xq_fragment);
                }
                break;
            case 1:
                if (cs_fragment == null) {
                    cs_fragment = new Details_CS_Fragment();
                    transaction.add(R.id.fl_details, cs_fragment);
                } else {
                    transaction.show(cs_fragment);
                }
                break;
            case 2:
                if (pl_fragment == null) {
                    pl_fragment = new Details_PL_Fragment();
                    transaction.add(R.id.fl_details, pl_fragment);
                } else {
                    transaction.show(pl_fragment);
                }
                break;
        }
        transaction.commit();
    }

    public void hidefragment(FragmentTransaction transaction) {
        if (xq_fragment != null) {
            transaction.hide(xq_fragment);
        }
        if (cs_fragment != null) {
            transaction.hide(cs_fragment);
        }
        if (pl_fragment != null) {
            transaction.hide(pl_fragment);
        }
    }


    //赋值
    public void initData() {
        details_name.setText(beanDetails.data.goods.goods_name);
        details_shop_price.setText("￥" + beanDetails.data.goods.shop_price);
        details_market_price.setText("￥" + beanDetails.data.goods.market_price);
        details_text_yunfei.setText(beanDetails.data.goods.shipping_fee);
        details_text_xiaoliang.setText(beanDetails.data.goods.sales_volume);
        details_text_shoucang.setText(beanDetails.data.goods.collect_count);

        details_market_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
    }


    public void initView() {
        viewPage_details = (ViewPager) findViewById(R.id.viewPage_details);
        relative_viewpage = (RelativeLayout) findViewById(R.id.relative_viewpage);
        ll = (LinearLayout) findViewById(R.id.ll);
        bt_details_back = (Button) findViewById(R.id.bt_details_back);
        details_name = (TextView) findViewById(R.id.details_name);
        details_shop_price = (TextView) findViewById(R.id.details_shop_price);
        details_market_price = (TextView) findViewById(R.id.details_market_price);
        details_text_yunfei = (TextView) findViewById(R.id.details_text_yunfei);
        details_text_xiaoliang = (TextView) findViewById(R.id.details_text_xiaoliang);
        details_text_shoucang = (TextView) findViewById(R.id.details_text_shoucang);

        recycler_details_tuijian = (RecyclerView) findViewById(R.id.recycler_details_tuijian);
        text_details_tuijian = (TextView) findViewById(R.id.text_details_tuijian);
        listView_details = (ListView) findViewById(R.id.listView_details);

        bt_details_goumai = (Button) findViewById(R.id.bt_details_goumai);
        bt_details_gouwuche = (Button) findViewById(R.id.bt_details_gouwuche);

        bt_details_xiangqing = (Button) findViewById(R.id.bt_details_xiangqing);
        bt_details_canshu = (Button) findViewById(R.id.bt_details_canshu);
        bt_details_pinglun = (Button) findViewById(R.id.bt_details_pinglun);
        bt_details_xiangqing.setOnClickListener(this);
        bt_details_canshu.setOnClickListener(this);
        bt_details_pinglun.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        bt_details_xiangqing.setTextColor(Color.parseColor("#444444"));
        bt_details_canshu.setTextColor(Color.parseColor("#444444"));
        bt_details_pinglun.setTextColor(Color.parseColor("#444444"));
        switch (v.getId()) {
            case R.id.bt_details_xiangqing:
                bt_details_xiangqing.setTextColor(Color.parseColor("#ff0088"));
                initFragment(0);
                break;
            case R.id.bt_details_canshu:
                bt_details_canshu.setTextColor(Color.parseColor("#ff0088"));
                initFragment(1);
                break;
            case R.id.bt_details_pinglun:
                bt_details_pinglun.setTextColor(Color.parseColor("#ff0088"));
                initFragment(2);
                break;
        }
    }
}
