package ru.fagci.tuihome;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.model.ContactModel;
import ru.fagci.tuihome.model.MediaModel;
import ru.fagci.tuihome.model.ModelObject;
import ru.fagci.tuihome.task.TaskExecutor;

import java.util.List;


public class CmdChainViewHolder extends SortedListAdapter.ViewHolder {

    private final View view;
    private final ImageView icon;
    private final TextView name;
    private final TextView description;
    private final TextView date;
    private final TextView size;

    CmdChainViewHolder(View v) {
        super(v);
        view = v;
        icon = v.findViewById(R.id.cmd_chain_itemIcon);
        name = v.findViewById(R.id.cmd_chain_itemName);
        description = v.findViewById(R.id.cmd_chain_itemDescription);
        date = v.findViewById(R.id.cmd_chain_itemDate);
        size = v.findViewById(R.id.cmd_chain_itemSize);
    }

    protected void performBind(SortedListAdapter.ViewModel vm) {
        final ModelObject m = (ModelObject) vm;
        setIcon(m.getBitmap(view.getContext()));
        setText(m.name);
        setDescription(m.getDescription());
        setDate(m.getModifiedDate(view.getContext()));
        setSize(m.getSize());

        if (vm instanceof AppModel) {
            setBgColor(Color.rgb(128, 80, 80));
        } else if (vm instanceof ContactModel) {
            setBgColor(Color.rgb(80, 128, 80));
        } else if (vm instanceof MediaModel) {
            setBgColor(Color.rgb(80, 80, 128));
        } else {
            setBgColor(0);
        }

        name.setOnClickListener(p1 -> {
            final PopupMenu popupMenu = new PopupMenu(view.getContext(), p1);
            final Menu menu = popupMenu.getMenu();
            final List<ModelAction> actionList = m.getAvailableActions();
            int menuItemId = 0;

            for (ModelAction action : actionList) {
                menu.add(0, menuItemId++, 0, action.getName());
            }

            popupMenu.setOnMenuItemClickListener(mItem -> {
                TaskExecutor taskExecutor = new TaskExecutor(view.getContext());
                taskExecutor.add(m, actionList.get(mItem.getItemId()));
                taskExecutor.execute();
                return false;
            });

            popupMenu.show();
        });
    }

    private void setIcon(Bitmap b) {
        icon.setImageBitmap(b);
    }

    private void setText(String t) {
        name.setText(t);
    }

    private void setDescription(String t) {
        description.setText(t);
    }

    private void setDate(String t) {
        date.setText(t);
    }


    private void setSize(String t) {
        size.setText(t);
    }

    private void setBgColor(int c) {
        view.setBackgroundColor(c);
    }
}
