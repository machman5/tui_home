package ru.fagci.tuihome.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import ru.fagci.tuihome.model.AppModel;

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        String packageName = intent.getData().getSchemeSpecificPart();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {

            try {
                ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
                AppModel app = new AppModel(info, pm); // TODO: add an application model via repo
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            // TODO: RM app
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
            //TODO: update app info
        }
    }
}
