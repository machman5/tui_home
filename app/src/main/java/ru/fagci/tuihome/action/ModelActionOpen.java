package ru.fagci.tuihome.action;
import android.content.*;
import android.net.*;


import ru.fagci.tuihome.model.*;
import android.support.v4.content.*;
import android.support.v4.*;

public class ModelActionOpen extends ModelAction {
  public ModelActionOpen() {
    super(Intent.ACTION_VIEW);
  }
  
  @Override
  public Object execute(Context c, Object a, Object b) {
    setAction(Intent.ACTION_VIEW);
    if (a instanceof MediaModel)
    {
      MediaModel m = (MediaModel) a;
	  
	  Uri uri = FileProvider.getUriForFile(c,BuildConfig.APPLICATION_ID +".provider", m.file);
      setDataAndType(uri, m.getMimeType());
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      if (null != resolveActivity(c.getPackageManager()))
      {
        c.startActivity(this);
      }
    }
    return a;
  }
}
