package ru.fagci.tuihome;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import ru.fagci.tuihome.databinding.ListModelItemBinding;
import ru.fagci.tuihome.model.ModelObject;

import java.util.LinkedList;
import java.util.List;

public class ModelListAdapter extends RecyclerView.Adapter<ModelItemViewHolder> {
    private List<ModelObject> items = new LinkedList<>();

    @NonNull
    @Override
    public ModelItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListModelItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_model_item, parent, false);
        return new ModelItemViewHolder(binding);
    }

    public void setData(List<ModelObject> newItems) {
        items.clear();
        items.addAll(newItems);
    }

    public List<ModelObject> getItems() {
        return items;
    }

    @Override
    public void onBindViewHolder(@NonNull ModelItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    static class DiffCallback extends DiffUtil.Callback {
        private final List<ModelObject> oldItems;
        private final List<ModelObject> newItems;

        DiffCallback(List<ModelObject> oldItems, List<ModelObject> newItems) {
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
            return oldItems.get(oldItemPosition).getUid() == newItems.get(newItemPosition).getUid();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).name == newItems.get(newItemPosition).name;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
