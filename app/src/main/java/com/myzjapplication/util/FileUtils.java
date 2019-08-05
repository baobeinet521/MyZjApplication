package com.myzjapplication.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.myzjapplication.FileDataBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

public class FileUtils {
    public static String TAG = "audioInfo";
    private static Vector<FileDataBean> vecFile = new Vector<FileDataBean>();
    public static String getRealPathFromURI(Context context,Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4以上设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public static String getFilePath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }




    public static String getRecordFilePath() {
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

    public static String getTecentPath() {
        File parent = Environment.getExternalStorageDirectory();
        File child = null;
        child = new File(parent, "tencent/MicroMsg/Download");
        String weChatPath = child.getPath();
        return weChatPath;
    }


    public static Vector<FileDataBean> getFileInfoHasFilter(String fileAbsolutePath) {
        Log.d(TAG, " getFileInfoHasFilter   path   " + fileAbsolutePath);
        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            return null;
        }
        File[] subFile = file.listFiles();
        if (subFile == null) {
            Log.d(TAG, " getFileInfoHasFilter   subFile  为空  ");
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
                        FileDataBean data = getFileInfo(fileAbsolutePath,_file);
                        vecFile.add(data);
                    }
                }


            } else {
                getFileInfoHasFilter(_file.getAbsolutePath());
            }
        }
        return vecFile;
    }

    /**
     * 获取文件时长
     * @return
     */
    public static String getAudioTime(String path){
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Vector<String> getFileInfoNoFilter(String fileAbsolutePath) {
        Log.d(TAG, " getFileInfoNoFilter   path   " + fileAbsolutePath);
        Vector<String> vecFile = new Vector<String>();
        File file = new File(fileAbsolutePath);
        if (!file.exists()) {
            return null;
        }
        File[] subFile = file.listFiles();
        if (subFile == null) {
            Log.d(TAG, " getFileInfoNoFilter   subFile  为空  ");
            return null;
        }
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            File _file = subFile[iFileLength];
            if (!_file.isDirectory() && _file.exists()) {
                String filename = _file.getName();
                if (!TextUtils.isEmpty(filename)) {
                    getFileInfo(fileAbsolutePath,_file);
                }


            } else {
                getFileInfoHasFilter(_file.getAbsolutePath());
            }
        }
        return vecFile;
    }


    public static FileDataBean getFileInfo(String path, File file) {
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

//        try {
//            FileTime t= Files.readAttributes(Paths.get(path), BasicFileAttributes.class).creationTime();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
}
