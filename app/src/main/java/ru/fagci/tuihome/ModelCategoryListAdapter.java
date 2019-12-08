package ru.fagci.tuihome;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import ru.fagci.tuihome.databinding.ListModelCategoryItemBinding;
import ru.fagci.tuihome.vm.ModelViewModel;

import java.util.LinkedList;
import java.util.List;

public class ModelCategoryListAdapter extends RecyclerView.Adapter<ModelCategoryViewHolder> {
    private List<ModelViewModel> items = new LinkedList<>();

    @NonNull
    @Override
    public ModelCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListModelCategoryItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_model_category_item, parent, false);
        return new ModelCategoryViewHolder(binding);
    }

    public void setData(List<ModelViewModel> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ModelCategoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(ModelViewModel viewModel) {
        items.add(viewModel);
        notifyDataSetChanged();
    }
}
