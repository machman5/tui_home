package ru.fagci.tuihome;
import android.content.*;
import android.view.*;
import java.util.*;
import ru.fagci.tuihome.model.*;

public class CmdChainAdapter extends SortedListAdapter<ModelObject> {
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

  public CmdChainAdapter(Context c) {
    super(c, ModelObject.class, new Comparator<ModelObject>(){
        @Override
        public int compare(ModelObject p1, ModelObject p2) {
          return p1.name.compareTo(p2.name);
        }
      });
  }
}
