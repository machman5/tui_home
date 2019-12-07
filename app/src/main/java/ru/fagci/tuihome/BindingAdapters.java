package ru.fagci.tuihome;

import android.graphics.Bitmap;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import ru.fagci.tuihome.model.ModelObject;

public class BindingAdapters {
    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, ModelObject modelObject) {
        imageView.setImageBitmap(modelObject.getBitmap(imageView.getContext()));
    }
}
