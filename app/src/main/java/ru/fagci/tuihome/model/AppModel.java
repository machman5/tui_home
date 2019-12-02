package ru.fagci.tuihome.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.action.ModelActionRun;
import ru.fagci.tuihome.loader.AppLoaderTask;
import ru.fagci.tuihome.utils.GraphicsUtils;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

public class AppModel extends ModelObject {
    public ApplicationInfo appInfo;

    public AppModel(ApplicationInfo appInfo, PackageManager pm) {
        super(appInfo.loadLabel(pm).toString(), appInfo.loadLabel(pm).toString() + " " + appInfo.packageName);
        this.appInfo = appInfo;

        File file = new File(appInfo.publicSourceDir);
        size = file.length();
        lastModified = file.lastModified();


        uid = getClass().getSimpleName() + ":" + appInfo.packageName;
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
            bitmap = GraphicsUtils.getBitmapFromDrawable(appInfo.loadIcon(AppLoaderTask.pm));
        }
        return bitmap;
    }

    @Override
    public List<ModelAction> getAvailableActions() {
        List<ModelAction> aa = super.getAvailableActions();
        aa.add(0, new ModelActionRun());
        return aa;
    }
}
