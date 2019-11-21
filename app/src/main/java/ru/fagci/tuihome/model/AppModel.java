package ru.fagci.tuihome.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.action.ModelActionRun;
import ru.fagci.tuihome.loader.AppLoaderTask;

import java.util.List;
import java.util.regex.Pattern;

public class AppModel extends ModelObject {
    public ApplicationInfo appInfo;

    public AppModel(ApplicationInfo appInfo) {
        super(appInfo.loadLabel(AppLoaderTask.pm).toString(), appInfo.loadLabel(AppLoaderTask.pm).toString() + " " + appInfo.packageName);
        this.appInfo = appInfo;
        this.uid = getClass().getSimpleName() + ":" + appInfo.packageName;
    }

    static Bitmap getBitmapFromDrawable(Drawable drawable) {
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

    @Override
    public boolean search(String q, Pattern pattern) {
        boolean res = super.search(q, pattern);
        if (!pattern.matcher(name).find()) searchWeight *= 0.75;
        return res;
    }

    @Override
    public String getDescription() {
        return appInfo.packageName;
    }

    @Override
    public Bitmap createBitmap(Context context) {
        if (bitmap == null) {
            bitmap = getBitmapFromDrawable(appInfo.loadIcon(AppLoaderTask.pm));
        }
        return bitmap;
    }

    @Override
    public List<ModelAction> getAvailableActions() {
        List<ModelAction> aa = super.getAvailableActions();
        aa.add(0, new ModelActionRun());
        return aa;
    }

    @Override
    public int getColor() {
        return Color.rgb(128, 180, 200);
    }
}
