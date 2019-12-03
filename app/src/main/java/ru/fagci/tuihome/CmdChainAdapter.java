package ru.fagci.tuihome;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import ru.fagci.tuihome.databinding.ListItemBinding;
import ru.fagci.tuihome.model.ModelObject;

import java.util.LinkedList;
import java.util.List;

public class CmdChainAdapter extends RecyclerView.Adapter<CmdChainViewHolder> {
    private List<ModelObject> items = new LinkedList<>();

    @NonNull
    @Override
    public CmdChainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false);
        return new CmdChainViewHolder(binding);
    }

    public void setData(List<? extends ModelObject> data) {
        items.clear();
        items.addAll(data);
    }

    @Override
    public void onBindViewHolder(@NonNull CmdChainViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
