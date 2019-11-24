package ru.fagci.tuihome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ru.fagci.tuihome.model.ModelObject;

public class CmdChainAdapter extends SortedListAdapter<ModelObject> {
    public CmdChainAdapter(Context c) {
        super(c, ModelObject.class, (p1, p2) -> p1.name.compareTo(p2.name));
    }

    @Override
    protected SortedListAdapter.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        final View itemView = inflater.inflate(R.layout.cmd_chain_item, parent, false);
        return new CmdChainViewHolder(itemView);
    }

    @Override
    protected boolean areItemsTheSame(ModelObject item1, ModelObject item2) {
        return item1 == item2;
    }

    @Override
    protected boolean areItemContentsTheSame(ModelObject oldItem, ModelObject newItem) {
        return oldItem.hashCode() == newItem.hashCode();
    }
}
