package com.myzjapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.myzjapplication.FileDataBean;
import com.myzjapplication.R;
import com.myzjapplication.util.FileUtils;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String BRAND_XIAOMI = "Xiaomi";
    public static String BRAND_ONEPLUS = "OnePlus";
    public static String BRAND_MEIZU = "Meizu";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static String TAG = "audioInfo";
    private Vector<FileDataBean> vecFile = new Vector<FileDataBean>();
    private Button mBtn;
    private Button mGetAudioBtn;
    private Button mGotoStorePath;
    private Button mCeshiBtn;
    private Button mCheckContactsPreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.goto_show);
        mGetAudioBtn = findViewById(R.id.get_audio_info_btn);
        mGotoStorePath = findViewById(R.id.goto_store_path);
        mCeshiBtn = findViewById(R.id.ceshi_btn);
        mCheckContactsPreBtn = findViewById(R.id.ceshi_check_contacts_btn);

        mCheckContactsPreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkContact();
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                startActivity(intent);
            }
        });
        mGetAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "    点击获取按钮    ");
                if (checkPermissionStore(MainActivity.this)) {
                    getExternalAudioInfo();
                    geInternalAudioInfo();
                    Log.d(TAG, "onClick: 开始获取时间 -----");
                    String path = FileUtils.getRecordFilePath();
                    Log.d(TAG, " path111111   " + path);
                    vecFile.clear();
                    Vector<FileDataBean> record = FileUtils.getFileInfoHasFilter(path);
                    vecFile.addAll(record);
                    Log.d(TAG, " 录音文件数量 -----    " + record.size());
                    String callRecordFile = FileUtils.getCallRecordFilePath();
                    Vector<FileDataBean> callRecordFileData = FileUtils.getFileInfoHasFilter(callRecordFile);
                    if (callRecordFileData != null) {
                        vecFile.addAll(callRecordFileData);
                        Log.d(TAG, " 通话记录的文件数量 -----    " + callRecordFileData.size());
                    }
                    String wechatPath = FileUtils.getTecentPath();
                    Vector<FileDataBean> wechat = FileUtils.getFileInfoHasFilter(wechatPath);
                    Log.d(TAG, "onClick: 获取结束时间 -----");
                    if (wechat != null) {
                        vecFile.addAll(wechat);
                        Log.d(TAG, " 微信文件数量  -----   " + wechat.size());
                    }

                    if (vecFile != null) {
                        Log.d(TAG, "onClick: vecFile Size -----  " + vecFile.size());
                    }
//                    getFileInfoNoFilter(wechatPath);
                }


            }
        });
        mGotoStorePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
//        String path = getFilePath();
//        Log.d(TAG, "onCreate: path   " + path);

        mCeshiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击按钮", Toast.LENGTH_LONG).show();
                Log.d(TAG, "onClick: 点击按钮");
            }
        });
        mCeshiBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: 长按");
                return true;
            }
        });

    }


    public void getExternalAudioInfo() {
        //存储在sd卡上的音频文件
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int counter = cursor.getCount();
        String title1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

        Log.w(TAG, "存储在sd卡上的音频文件------------before looping, title = " + title1);
        for (int j = 0; j < counter; j++) {

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            Log.w(TAG, "title     " + title + "      displayName     " + displayName + "   size     " + size + "    data   " + data);
            cursor.moveToNext();
        }
        cursor.close();
    }

    public void geInternalAudioInfo() {
        //存储在手机内部存储器上
        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int counter = cursor.getCount();
        String title1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));

        Log.w(TAG, "存储在手机内部存储器上------------before looping, title = " + title1);
        for (int j = 0; j < counter; j++) {

            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            String size = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            Log.w(TAG, "title     " + title + "        displayName     " + displayName + "     size     " + size + "   data    " + data);
            cursor.moveToNext();
        }
        cursor.close();
    }


    public void checkContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            Log.d("检查联系人", "checkBt: ");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 2);
            }
        }
    }

    public boolean checkPermissionStore(Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    Log.d(TAG, "checkPermissionStore: ----------------------");
                } else {
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getExternalAudioInfo();
                    geInternalAudioInfo();
                } else {
                    Toast.makeText(MainActivity.this, "GET_ACCOUNTS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if (requestCode == 1) {
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    path = uri.getPath();
                    Log.d(TAG, "onActivityResult: 第三方软件 path " + path);
                    Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    path = FileUtils.getFilePath(MainActivity.this, uri);
                    Log.d(TAG, "onActivityResult: 4.4以后 path " + path);
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                } else {//4.4以下下系统调用方法
                    path = FileUtils.getRealPathFromURI(this, uri);
                    Log.d(TAG, "onActivityResult: 4.4以前 path " + path);
                    Toast.makeText(MainActivity.this, path + "222222", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(MainActivity.this, "没有获取的音频文件路径", Toast.LENGTH_LONG).show();
                }
//                FileUtils.getAudioTime(path);
            }


        }
    }
}
