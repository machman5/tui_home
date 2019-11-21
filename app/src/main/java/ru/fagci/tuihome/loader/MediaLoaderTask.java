package ru.fagci.tuihome.loader;

import android.content.Context;
import android.os.Environment;
import android.os.FileObserver;
import ru.fagci.tuihome.comparator.LastModifiedComparator;
import ru.fagci.tuihome.model.MediaModel;
import ru.fagci.tuihome.model.ModelObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MediaLoaderTask extends ModelLoaderTask {
    private final static Comparator comparator = new LastModifiedComparator();
    private File baseDirectory;
    private FileObserver fObserver;
    private List<ModelObject> items = new ArrayList<>();

    public MediaLoaderTask(Context context) {
        super(context);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            this.baseDirectory = new File(Environment.getExternalStorageDirectory().toString());
        }
    }

    @Override
    protected void onStartLoading() {
        if (null == fObserver) {
            fObserver = new FileObserver(baseDirectory.getPath()) {
                @Override
                public void onEvent(int event, String path) {
                    if (event != FileObserver.CREATE && event != FileObserver.DELETE) return;
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

        for (File f : dir.listFiles()) {
            if (isLoadInBackgroundCanceled()) return;
            if (f.isHidden()) continue;
            if (f.isDirectory()) {
                walkdir(f, items);
                continue;
            }
            items.add(new MediaModel(f));
        }
    }

    @Override
    protected void onReset() {
        if (null != fObserver) {
            fObserver.stopWatching();
            fObserver = null;
        }
        super.onReset();
    }
}
