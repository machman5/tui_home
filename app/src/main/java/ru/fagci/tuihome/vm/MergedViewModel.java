package ru.fagci.tuihome.vm;

import androidx.lifecycle.*;
import ru.fagci.tuihome.FilterState;
import ru.fagci.tuihome.model.ModelObject;

import java.util.ArrayList;
import java.util.List;

public class MergedViewModel extends ViewModel {
    private MediatorLiveData<List<? extends ModelObject>> _data = new MediatorLiveData<>();
    private MediatorLiveData<List<? extends ModelObject>> data = new MediatorLiveData<>();
    MutableLiveData<FilterState> modelFilter = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading;

    public LiveData<List<? extends ModelObject>> getData() {
        return Transformations.map(_data, input -> {
            FilterState filterState = modelFilter.getValue();
            if (null == filterState) {
                return input;
            }
            List<ModelObject> buffer = new ArrayList<>();
            for (ModelObject item :
                    input) {
                if (item.search(filterState)) {
                    buffer.add(item);
                }
            }
            return buffer;
        });
    }

    public void filterModel(FilterState filterState) {
        modelFilter.postValue(filterState);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void addModel(ModelViewModel viewModel) {
        _data.addSource(viewModel.getData(), modelObjects -> {
            _data.setValue(modelObjects);
        });
    }
}
