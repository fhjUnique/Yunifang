package com.bwie.hhww.yunifang.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bwie.hhww.yunifang.bean.UserDaoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/3/29.
 */

public class UserDao {
    private final SQLiteDatabase database;
    private String Sid;
    private Integer num;
    private float price;
    private String image;
    private String title;

    private UserDao(Context context){
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(context);
        database = helper.getReadableDatabase();
    }
    static UserDao userDao = null;
    public static synchronized UserDao getInstance(Context context){
        if(userDao == null){
            userDao = new UserDao(context);
        }
        return userDao;
    }
    //添加数据库
    public void insert(String image,float price,String title,String Sid,Integer num){
        ContentValues values = new ContentValues();
        values.put("image",image);
        values.put("price",price);
        values.put("title",title);
        values.put("Sid",Sid);
        values.put("num",num);
        database.insert("user",null,values);
    }
    //查询数据库id
    public List<UserDaoBean> select(String id){
        List<UserDaoBean> list = new ArrayList<>();
        Cursor cursor = database.query("user", new String[]{"Sid","num"}, "Sid = ?", new String[]{id}, null, null, null);
        while(cursor.moveToNext()){
            Sid = cursor.getString(cursor.getColumnIndex("Sid"));
            num = cursor.getInt(cursor.getColumnIndex("num"));
            list.add(new UserDaoBean(null,null,0,num,Sid));
        }
        return list;
    }
    //改变数据库num
    public void update(String id,Integer num){
        ContentValues values = new ContentValues();
        values.put("num", num);//key为字段名，value为值
        database.update("user", values, "Sid=?", new String[]{id});
    }

    //查询数据库
    public List<UserDaoBean> select(){
        List<UserDaoBean> list = new ArrayList<>();
        Cursor cursor = database.query("user", null, null,null, null, null, null);
        while(cursor.moveToNext()){
            Sid = cursor.getString(cursor.getColumnIndex("Sid"));
            num = cursor.getInt(cursor.getColumnIndex("num"));
            price = cursor.getFloat(cursor.getColumnIndex("price"));
            image = cursor.getString(cursor.getColumnIndex("image"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            list.add(new UserDaoBean(title,image,price,num,Sid));
        }
        return list;
    }
    //删除
    public void delete(String id){
        database.delete("user","Sid = ?",new String[]{id});
    }
}
