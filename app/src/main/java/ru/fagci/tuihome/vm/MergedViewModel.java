package ru.fagci.tuihome.vm;

import android.util.Log;
import androidx.lifecycle.*;
import ru.fagci.tuihome.FilterState;
import ru.fagci.tuihome.model.ModelObject;

import java.util.ArrayList;
import java.util.List;

public class MergedViewModel extends ViewModel {
    private MediatorLiveData<List<? extends ModelObject>> data = new MediatorLiveData<>();
    MutableLiveData<FilterState> modelFilter = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading;

    public LiveData<List<? extends ModelObject>> getData() {
        return Transformations.map(data, input -> {
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

    public void addDataSource(LiveData<List<? extends ModelObject>> source) {
        data.removeSource(source);
        data.addSource(source, modelObjects -> data.setValue(modelObjects));
        Log.i("Merged VM", "Add source: " + source);
    }
}
