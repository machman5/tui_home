package ru.fagci.tuihome;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.model.ModelObject;
import ru.fagci.tuihome.task.TaskExecutor;

import java.util.List;


public class CmdChainViewHolder extends SortedListAdapter.ViewHolder {

    private final TextView name;

    public CmdChainViewHolder(View v) {
        super(v);
        name = v.findViewById(R.id.cmd_chain_itemName);
    }

    protected void performBind(SortedListAdapter.ViewModel vm) {
        final ModelObject m = (ModelObject) vm;
        setText(m.name);
        setBgColor(m.getColor());

        name.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                final PopupMenu popupMenu = new PopupMenu(name.getContext(), p1);
                final Menu menu = popupMenu.getMenu();
                final List<ModelAction> actionList = m.getAvailableActions();
                int menuItemId = 0;

                for (ModelAction action : actionList) {
                    menu.add(0, menuItemId++, 0, action.getName());
                }

                popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem mItem) {
                        TaskExecutor taskExecutor = new TaskExecutor(name.getContext());
                        taskExecutor.add(m, actionList.get(mItem.getItemId()));
                        taskExecutor.execute();
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    public void setText(String t) {
        name.setText(t);
    }

    public void setBgColor(int c) {
        name.setBackgroundColor(c);
    }
}
