package ru.fagci.tuihome.task;
import android.content.*;
import java.util.*;
import ru.fagci.tuihome.action.*;

public class TaskExecutor {
  Context context;
  List<ModelTask> tasks = new ArrayList<>();

  public TaskExecutor(Context context) {
    this.context = context;
  }

  public Object execute() {
    Object previousResult = null;

    for (ModelTask task: tasks) {
      if (previousResult != null && task.modelA == null) {
        task.modelA = previousResult;
      }
      previousResult = task.execute(context);
    }

    tasks.clear();

    return previousResult;
  }

  public ModelTask add(Object a, Object b, ModelAction action) {
    ModelTask task = new ModelTask();
    task.modelA = a;
    task.modelB = b;
    task.action = action;
    tasks.add(task);
    return task;
  }

  public void add(Object modelObject, ModelAction modelAction) {
    add(modelObject, null, modelAction);
  }
}
