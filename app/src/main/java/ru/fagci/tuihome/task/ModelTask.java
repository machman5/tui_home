package ru.fagci.tuihome.task;
import android.content.*;
import ru.fagci.tuihome.action.*;

public class ModelTask {
  Object modelA;
  Object modelB;
  ModelAction action;
  
  public Object execute(Context context) {
    return action.execute(context, modelA, modelB);
  }
}
