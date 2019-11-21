package ru.fagci.tuihome.comparator;

import ru.fagci.tuihome.model.ModelObject;

import java.text.Collator;
import java.util.Comparator;

public class AlphaComparator implements Comparator<ModelObject> {
    private final Collator sCollator = Collator.getInstance();

    @Override
    public int compare(ModelObject o1, ModelObject o2) {
        return sCollator.compare(o1.name, o2.name);
    }
}
