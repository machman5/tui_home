package ru.fagci.tuihome.action;
import android.content.*;
import ru.fagci.tuihome.model.*;
import android.content.pm.*;

public class ModelActionRun extends ModelAction
{
  public ModelActionRun() {
    super(Intent.ACTION_RUN);
  }
  @Override
  public Object execute(Context context, Object a, Object b) {
    if(a != null && a instanceof AppModel) {
      AppModel m = (AppModel)a;
      
      PackageManager pm = context.getPackageManager();
      Intent launchIntent = pm.getLaunchIntentForPackage(m.appInfo.packageName);
      if (launchIntent != null) { 
        context.startActivity(launchIntent);//null pointer check in case package name was not found
      }
      return a;
    }
    return null;
  }
}
