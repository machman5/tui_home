package ru.fagci.tuihome.vm;

import android.util.Log;
import androidx.lifecycle.*;
import ru.fagci.tuihome.FilterState;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.model.ModelObject;

import java.util.Map;

public class MergedViewModel extends ViewModel {
    private MutableLiveData<FilterState> modelFilter = new MutableLiveData<>();
    private MediatorLiveData<ModelObjectMap> data = new MediatorLiveData<>();

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

    public void addDataSource(LiveData<ModelObjectMap> source) {
        data.addSource(source, modelObjects -> {
//            new AddDataSourceAsyncTask(data).execute(modelObjects);
            ModelObjectMap oldData = data.getValue();
            if (oldData == null) {
                data.postValue(modelObjects);
            } else {
                oldData.putAll(modelObjects);
                data.postValue(oldData);
            }

        });
        Log.i("Merged VM", "Add source: " + source);
    }
}
