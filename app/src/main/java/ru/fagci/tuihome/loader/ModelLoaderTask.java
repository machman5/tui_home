package ru.fagci.tuihome.loader;
import android.os.*;
import java.util.*;
import ru.fagci.tuihome.model.*;
import android.content.*;

public abstract class ModelLoaderTask extends AsyncTaskLoader<List<?>> {
  protected Context context;
  List<?> data;

  @Override public void deliverResult(List<?> items) {
    data = items;

    if (isStarted()) {
      super.deliverResult(items);
    }
  }

  @Override protected void onStartLoading() {
    if (data != null) {
      deliverResult(data);
    }

    if (takeContentChanged() || data == null) {
      forceLoad();
    }
  }

  @Override protected void onStopLoading() {
    cancelLoad();
  }

  @Override protected void onReset() {
    super.onReset();
    onStopLoading();

    if (data != null) {
      data = null;
    }
  }
  
  public ModelLoaderTask(Context context) {
    super(context);
    this.context = context;
  }
}
