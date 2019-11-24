package ru.fagci.tuihome.task;

import android.content.Context;
import ru.fagci.tuihome.action.ModelAction;

import java.util.ArrayList;

public class ModelTask extends ArrayList<Object> {
    ModelAction action;

    public Object execute(Context context) {
        if (size() == 1) return action.execute(context, get(0));
        return null;
    }
}
