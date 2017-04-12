package com.bwie.hhww.yunifang.bean;

import java.util.List;

/**
 * Created by dell on 2017/3/23.
 */

public class BeanDetails {
    public Data data;

    public class Data{
        public List<Detailsactivity> activity;
        public List<Comments> comments;
        public Goods goods;
        public List<GoodsRelDetails> goodsRelDetails;

    }

    public class Detailsactivity{
        public String description;
        public String id;
        public String title;

    }
    public class Comments{
        public String content;
        public String createtime;
        public String id;
        public User user;
    }
    public class Goods{
        public List<Attributes> attributes;
        public List<Gallery> gallery;
        public String efficacy;
        public String goods_name;
        public String market_price;
        public float shop_price;
        public String watermarkUrl;
        public String shipping_fee;
        public String collect_count;
        public String sales_volume;
        public String goods_desc;

    }
    public class GoodsRelDetails{
        public String goods_img;
        public String goods_name;
        public String id;
        public String shop_price;

    }

    public class User{
        public String icon;
        public String id;
        public String nick_name;
    }
    public class Attributes{
        public  String attr_name;
        public  String attr_value;
        public  String id;
        public  String goods_id;
    }
    public class Gallery{
        public  String normal_url;
    }
}
