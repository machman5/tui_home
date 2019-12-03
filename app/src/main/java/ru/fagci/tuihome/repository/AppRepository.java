package ru.fagci.tuihome.repository;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.receivers.PackageReceiver;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {
    private MutableLiveData<List<AppModel>> apps;
    private PackageReceiver receiver;

    public AppRepository(Application context) {
        apps = new MutableLiveData<>();
        AppLoaderTask task = new AppLoaderTask(context);
        task.loadInBackground();
    }

    public MutableLiveData<List<AppModel>> getApps() {
        return apps;
    }

    class AppLoaderTask extends ModelLoaderTask {
        AppLoaderTask(Context context) {
            super(context);
        }

        @Override
        public List<AppModel> loadInBackground() {
            PackageManager pm = context.getPackageManager();
            List<AppModel> entries = new ArrayList<>();
            for (ApplicationInfo info : pm.getInstalledApplications(0)) {
                if (isLoadInBackgroundCanceled()) break;
                if (pm.getLaunchIntentForPackage(info.packageName) == null) continue;
                entries.add(new AppModel(info, pm));
            }

            apps.postValue(entries);

            return entries;
        }
    }

}
