package ru.fagci.tuihome.loader;
import android.content.*;
import android.os.*;
import java.io.*;
import java.util.*;
import ru.fagci.tuihome.model.*;
import ru.fagci.tuihome.comparator.*;
import ru.fagci.tuihome.lib.*;

public class MediaLoaderTask extends ModelLoaderTask {
  private File baseDirectory;
  private FileObserver fObserver;
  List<ModelObject> items = new ArrayList<>();
  
  private final static Comparator comparator = new LastModifiedComparator();
  
  public MediaLoaderTask(Context context) {
    super(context);
    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      this.baseDirectory = new File(Environment.getExternalStorageDirectory().toString());
    }
  }

  @Override
  protected void onStartLoading() {
    if(null == fObserver) {
      fObserver = new FileObserver(baseDirectory.getPath()){
        @Override
        public void onEvent(int event, String path) {
          if(event != FileObserver.CREATE && event != FileObserver.DELETE) return;
          onContentChanged();
        }
      };
      fObserver.startWatching();
    }
    super.onStartLoading();
  }
  
  @Override
  public List<ModelObject> loadInBackground() {
    items.clear(); // TODO: сделать кэширование
    walkdir(baseDirectory, items);
    Collections.sort(items, comparator);
    return items;
  }

  public void walkdir(File dir, List<ModelObject> items) {
    if (dir == null || dir.length() == 0) return;
    
    for (File f: dir.listFiles()) {
      if(isLoadInBackgroundCanceled()) return;
      if (f.isHidden()) continue;
      if (f.isDirectory()) {
        walkdir(f, items);
        continue;
      }
      items.add(new MediaModel(f));
      
      /*new Res(f.getName(),f){
        createdAt = f.getCreatedAt();
      };*/
    }
  }

  @Override
  protected void onReset() {
    if(null != fObserver) {
      fObserver.stopWatching();
      fObserver = null;
    }
    super.onReset();
  }
}
