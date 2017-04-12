package com.bwie.hhww.yunifang.fragment;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bwie.hhww.yunifang.R;
import com.bwie.hhww.yunifang.activity.ClipImageActivity;
import com.bwie.hhww.yunifang.activity.MainActivity;
import com.bwie.hhww.yunifang.activity.XiTongSheZhi;
import com.bwie.hhww.yunifang.model.GlideCircleTransform;
import com.bwie.hhww.yunifang.model.RoundImageView;
import com.bwie.hhww.yunifang.model.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dell on 2017/3/15.
 */

public class MyFragment extends Fragment{

    private View view;
    private RelativeLayout loginView;
    private CircleImageView img_login;
    private TextView qq_name;
    private Tencent mTencent;
    private IUiListener loginListener;
    private SharedPreferences sp;
    private ImageView my_shezhi;
    private String nickName;
    private String iconUrl;

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像1
    private CircleImageView headImage1;
    //调用照相机返回图片临时文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;
    private Bitmap bitMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.my_fragment,null);
        initView();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sp = getActivity().getSharedPreferences("user",MODE_PRIVATE);
        boolean b = sp.getBoolean("boo",false);

        Log.i("TAG", "onActivityCreated: "+b);
        if(b == true){
            nickName = sp.getString("nickName","");
            iconUrl = sp.getString("iconUrl","");
            qq_name.setText(nickName);
            boolean camera = sp.getBoolean("camera",false);
            String cropImagePath = sp.getString("cropImagePath", null);
            if(camera){
                bitMap = BitmapFactory.decodeFile(cropImagePath);
                img_login.setImageBitmap(bitMap);
            }else{
                Glide.with(getActivity()).load(iconUrl).transform(new GlideCircleTransform(getActivity())).into(img_login);
            }
            loginView.setVisibility(View.GONE);


        }else if(b == false){
            qq_name.setText("");
            Glide.with(getActivity()).load(R.mipmap.default_sdk_login).transform(new GlideCircleTransform(getActivity())).into(img_login);
            loginView.setVisibility(View.VISIBLE);
        }
        //更换头像
        img_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadHeadImage();
            }
        });
        login_();
        //系统设置
        my_shezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b;
                if (loginView.getVisibility() == View.GONE) {
                    b = true;
                } else {
                    b = false;

                }
                Intent it = new Intent(getActivity(), XiTongSheZhi.class);
                it.putExtra("visi", b);
                startActivityForResult(it, 1);
            }
        });
        //创建拍照存储的临时文件
        createCameraTempFile(savedInstanceState);
    }
    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCarema();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }
    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }
    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    //QQ第三方登录
    public void login_() {

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("boo",true);
                editor.commit();
                mTencent =  Tencent.createInstance("1105851865", getActivity());
                mTencent.login(getActivity(), "all", loginListener);
                loginListener = new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        //登录成功后回调该方法,可以跳转相关的页面

//                        Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                        JSONObject object = (JSONObject) o;
                        try {
                            String accessToken = object.getString("access_token");
                            String expires = object.getString("expires_in");
                            String openID = object.getString("openid");
                            mTencent.setAccessToken(accessToken, expires);
                            mTencent.setOpenId(openID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(UiError uiError) {
                    }
                    @Override
                    public void onCancel() {
                    }
                };
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        if (requestCode == Constants.REQUEST_LOGIN) {
            if (resultCode == -1) {
                Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
                Tencent.handleResultData(data, loginListener);
                UserInfo info = new UserInfo(getActivity(), mTencent.getQQToken());
                info.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        try {
                            JSONObject info = (JSONObject) o;
                            String nickName = info.getString("nickname");//获取用户昵称
                            //获取用户头像的url
                            String iconUrl = info.getString("figureurl_qq_2");
                            SharedPreferences.Editor edi = sp.edit();
                            edi.putString("iconUrl", iconUrl);
                            edi.putString("nickName", nickName);
                            edi.commit();
//                            Toast.makeText(getActivity(), "昵称：" + nickName, Toast.LENGTH_SHORT).show();
                            qq_name.setText(nickName);
                            Glide.with(getActivity()).load(iconUrl).transform(new GlideCircleTransform(getActivity())).into(img_login);
//                            id_linear_login.setVisibility(View.VISIBLE);
                            loginView.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(UiError uiError) {
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        }
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    boolean b = data.getBooleanExtra("b", false);
//                    Log.i("TAG", "onActivityResult: "+b);
                    if (b) {
                        Glide.with(getActivity()).load(R.mipmap.default_sdk_login).transform(new GlideCircleTransform(getActivity())).into(img_login);
                        loginView.setVisibility(View.VISIBLE);
                        qq_name.setText("");
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("boo",false);
                        editor.commit();
                    } else {
                        Glide.with(getActivity()).load(iconUrl).transform(new GlideCircleTransform(getActivity())).into(img_login);
                        qq_name.setText(nickName);
                        loginView.setVisibility(View.GONE);
                    }
                }
                break;
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = data.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getActivity().getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("camera",true);
                    editor.putString("cropImagePath",cropImagePath);
                    editor.commit();
                    img_login.setImageBitmap(bitMap);
                    if (type == 1) {
                        img_login.setImageBitmap(bitMap);
                    } else {
//                        headImage2.setImageBitmap(bitMap);
                    }
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }

    public void initView(){
        loginView = (RelativeLayout) view.findViewById(R.id.loginView);
        img_login = (CircleImageView) view.findViewById(R.id.img_login);
        qq_name = (TextView) view.findViewById(R.id.qq_name);
        my_shezhi = (ImageView) view.findViewById(R.id.my_shezhi);
    }


}
