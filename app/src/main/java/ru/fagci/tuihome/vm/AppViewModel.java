package ru.fagci.tuihome.vm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.repository.AppRepository;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private MutableLiveData<List<AppModel>> data;
    private AppRepository appRepository;


    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

    public LiveData<List<AppModel>> getData() {
        if (data == null) {
            data = appRepository.getApps();
        }
        return data;
    }
}
