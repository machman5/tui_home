package ru.fagci.tuihome.repository;

import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.model.ModelObject;

import java.util.List;

public class Repository {
    protected MutableLiveData<List<? extends ModelObject>> items;

    public MutableLiveData<List<? extends ModelObject>> getItems() {
        return items;
    }
}
