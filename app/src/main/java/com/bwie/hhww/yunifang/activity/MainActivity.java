package com.bwie.hhww.yunifang.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bwie.hhww.yunifang.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 冯虎杰
 * 欢迎页面
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager viewPage;
    private List<View> pageImage = new ArrayList<>();
    private SharedPreferences sp;
    private ImageView image_hello;
    private Button bt_tiaozhuan;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            if (what == 1) {
                image_hello.setVisibility(View.GONE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("flag", true);
                editor.commit();
                viewPage.setAdapter(new ViewPageAdapter());
                bt_tiaozhuan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(it);
                        finish();
                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        viewPage = (ViewPager) findViewById(R.id.viewpage);
        image_hello = (ImageView) findViewById(R.id.image_hello);


        pageImage.add(View.inflate(this, R.layout.pageone, null));
        pageImage.add(View.inflate(this, R.layout.pagetwo, null));
        pageImage.add(View.inflate(this, R.layout.pagethree, null));
        pageImage.add(View.inflate(this, R.layout.pagefour, null));
        View page5 = View.inflate(this, R.layout.pagefive, null);
        pageImage.add(page5);
        bt_tiaozhuan = (Button) page5.findViewById(R.id.bt_tiaozhuan);

        sp = getSharedPreferences("user", MODE_PRIVATE);
        boolean b = sp.getBoolean("flag", false);
        if (b) {
            final Intent it = new Intent(MainActivity.this, HomeActivity.class);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    startActivity(it); //执行
                    finish();
                }
            };
            timer.schedule(task, 1000 * 3);


        } else {
            handler.sendEmptyMessageDelayed(1, 3000);

        }


    }

    class ViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageImage.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageImage.get(position));
            return pageImage.get(position);
        }
    }

}
