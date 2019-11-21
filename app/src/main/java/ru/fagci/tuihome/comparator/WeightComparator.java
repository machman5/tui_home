package ru.fagci.tuihome.comparator;

import ru.fagci.tuihome.model.ModelObject;

import java.util.Comparator;

public class WeightComparator implements Comparator<ModelObject> {
    @Override
    public int compare(ModelObject p1, ModelObject p2) {
        return p2.searchWeight - p1.searchWeight;
    }
}
