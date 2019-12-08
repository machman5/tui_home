package ru.fagci.tuihome.repository;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.AppModel;

public class AppRepository extends Repository {
    public AppRepository(Application application) {
        super();
        task = new AppLoaderTask(application, items, isLoading);
    }

    static class AppLoaderTask extends ModelLoaderTask {
        AppLoaderTask(@NonNull Application application, @NonNull MutableLiveData<ModelObjectMap> liveData, @NonNull MutableLiveData<Boolean> isLoading) {
            super(application, liveData, isLoading);
        }

        @Override
        @WorkerThread
        protected ModelObjectMap doInBackground(Void... voids) {
            PackageManager pm = getContext().getPackageManager();
            ModelObjectMap apps = new ModelObjectMap();
            for (ApplicationInfo info : pm.getInstalledApplications(0)) {
                if (isCancelled()) break;
                if (pm.getLaunchIntentForPackage(info.packageName) == null) continue;
                AppModel model = new AppModel(info, pm);
                apps.put(model.getUid(), model);
            }

            return apps;
        }
    }
}
