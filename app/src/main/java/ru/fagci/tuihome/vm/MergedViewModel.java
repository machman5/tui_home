package ru.fagci.tuihome.vm;

import androidx.lifecycle.*;
import ru.fagci.tuihome.FilterState;
import ru.fagci.tuihome.model.ModelObject;

import java.util.List;

public class MergedViewModel extends ViewModel {
    private MediatorLiveData<List<? extends ModelObject>> _data = new MediatorLiveData<>();
    private MediatorLiveData<List<? extends ModelObject>> data = new MediatorLiveData<>();
    MutableLiveData<FilterState> modelFilter = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading;

    public LiveData<List<? extends ModelObject>> getData() {
        return Transformations.map(_data, input -> {
            return input;
        });
    }

    public void filterModel(FilterState filterState) {
        modelFilter.postValue(filterState);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void addModel(ModelViewModel viewModel) {
        _data.addSource(viewModel.getData(),);
    }
}
