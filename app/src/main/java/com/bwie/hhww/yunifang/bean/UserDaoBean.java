package com.bwie.hhww.yunifang.bean;

/**
 * Created by dell on 2017/3/29.
 */

public class UserDaoBean {
    private String name;
    private String image;
    private float price;
    private Integer num;
    private String id;
    private boolean isChecked;

    public UserDaoBean(String name, String image, float price, Integer num, String id) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.num = num;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
