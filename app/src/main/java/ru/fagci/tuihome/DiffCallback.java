package ru.fagci.tuihome;

import androidx.recyclerview.widget.DiffUtil;
import ru.fagci.tuihome.model.ModelObject;

import java.util.List;

public class DiffCallback extends DiffUtil.Callback {
    private List<ModelObject> oldItems;
    private List<ModelObject> newItems;

    public DiffCallback(List<ModelObject> oldItems, List<ModelObject> newItems) {
        this.oldItems = oldItems;
        this.newItems = newItems;
    }

    @Override
    public int getOldListSize() {
        return oldItems.size();
    }

    @Override
    public int getNewListSize() {
        return newItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).getUid().equals(newItems.get(newItemPosition).getUid());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItems.get(oldItemPosition).getUid().equals(newItems.get(newItemPosition).getUid());
    }
}
