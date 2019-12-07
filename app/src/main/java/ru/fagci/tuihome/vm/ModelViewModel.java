package ru.fagci.tuihome.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.repository.Repository;

public class ModelViewModel extends ViewModel {
    private MutableLiveData<ModelObjectMap> data;
    private MutableLiveData<Boolean> isLoading;
    private Repository repository;

    public ModelViewModel(Repository repository) {
        super();
        this.repository = repository;
    }

    public LiveData<ModelObjectMap> getData() {
        if (data == null) {
            data = repository.getItems();
        }
        return data;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
