package ru.fagci.tuihome.vm;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.model.ModelObject;

import java.util.ArrayList;
import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private MutableLiveData<List<ModelObject>> data;


    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<ModelObject>> getData() {
        if (data == null) {
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }

    private void loadData() {
        final PackageManager pm = getApplication().getPackageManager();
        final List<ModelObject> entries = new ArrayList<>();

        for (ApplicationInfo info : pm.getInstalledApplications(0)) {
            if (pm.getLaunchIntentForPackage(info.packageName) == null) continue;
            entries.add(new AppModel(info, pm));
        }
        data.postValue(entries);
    }
}
