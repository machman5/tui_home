package ru.fagci.tuihome.task;

import android.content.Context;
import ru.fagci.tuihome.action.ModelAction;

import java.util.ArrayList;

class ModelTask extends ArrayList<Object> {
    private ModelAction action;

    Object execute(Context context) {
        if (size() == 1) return action.execute(context, get(0));
        return null;
    }
}
