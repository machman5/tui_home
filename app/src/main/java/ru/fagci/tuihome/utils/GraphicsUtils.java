package ru.fagci.tuihome.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;

import java.io.File;

public class GraphicsUtils {
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && drawable instanceof AdaptiveIconDrawable) {
            final Drawable[] drr = new Drawable[]{
                    ((AdaptiveIconDrawable) drawable).getBackground(),
                    ((AdaptiveIconDrawable) drawable).getForeground()
            };

            final LayerDrawable layerDrawable = new LayerDrawable(drr);

            int width = 96; //layerDrawable.getIntrinsicWidth();
            int height = 96; //layerDrawable.getIntrinsicHeight();

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);

            layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            layerDrawable.draw(canvas);

            return bitmap;
        }
        return null;
    }

    public static Bitmap getApkIcon(File file, Context context) {
        PackageInfo packageInfo = context.getPackageManager()
                .getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
        if (packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;

            appInfo.sourceDir = file.getPath();
            appInfo.publicSourceDir = file.getPath();

            Drawable icon = appInfo.loadIcon(context.getPackageManager());
            return GraphicsUtils.getBitmapFromDrawable(icon);
        }
        return null;
    }
}
