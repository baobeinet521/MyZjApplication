package com.myzjapplication.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
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
import com.myzjapplication.util.FilePathUtils;
import com.myzjapplication.util.SystemUtil;
import com.myzjapplication.util.TimeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtn = findViewById(R.id.goto_show);
        mGetAudioBtn = findViewById(R.id.get_audio_info_btn);
        mGotoStorePath = findViewById(R.id.goto_store_path);
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
                if (checkPermissionStore(MainActivity.this)) {
//                    getExternalAudioInfo();
//                    geInternalAudioInfo();
                    String path = getPath();
                    Log.d(TAG, " path111111   " + path);
                    vecFile.clear();
                    getFileName(path);
                    String wechatPath = getTecentPath();
                    getFileName(wechatPath);
                    if (vecFile != null) {
                        Log.d(TAG, "onClick: vecFile Size   " + vecFile.size());
                    }
//                    getFileNameNoFilter(wechatPath);
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
//        String path = getPath();
//        Log.d(TAG, "onCreate: path   " + path);

    }

    public String getPath() {
        File parent = Environment.getExternalStorageDirectory();
        File child = null;
        String brand = SystemUtil.getDeviceBrand();
        Log.w(TAG, "------------brand = " + brand);
        switch (brand) {
            case "Xiaomi":
                child = new File(parent, "MIUI/sound_recorder");
                break;
            case "OnePlus":
                child = new File(parent, "Record/SoundRecord");
                break;
            case "Meizu":
                child = new File(parent, "Recorder");
                break;
            case "htc":
                child = new File(parent, "My Documents/My recordings");
                break;
            case "HONOR":
                child = new File(parent, "Sounds");
                parent.getPath();
                Log.d(TAG, "华为sdcard getPath: " + parent.getPath());
                break;
            case "vivo":
                child = new File(parent, "Record");
                break;
        }
        if (child != null) {
            return child.getPath();
        } else {
            return "";
        }

    }

    public String getTecentPath() {
        File parent = Environment.getExternalStorageDirectory();
        File child = null;
        child = new File(parent, "tencent/MicroMsg/Download");
        String weChatPath = child.getPath();
        return weChatPath;
    }


    public Vector<FileDataBean> getFileName(String fileAbsolutePath) {
        Log.d(TAG, " getFileName   path   " + fileAbsolutePath);
        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            return null;
        }
        File[] subFile = file.listFiles();
        if (subFile == null) {
            Log.d(TAG, " getFileName   subFile  为空  ");
            return null;
        }
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            File _file = subFile[iFileLength];
            if (!_file.isDirectory() && _file.exists()) {
                String filename = _file.getName();
                if (!TextUtils.isEmpty(filename)) {
                    if (filename.endsWith(".mp3") || filename.endsWith(".wav")
                            || filename.endsWith(".m4a") || filename.endsWith(".amr")
                            || filename.endsWith(".aac")) {
                        FileDataBean data = getFileInfo(_file);
                        vecFile.add(data);
                    }
                }


            } else {
                getFileName(_file.getAbsolutePath());
            }
        }
        return vecFile;
    }

    /**
     * 获取文件时长
     * @return
     */
    public String getAudioTime(String path){
        File file = new File(path);

        MediaPlayer meidaPlayer = new MediaPlayer();

        try {
            meidaPlayer.setDataSource(file.getPath());
            meidaPlayer.prepare();
            long time = meidaPlayer.getDuration();//获得了视频的时长（以毫秒为单位）
            long timeSecond = time / 1000;
            String tineFormat =TimeUtils.secondsToString(timeSecond);
            Log.d(TAG, " getAudioTime   音频时长   " + timeSecond + "  转换后的时间 " +tineFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Vector<String> getFileNameNoFilter(String fileAbsolutePath) {
        Log.d(TAG, " getFileName   path   " + fileAbsolutePath);
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            return null;
        }
        File[] subFile = file.listFiles();
        if (subFile == null) {
            Log.d(TAG, " getFileName   subFile  为空  ");
            return null;
        }
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            File _file = subFile[iFileLength];
            if (!_file.isDirectory() && _file.exists()) {
                String filename = _file.getName();
                if (!TextUtils.isEmpty(filename)) {
                    getFileInfo(_file);
                }


            } else {
                getFileName(_file.getAbsolutePath());
            }
        }
        return vecFile;
    }

    public FileDataBean getFileInfo(File file) {
        FileDataBean dataBean = new FileDataBean();
        String filename = file.getName();
        dataBean.setFileName(filename);
        String filePath = file.getPath();
        dataBean.setFilePath(filePath);
        //获取 file大小 返回的是byte
        Double fileLen = Double.parseDouble(String.valueOf(file.length()));
        String fileCompany = "";
        double fileLenM = 0;
        if (fileLen < 1000000) {
            fileLenM = fileLen / 1000;
            fileCompany = "KB";
        } else {
            fileLenM = fileLen / 1000 / 1000;
            fileCompany = "MB";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSize = df.format(fileLenM);
        dataBean.setFileSizeNoCompany(fileSize);
        dataBean.setFileSizeHasCompany(fileSize + fileCompany);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lastMofified = file.lastModified();
        String fileTime = dateFormat.format(lastMofified);
        dataBean.setFileTime(fileTime);
        Log.e(TAG, "eee  文件名 ： " + filename + "   文件大小   " + fileSize + fileCompany + "   文件修改时间   " + fileTime + "   文件路径   " + filePath);
        return dataBean;
    }

    /**
     * 获取指定文件大小
     * 可以获取得到
     *
     * @param
     * @return
     * @throws Exception
     */
    private long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
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
                    path = FilePathUtils.getPath(MainActivity.this, uri);
                    Log.d(TAG, "onActivityResult: 4.4以后 path " + path);
                    Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
                } else {//4.4以下下系统调用方法
                    path = FilePathUtils.getRealPathFromURI(this,uri);
                    Log.d(TAG, "onActivityResult: 4.4以前 path " + path);
                    Toast.makeText(MainActivity.this, path + "222222", Toast.LENGTH_SHORT).show();
                }
                getAudioTime(path);
            }


        }
    }
}
