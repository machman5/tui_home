package ru.fagci.tuihome;

import android.graphics.Bitmap;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;

public class BindingAdapters {
    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
