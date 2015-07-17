package com.karan.bikedekhoproject;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by karanahuja on 13/07/15.
 */
public class LRUBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    /*LRU Bitmap Cache implemets Volley's ImageCache which basically defines caching space inRam*/

    public static int getDefaultCacheSize(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    public LRUBitmapCache(){
        this(getDefaultCacheSize());
    }

    public LRUBitmapCache (int sizeInKb){
        super(sizeInKb);
    }

    @Override
    protected int sizeOf(String key, Bitmap value){
      return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
