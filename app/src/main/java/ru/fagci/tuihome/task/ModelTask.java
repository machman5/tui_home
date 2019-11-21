package ru.fagci.tuihome.task;

import android.content.Context;
import ru.fagci.tuihome.action.ModelAction;

public class ModelTask {
    Object modelA;
    Object modelB;
    ModelAction action;

    public Object execute(Context context) {
        return action.execute(context, modelA, modelB);
    }
}
