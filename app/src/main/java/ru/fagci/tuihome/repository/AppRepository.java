package ru.fagci.tuihome.repository;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class AppRepository extends Repository {
    public AppRepository(Application context) {
        items = new MutableLiveData<>();
        AppLoaderTask task = new AppLoaderTask(context);
        task.loadInBackground();
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

            items.postValue(entries);

            return entries;
        }
    }

}
