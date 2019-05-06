package com.xzz.hxjdglpt.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;

import com.xzz.hxjdglpt.activity.AlbumActivity;

public class ImageUtil {

    public static List<String> parse(String json) {
        List<String> list = new ArrayList<String>();
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray array = obj.getJSONArray("DataList");
            for (int i = 0; i < array.length(); i++) {
                list.add(array.optString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 启动图库
     *
     * @param activity
     */
    public static void startGallery(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media
                .EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, ConstantUtil.RESULT_LOAD_IMAGE);
//        Intent intent = new Intent(activity, AlbumActivity.class);
//        activity.startActivityForResult(intent,
//                ConstantUtil.RESULT_LOAD_IMAGE);
    }

    /**
     * 启动相机
     *
     * @param activity
     */
    public static void startCamera(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        activity.startActivityForResult(intent, ConstantUtil.TAKE_PHOTO_WITH_DATA);
    }

    /**
     * 启动相机
     *
     * @param activity
     */
    public static void startCamera(Activity activity, File file) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 判断存储卡是否可以用，可用进行存储
        if (CameraUtil.hasSdcard()) {
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                Uri uri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            } else {
                String authority = activity.getPackageName() + ".provider";
                Uri uri = FileProvider.getUriForFile(activity, authority, file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        activity.startActivityForResult(intent, ConstantUtil.TAKE_PHOTO_WITH_DATA);
    }


    /**
     * 根据从图库里选择的图片，查找到此图的缩略图路径
     *
     * @return
     */
    public static String getThumbnailsPathFromGallery(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        // 获取包含所需数据的Cursor对象
        Cursor cursor1 = context.getContentResolver().query(uri, projection, null, null, null);
        // 获取原图路径的索引
        int imageColumnIndex = cursor1.getColumnIndex(MediaStore.Images.Media.DATA);
        // 获取原图ID的索引
        int idColumnIndex = cursor1.getColumnIndex(MediaStore.Images.Media._ID);
        cursor1.moveToFirst();
        // 根据索引值获取图片路径
        String imagePath = cursor1.getString(imageColumnIndex);
        String id = cursor1.getString(idColumnIndex);
        cursor1.close();

        String thumbnailsPath = "";
        // 获取缩略图路径
        String[] projection2 = {MediaStore.Images.Thumbnails.DATA};
        Cursor cursor2 = context.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI,
                projection2, MediaStore.Images.Thumbnails.IMAGE_ID + "=?", new String[]{id}, null);

        if (cursor2 != null && cursor2.moveToFirst()) {
            int thumbnailsColumnIndex = cursor2.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            thumbnailsPath = cursor2.getString(thumbnailsColumnIndex);
        } else {
            thumbnailsPath = imagePath;
        }
        cursor2.close();

        return thumbnailsPath;
    }

    /**
     * 根据从图库里选择的图片，查找到此图路径
     *
     * @return
     */
    public static String getImagePathByGallery(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA};
        // 获取包含所需数据的Cursor对象
        Cursor cursor1 = context.getContentResolver().query(uri, projection, null, null, null);
        // 获取原图路径的索引
        int imageColumnIndex = cursor1.getColumnIndex(MediaStore.Images.Media.DATA);
        // 获取原图ID的索引
        cursor1.moveToFirst();
        // 根据索引值获取图片路径
        return cursor1.getString(imageColumnIndex);
    }

    public static String getThumbnailsPathFromCamera(Context context, Bitmap bitmap) {
        File dir = context.getCacheDir();
        String path = "";
        File file = new File(dir.getAbsolutePath() + File.separator + System.currentTimeMillis()
                + ".jpg");
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
                path = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    /**
     * 本地图片展示
     *
     * @param path
     * @param imageView
     */
    public static void loadLocalImage(String path, ImageView imageView) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(path));
            imageView.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地图片展示
     *
     * @param path
     * @param imageView
     * @param inSampleSize
     */
    public static void loadLocalImage(String path, ImageView imageView, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap photo = BitmapFactory.decodeFile(path, options);
        imageView.setImageBitmap(photo);
    }


    /**
     * 通过图片路径将图片进行转向
     *
     * @param fileName
     * @return bitmap
     */
    public static Bitmap setDirection(String fileName) {
        ExifInterface exif = null;
        Bitmap bitmap = null;
        try {
            exif = new ExifInterface(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }

        int degree = 0;
        if (exif != null) {
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface
                    .ORIENTATION_UNDEFINED);
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        }
        if (degree != 0) {
            Matrix m = new Matrix();
            m.postRotate(degree);
            Bitmap bm = BitmapFactory.decodeFile(fileName);
            bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            bm.recycle();
        } else {
            Matrix m = new Matrix();
            m.postRotate(90);
            Bitmap bm = BitmapFactory.decodeFile(fileName);
            bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            bm.recycle();
        }
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        return bitmap;
    }

    /**
     * 存放图片的目录
     *
     * @param context
     * @return
     */
    public static File picturesPath(Context context) {
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }


    public static Intent choosePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        return Intent.createChooser(intent, null);
    }

    public static String tempPath;

    public static Intent takeBigPicture(Context mContext) {
        //獲取系統版本
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (CameraUtil.hasSdcard()) {
            File tempFile = CompressImageUtil.createPicture(mContext);
            tempPath = tempFile.getAbsolutePath();
            if (currentapiVersion < 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFile);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media
                        .EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        return intent;
    }


    public static String retrievePath(Context context, Intent sourceIntent, Intent dataIntent) {
        String picPath = null;
        try {
            Uri uri;
            if (dataIntent != null) {
                uri = dataIntent.getData();
                if (uri != null) {
                    picPath = ContentUtil.getPath(context, uri);
                }
                if (isFileExists(picPath)) {
                    return picPath;
                }

            }

            if (sourceIntent != null) {
                uri = sourceIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                if (uri != null) {
                    String scheme = uri.getScheme();
                    if (scheme != null && scheme.startsWith("file")) {
                        picPath = uri.getPath();
                    }
                }
                if (!TextUtils.isEmpty(picPath)) {
                    File file = new File(picPath);
                    if (!file.exists() || !file.isFile()) {
                        LogUtil.i(String.format("retrievePath file not found from sourceIntent "
                                + "path:%s", picPath));
                    }
                }
            }
            return picPath;
        } finally {
            LogUtil.i("retrievePath(" + sourceIntent + "," + dataIntent + ") ret: " + picPath);
        }
    }


    private static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        if (!f.exists()) {
            return false;
        }
        return true;
    }
}
