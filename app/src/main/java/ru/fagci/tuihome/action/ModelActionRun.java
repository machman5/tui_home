package ru.fagci.tuihome.action;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import ru.fagci.tuihome.model.AppModel;

public class ModelActionRun extends ModelAction {
    public ModelActionRun() {
        super(ACTION_RUN);
    }

    @Override
    public Object execute(Context context, Object modelObject) {
        if (modelObject instanceof AppModel) {
            AppModel m = (AppModel) modelObject;

            PackageManager pm = context.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(m.appInfo.packageName);
            if (launchIntent != null) {
                context.startActivity(launchIntent);//null pointer check in case package name was not found
            }
        }
        return modelObject;
    }
}
