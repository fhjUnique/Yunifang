package com.bwie.hhww.yunifang.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.model.DataClearManager;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by dell on 2017/1/14.
 */

public class XiTongSheZhi extends Activity {

    private TextView settings_back;
    private RelativeLayout settings_compile;
    private Button settings_back_login;
    private RelativeLayout settings_push;
    private File cacheDir;
    private TextView tv_cache_size;
    private long totalSize;
    private RelativeLayout settings_cache;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xitongshezhi);
        initView();
        truefalse();

        call_phone();
        back();
        dialog();
        //清除缓存
        deletecacheDir();
        clearCache();
    }

    //传过来的数据
    public void truefalse() {
        final Intent it = getIntent();
        boolean b = it.getBooleanExtra("visi", false);
        if (b) {
            settings_compile.setVisibility(View.VISIBLE);
            settings_back_login.setVisibility(View.VISIBLE);
        } else {
            settings_compile.setVisibility(View.GONE);
            settings_back_login.setVisibility(View.GONE);
        }
    }

    //清除缓存
    public void deletecacheDir(){
        //人为的写入缓存信息
        cacheDir = getCacheDir();


        writeCache(cacheDir);
        //显示当前缓存的大小
        try {
            String size = DataClearManager.getCacheSize(cacheDir);
            tv_cache_size.setText("已缓存"+size);
        } catch (Exception e) {
            e.printStackTrace();
        }
//         String size=  getCacheSize(cacheDir);


        //缓存的大小-Cache Files
        //SD 当前放置缓存信息的目录
        //所有的 都删除
        //除了sharedPreference不删除

        //获取当前文件夹下边所有文件的总大小9087
    }

    //拨打电话
    public void call_phone() {
        settings_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(XiTongSheZhi.this);
                builder.setTitle("拨打电话");
                builder.setMessage("请问是否要拨打电话吗？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + "18310735800");
                        intent.setData(data);
                        if (ActivityCompat.checkSelfPermission(XiTongSheZhi.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });

    }

    //返回
    public void back(){
        settings_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b;
                if (settings_compile.getVisibility() == View.GONE) {
                    b = true;
                } else {
                    b = false;
                }
                Intent in = new Intent();
                in.putExtra("b", b);
                setResult(RESULT_OK, in);
                finish();
            }
        });
    }

    //点击退出弹出框
    public void dialog(){
        settings_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(XiTongSheZhi.this);
                builder.setTitle("退出确认");
                builder.setMessage("退出当前账号，将不能同步收藏，发布评论和云端分享等");
                builder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settings_compile.setVisibility(View.GONE);
                        settings_back_login.setVisibility(View.GONE);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    //初始化控件
    public void initView() {
        settings_back = (TextView) findViewById(R.id.settings_back);
        settings_compile = (RelativeLayout) findViewById(R.id.settings_compile);
        settings_back_login = (Button) findViewById(R.id.settings_back_login);
        settings_push = (RelativeLayout) findViewById(R.id.settings_push);
        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        settings_cache = (RelativeLayout) findViewById(R.id.settings_cache);
    }
    /**
     * 获取缓存目录下的文件大小
     *
     * @param cacheDir
     * @return
     */
    private String getCacheSize(File cacheDir) {
        //先置位
        totalSize = 0;

        getCacheSizeMethod(cacheDir);
        //字节大小转成字符串  222222     12KB
        return formartSize(totalSize);
    }

    private String formartSize(long totalSize) {
        //小于1K
        if (totalSize < 1024) {
            return totalSize + "字节";
        } else {
            if ((totalSize / 1024) < 1024) {
                //kb范围以内
                return totalSize / 1024 + "kb";
            }
        }
        return null;
    }

    /**
     * 递归判断
     *
     * @param cacheDir
     */
    private void getCacheSizeMethod(File cacheDir) {
        //获取当前所有的大小
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                //是文件夹
                if (file.isDirectory()) {
                    //继续获取
                    getCacheSizeMethod(file);
                } else {
                    //是文件---获取当前文件的大小
                    long length = file.length();
                    totalSize = totalSize + length;
                }
            }

        }
    }

    /**
     * 模拟存储缓存内容
     *
     * @param cacheDir
     */
    private void writeCache(File cacheDir) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(cacheDir, "aaaa.txt"));
            fileOutputStream.write("dhoadhlfajdlsjddddddddddddddddddakfsavoannvkawodqqqqqfnawdnfawnvvnkanvaovnjaonvalvnakdjfnajdvjjdjjdjdjdjdjdjdjdjdjdjdjakdnfaeo;aeo".getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(cacheDir, "uuuu");
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, "bbbb.txt"));
            fileOutputStream.write("dhoadhlfajdlsjddddddddddddddddddakfsavoannvkawodfnawdnfawnvvnkanvaovnjaonvalvnakdjfnajdvjjdjjdjdjdjdjdjdjdjdjdjdjakdnfaeo;aeo".getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 清除缓存
     *
     */
    public void clearCache() {
//        clearAllCache(cacheDir);
        //判断是否删光
//        String cacheSize = getCacheSize(cacheDir);
        //删除某个指定文件夹下的所有信息
        settings_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataClearManager.deleteFolderFile(cacheDir.getAbsolutePath(), true);
                String size = null;
                try {
                    size = DataClearManager.getCacheSize(cacheDir);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_cache_size.setText("已缓存"+size);
                Toast.makeText(XiTongSheZhi.this, "清除成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void clearAllCache(File cacheDir) {
        File[] files = cacheDir.listFiles();
        //uuuu列出所有  bbb.txt
        for (File file : files) {
            if (file.isDirectory()) {
                //如果是文件夹，还是得判断
                if (file.listFiles().length == 0) {
                    //空文件夹
                    file.delete();
                } else {
                    //继续递归删除文件
                    clearAllCache(file);
                }
            } else {
                file.delete();
            }
        }
        //删除uuu
        cacheDir.delete();
    }
}
