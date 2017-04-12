package com.bwie.hhww.yunifang.bean;

import java.util.List;

/**
 * Created by dell on 2017/3/19.
 */

public class BestSellersBean {
    public String name;
    public List<GoodsList> goodsList;

    public BestSellersBean(String name, List<GoodsList> goodsList) {
        this.name = name;
        this.goodsList = goodsList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GoodsList> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsList> goodsList) {
        this.goodsList = goodsList;
    }
}
