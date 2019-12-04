package ru.fagci.tuihome;

import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import ru.fagci.tuihome.action.*;
import ru.fagci.tuihome.databinding.ListItemBinding;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.model.ContactModel;
import ru.fagci.tuihome.model.MediaModel;
import ru.fagci.tuihome.model.ModelObject;

import java.lang.reflect.Field;
import java.util.List;


public class CmdChainViewHolder extends RecyclerView.ViewHolder {
    ListItemBinding binding;
    View view;

    CmdChainViewHolder(ListItemBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;
    }

    private static int getColorForModel(ModelObject modelObject) {
        if (modelObject instanceof AppModel) {
            return Color.rgb(128, 80, 80);
        } else if (modelObject instanceof ContactModel) {
            return Color.rgb(80, 128, 80);
        } else if (modelObject instanceof MediaModel) {
            return Color.rgb(80, 80, 128);
        } else {
            return 0;
        }
    }

    public void bind(ModelObject modelObject) {
        binding.setItem(modelObject);
        binding.executePendingBindings();
        performBind(modelObject);
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

    private void performBind(ModelObject modelObject) {
        setBgColor(getColorForModel(modelObject));


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
                menuItem.setIcon(CmdChainViewHolder.this.getIcon(action));
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

    private void setBgColor(int c) {
        view.setBackgroundColor(c);
    }
}
