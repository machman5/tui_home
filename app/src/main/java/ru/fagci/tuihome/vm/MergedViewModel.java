package ru.fagci.tuihome.vm;

import android.util.Log;
import androidx.lifecycle.*;
import ru.fagci.tuihome.FilterState;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.model.ModelObject;

import java.util.Map;

public class MergedViewModel extends ViewModel {
    MutableLiveData<FilterState> modelFilter = new MutableLiveData<>();
    ModelObjectMap items = new ModelObjectMap();
    private MediatorLiveData<ModelObjectMap> data = new MediatorLiveData<>();
    private MutableLiveData<Boolean> isLoading;

    public LiveData<ModelObjectMap> getData() {
        return Transformations.map(data, input -> {
            FilterState filterState = modelFilter.getValue();
            if (null == filterState) {
                return input;
            }
            ModelObjectMap buffer = new ModelObjectMap();
            for (Map.Entry<String, ModelObject> item : input.entrySet()) {
                if (item.getValue().search(filterState)) {
                    buffer.put(item.getKey(), item.getValue());
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

    public void addDataSource(LiveData<ModelObjectMap> source) {
        data.removeSource(source);
        data.addSource(source, modelObjects -> {
            items.putAll(modelObjects);
            data.setValue(items);
        });
        Log.i("Merged VM", "Add source: " + source);
    }
}
