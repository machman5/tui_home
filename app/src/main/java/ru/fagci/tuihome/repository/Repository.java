package ru.fagci.tuihome.repository;

import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.ModelObjectMap;

public class Repository {
    protected MutableLiveData<ModelObjectMap> items;

    public MutableLiveData<ModelObjectMap> getItems() {
        return items;
    }
}
