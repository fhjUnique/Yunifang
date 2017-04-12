package com.bwie.hhww.yunifang.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.adapter.SpecialGridViewAdapter;
import com.bwie.hhww.yunifang.bean.SubGoodsList;
import com.bwie.hhww.yunifang.bean.SubjectsBean;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

public class SpecialActivity extends AppCompatActivity {

    private TextView text_special_title,text_special_t;
    private String title;
    private String detail;
    private LinearLayout lin_special;
    private int width;
    private int height;
    private GridView special_grid;
    private List<SubGoodsList> subGoodsLists;
    private Button bt_special_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        //手机屏幕宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        initView();
        initHeight();
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        detail = bundle.getString("detail");
        subGoodsLists = (List<SubGoodsList>) bundle.getSerializable("subjectsesBean");
//        Log.i("subjectsesBean", "onCreate: ------"+ subGoodsLists);
        initData();
        bt_special_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initData(){
        text_special_title.setText("# "+title+" #");
        text_special_t.setText(detail);
        special_grid.setAdapter(new SpecialGridViewAdapter(this,subGoodsLists));
        setGridViewHeightBasedOnChildren(special_grid);

        special_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(SpecialActivity.this,CommodityDetails.class);
                it.putExtra("id",subGoodsLists.get(position).id);
                startActivity(it);
            }
        });

    }
    //给控件设置高度
    public void initHeight(){
        ViewGroup.LayoutParams params = lin_special.getLayoutParams();
        params.height = height / 3;
        lin_special.setLayoutParams(params);
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

    //初始化数据
    public void initView(){
        text_special_title = (TextView) findViewById(R.id.text_special_title);
        text_special_t = (TextView) findViewById(R.id.text_special_t);
        lin_special = (LinearLayout) findViewById(R.id.lin_special);
        special_grid = (GridView) findViewById(R.id.special_grid);
        bt_special_back = (Button) findViewById(R.id.bt_special_back);

    }
}
