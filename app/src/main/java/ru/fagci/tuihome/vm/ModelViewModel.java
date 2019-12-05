package ru.fagci.tuihome.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.fagci.tuihome.model.ModelObject;
import ru.fagci.tuihome.repository.Repository;

import java.util.List;

public class ModelViewModel extends ViewModel {
    private MutableLiveData<List<? extends ModelObject>> data;
    private MutableLiveData<Boolean> isLoading;
    private Repository repository;

    public ModelViewModel(Repository repository) {
        super();
        this.repository = repository;
    }

    public LiveData<List<? extends ModelObject>> getData() {
        if (data == null) {
            data = repository.getItems();
        }
        return data;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
