package ru.fagci.tuihome.action;
import android.content.*;
import android.net.*;
import ru.fagci.tuihome.model.*;

public class ModelActionPhoneCall extends ModelAction 
{
  public ModelActionPhoneCall() {
    super(Intent.ACTION_CALL);
  }

  @Override
  public Object execute(Context context, Object a, Object b) {
    if (a != null && a instanceof ContactModel)
    {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      setData(Uri.parse("tel:" + ((ContactModel)a).mobileNumber));
      if (null != resolveActivity(context.getPackageManager()))
      {
        context.startActivity(this);
      }
      return a;
    }
    return null;
  }

}
