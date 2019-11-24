package ru.fagci.tuihome;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import ru.fagci.tuihome.action.*;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.model.ContactModel;
import ru.fagci.tuihome.model.MediaModel;
import ru.fagci.tuihome.model.ModelObject;

import java.lang.reflect.Field;
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
        final ModelObject modelObject = (ModelObject) vm;
        setIcon(modelObject.getBitmap(view.getContext()));
        setText(modelObject.name);
        setDescription(modelObject.getDescription());
        setDate(modelObject.getModifiedDate(view.getContext()));
        setSize(modelObject.getSize());

        setBgColor(getColorForModel(vm));


        view.setOnClickListener(p1 -> {
            view.setBackgroundColor(Color.BLACK);

            final PopupMenu popupMenu = new PopupMenu(view.getContext(), p1);

            try {
                Field mFieldPopup = popupMenu.getClass().getDeclaredField("mPopup");
                mFieldPopup.setAccessible(true);
                MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupMenu);
                if (mPopup != null) {
                    mPopup.setForceShowIcon(true);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            final Menu menu = popupMenu.getMenu();
            final List<ModelAction> actionList = modelObject.getAvailableActions();
            int menuItemId = 0;

            for (ModelAction action : actionList) {
                MenuItem menuItem = menu.add(0, menuItemId++, 0, action.getName());
                menuItem.setIcon(getIcon(action));
            }

            popupMenu.setOnMenuItemClickListener(mItem -> {
                ModelAction modelAction = actionList.get(mItem.getItemId());
                modelAction.execute(view.getContext(), modelObject);
                view.setBackgroundColor(getColorForModel(modelObject));
                return false;
            });
            popupMenu.setOnDismissListener(pMenu -> view.setBackgroundColor(getColorForModel(modelObject)));

            popupMenu.show();
        });
    }

    private int getIcon(ModelAction action) {
        if (action instanceof ModelActionOpen) {
            return android.R.drawable.ic_menu_view;
        } else if (action instanceof ModelActionPhoneCall) {
            return android.R.drawable.ic_menu_call;
        } else if (action instanceof ModelActionRun) {
            return android.R.drawable.ic_media_play;
        } else if (action instanceof ModelActionShare) {
            return android.R.drawable.ic_menu_share;
        }
        return android.R.drawable.ic_menu_help;
    }

    private static int getColorForModel(SortedListAdapter.ViewModel vm) {
        if (vm instanceof AppModel) {
            return Color.rgb(128, 80, 80);
        } else if (vm instanceof ContactModel) {
            return Color.rgb(80, 128, 80);
        } else if (vm instanceof MediaModel) {
            return Color.rgb(80, 80, 128);
        } else {
            return 0;
        }
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
