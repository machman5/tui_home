package ru.fagci.tuihome.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import ru.fagci.tuihome.DiffCallback;
import ru.fagci.tuihome.ModelItemViewHolder;
import ru.fagci.tuihome.R;
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

    @MainThread
    public void setData(List<ModelObject> newItems) {
        final Handler handler = new Handler();
        new Thread(() -> {
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(items, newItems));
            handler.post(() -> {
                items = newItems;
                diffResult.dispatchUpdatesTo(ModelListAdapter.this);
            });
        }).start();
    }

    @Override
    public void onBindViewHolder(@NonNull ModelItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
