package ru.fagci.tuihome.model;
import android.util.*;
import android.graphics.*;

public class ModelIconCache extends LruCache<String, Bitmap> {

  public ModelIconCache() {
    super(1 * 1024 * 1024);
  }

  @Override
  protected int sizeOf( String key, Bitmap value ) {
    return value.getByteCount();
  }
/*
  @Override
  protected void entryRemoved( boolean evicted, String key, Bitmap oldValue, Bitmap newValue ) {
    oldValue.recycle();
  }
*/
}
