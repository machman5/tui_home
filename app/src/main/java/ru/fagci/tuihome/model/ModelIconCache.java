package ru.fagci.tuihome.model;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ModelIconCache extends LruCache<String, Bitmap> {

    public ModelIconCache() {
        super(1024 * 1024);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }
/*
  @Override
  protected void entryRemoved( boolean evicted, String key, Bitmap oldValue, Bitmap newValue ) {
    oldValue.recycle();
  }
*/
}
