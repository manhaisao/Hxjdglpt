package com.xzz.hxjdglpt.volley;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import org.xutils.cache.LruCache;

public class BitmapCache implements ImageCache {
    private LruCache<String, Bitmap> mCache;

    private static BitmapCache myImageCache;

    private BitmapCache() {
        int maxSize = 5 * 1024 * 1024;
        // 获取当前运行的最大内存
        // int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        // final int cacheSize = maxMemory/4;//取四分之一作为图片缓存
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }

    public static BitmapCache getInstance() {
        if (myImageCache == null) {
            myImageCache = new BitmapCache();
        }
        return myImageCache;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);

    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if (getBitmap(url) == null) {
            mCache.put(url, bitmap);
        }
    }
}