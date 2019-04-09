package ru.fagci.tuihome.action;
import android.content.*;
import android.os.health.*;
import android.content.pm.*;

public abstract class ModelAction extends Intent {
  public ModelAction(String name) {
    super(name);
  }
  
  
  public Object execute(Context c, Object a, Object b) {
    PackageManager packageManager = c.getPackageManager();
    ComponentName cn = resolveActivity(packageManager);
    if(null == cn) return null;
    return null;
  }

  public String getName() {
    return getClass().getSimpleName().replace("ModelAction", "");
  }

  public String toString() {
    return getName();
  }
}
