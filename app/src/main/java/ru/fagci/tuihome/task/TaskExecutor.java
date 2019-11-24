package ru.fagci.tuihome.task;

import android.content.Context;

import java.util.ArrayList;

public class TaskExecutor extends ArrayList<ModelTask> {
    private Context context;

    public TaskExecutor(Context context) {
        this.context = context;
    }

    public Object execute() {
        Object previousResult = null;

        for (ModelTask task : this) {
            if (previousResult != null && task.isEmpty()) task.add(previousResult);
            previousResult = task.execute(context);
        }

        clear();

        return previousResult;
    }
}
