package com.bwie.hhww.yunifang.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.fragment.ClassificationFragment;
import com.bwie.hhww.yunifang.fragment.HomePageFragment;
import com.bwie.hhww.yunifang.fragment.MyFragment;
import com.bwie.hhww.yunifang.fragment.ShoppingFragment;

/**
 * Created by dell on 2017/3/14.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bottom_tab_home_normal;
    private Button bottom_tab_classify_normal;
    private Button bottom_tab_shopping_normal;
    private Button bottom_tab_user_normal;
    private HomePageFragment homePage;
    private ClassificationFragment classification;
    private ShoppingFragment shopping;
    private MyFragment my;
    private FragmentManager manager;
    private TextView text_shouye;
    private TextView text_fenlei;
    private TextView text_gouwuche;
    private TextView text_my;
    private LinearLayout lin_shouye;
    private LinearLayout lin_fenlei;
    private LinearLayout lin_gouwuche;
    private LinearLayout lin_my;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        homePage = new HomePageFragment();
        transaction1.add(R.id.fl, homePage);
        transaction1.commit();
        initView();

    }

    public void initFragment(int index) {
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hidefragment(transaction);
        switch (index) {
            case 0:
                if (homePage == null) {
                    homePage = new HomePageFragment();
                    transaction.add(R.id.fl, homePage);
                } else {
                    transaction.show(homePage);
                }
                break;
            case 1:
                if (classification == null) {
                    classification = new ClassificationFragment();
                    transaction.add(R.id.fl, classification);
                } else {
                    transaction.show(classification);
                }
                break;
            case 2:
                if (shopping == null) {
                    shopping = new ShoppingFragment();
                    transaction.add(R.id.fl, shopping);
                } else {
                    transaction.show(shopping);
                }
                break;
            case 3:
                if (my == null) {
                    my = new MyFragment();
                    transaction.add(R.id.fl, my, "tag");
                } else {
                    transaction.show(my);
                }
                break;
        }
        transaction.commit();
    }

    public void hidefragment(FragmentTransaction transaction) {
        if (homePage != null) {
            transaction.hide(homePage);
        }
        if (classification != null) {
            transaction.hide(classification);
        }
        if (shopping != null) {
            transaction.hide(shopping);
        }
        if (my != null) {
            transaction.hide(my);
        }
    }



    @Override
    public void onClick(View v) {
        bottom_tab_home_normal.setBackgroundResource(R.drawable.bottom_tab_home_normal);
        bottom_tab_classify_normal.setBackgroundResource(R.drawable.bottom_tab_classify_normal);
        bottom_tab_shopping_normal.setBackgroundResource(R.drawable.bottom_tab_shopping_normal);
        bottom_tab_user_normal.setBackgroundResource(R.drawable.bottom_tab_user_normal);
        text_shouye.setTextColor(Color.parseColor("#444444"));
        text_fenlei.setTextColor(Color.parseColor("#444444"));
        text_gouwuche.setTextColor(Color.parseColor("#444444"));
        text_my.setTextColor(Color.parseColor("#444444"));
        switch (v.getId()) {
            case R.id.lin_shouye:
                bottom_tab_home_normal.setBackgroundResource(R.drawable.bottom_tab_home_selected);
                text_shouye.setTextColor(Color.parseColor("#ff3333"));
                initFragment(0);
                break;
            case R.id.lin_fenlei:
                bottom_tab_classify_normal.setBackgroundResource(R.drawable.bottom_tab_classify_selected);
                text_fenlei.setTextColor(Color.parseColor("#ff3333"));
                initFragment(1);
                break;
            case R.id.lin_gouwuche:
                bottom_tab_shopping_normal.setBackgroundResource(R.drawable.bottom_tab_shopping_selected);
                text_gouwuche.setTextColor(Color.parseColor("#ff3333"));
                initFragment(2);
                break;
            case R.id.lin_my:
                bottom_tab_user_normal.setBackgroundResource(R.drawable.bottom_tab_user_selected);
                text_my.setTextColor(Color.parseColor("#ff3333"));
                initFragment(3);
                break;
        }
    }


    public void initView() {
        lin_shouye = (LinearLayout) findViewById(R.id.lin_shouye);
        lin_fenlei = (LinearLayout) findViewById(R.id.lin_fenlei);
        lin_gouwuche = (LinearLayout) findViewById(R.id.lin_gouwuche);
        lin_my = (LinearLayout) findViewById(R.id.lin_my);

        bottom_tab_home_normal = (Button) findViewById(R.id.button_shouye);
        bottom_tab_classify_normal = (Button) findViewById(R.id.button_fenlei);
        bottom_tab_shopping_normal = (Button) findViewById(R.id.button_gouwuche);
        bottom_tab_user_normal = (Button) findViewById(R.id.button_my);
        text_shouye = (TextView) findViewById(R.id.text_shouye);
        text_fenlei = (TextView) findViewById(R.id.text_fenlei);
        text_gouwuche = (TextView) findViewById(R.id.text_gouwuche);
        text_my = (TextView) findViewById(R.id.text_my);
        lin_shouye.setOnClickListener(this);
        lin_fenlei.setOnClickListener(this);
        lin_gouwuche.setOnClickListener(this);
        lin_my.setOnClickListener(this);
        ;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragmentByTag = manager.findFragmentByTag("tag");
        fragmentByTag.onActivityResult(requestCode, resultCode, data);

    }
}
