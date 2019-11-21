package ru.fagci.tuihome.loader;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import ru.fagci.tuihome.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class AppLoaderTask extends ModelLoaderTask {
    public static PackageManager pm;

    public AppLoaderTask(Context context) {
        super(context);
        pm = context.getPackageManager();
    }

    @Override
    public List<AppModel> loadInBackground() {
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<AppModel> entries = new ArrayList<>();

        for (ApplicationInfo info : pm.getInstalledApplications(0)) {
            if (isLoadInBackgroundCanceled()) break;
            if (pm.getLaunchIntentForPackage(info.packageName) == null) continue;
            entries.add(new AppModel(info));
        }

        return entries;
    }
}
