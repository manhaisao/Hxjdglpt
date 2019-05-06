package com.xzz.hxjdglpt.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.xzz.hxjdglpt.activity.BaseApplication;
import com.xzz.hxjdglpt.activity.R;
import com.xzz.hxjdglpt.customview.CircleImageView;
import com.xzz.hxjdglpt.utils.BitmapUtil;

public class VolleyImageLoader {

    /**
     * 通过ImageRequest来显示网络图片
     *
     * @param url
     * @param imageView
     */
    public static void setImageRequest(final Context context, String url, final ImageView imageView) {
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                imageView.setImageBitmap(BitmapUtil.readBitMap(context, R.mipmap.ad_load_fail));
            }
        });
        BaseApplication.getRequestQueue().add(imageRequest);
    }

    /**
     * 通过ImageRequest来显示网络图片
     *
     * @param url
     * @param view
     */
    public static void setViewBgRequest(String url, final View view) {
        ImageRequest imageRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(bitmap);
                        view.setBackground(drawable);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                view.setBackgroundResource(R.mipmap.ad_load_fail);
            }
        });
        BaseApplication.getRequestQueue().add(imageRequest);
    }

    /**
     * 通过ImageLoader来显示网络图片
     *
     * @param application
     * @param url
     * @param imageView
     * @param defaultImageResId
     * @param errorImageResId
     */
    public static void setImageLoader(BaseApplication application, String url,
                                      ImageView imageView, int defaultImageResId, int errorImageResId) {
        // ImageLoader loader = new
        // ImageLoader(BaseApplication.getRequestQueue(),
        // BitmapCache.getInstance());
        ImageLoader loader = application.getImageLoader();
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(
                imageView, defaultImageResId, errorImageResId);
        loader.get(url, imageListener);
    }

    /**
     * 通过Volley的NetWorkImageView来显示网络图片
     *
     * @param application
     * @param url
     * @param netWorkImageView
     * @param defaultImageResId
     * @param errorImageResId
     */
    public static void setNetWorkImageView(BaseApplication application,
                                           String url, NetworkImageView netWorkImageView,
                                           int defaultImageResId, int errorImageResId) {
        // ImageLoader loader = new
        // ImageLoader(BaseApplication.getRequestQueue(),
        // BitmapCache.getInstance());
        ImageLoader loader = application.getImageLoader();
        netWorkImageView.setDefaultImageResId(defaultImageResId);
        netWorkImageView.setErrorImageResId(errorImageResId);
        netWorkImageView.setImageUrl(url, loader);
    }
}
