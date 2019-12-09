package ru.fagci.tuihome.repository;

import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.loader.ModelLoaderTask;

public class Repository {
    protected MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    ModelLoaderTask task;
    MutableLiveData<ModelObjectMap> items = new MutableLiveData<>();
    private boolean isLoadInitiated = false;

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<ModelObjectMap> getItems() {
        if (!isLoadInitiated) {
            isLoadInitiated = true;
            isLoading.postValue(true);
            task.execute();
        }
        return items;
    }
}
