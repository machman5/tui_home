package ru.fagci.tuihome.repository;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.FileObserver;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.comparator.LastModifiedComparator;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.MediaModel;
import ru.fagci.tuihome.model.ModelObject;
import ru.fagci.tuihome.receivers.PackageReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MediaRepository {
    private MutableLiveData<List<MediaModel>> media;
    private PackageReceiver receiver;

    public MediaRepository(Application context) {
        media = new MutableLiveData<>();
        MediaLoaderTask task = new MediaLoaderTask(context);
        task.loadInBackground();
    }

    public MutableLiveData<List<MediaModel>> getMedia() {
        return media;
    }

    public class MediaLoaderTask extends ModelLoaderTask {
        private final Comparator<ModelObject> comparator = new LastModifiedComparator();
        private File baseDirectory;
        private FileObserver fObserver;
        private List<ModelObject> items = new ArrayList<>();

        MediaLoaderTask(Context context) {
            super(context);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                this.baseDirectory = Environment.getExternalStorageDirectory();
            }
        }

        @Override
        protected void onStartLoading() {
            if (null == fObserver) {
                fObserver = new FileObserver(baseDirectory.getAbsolutePath(), FileObserver.CREATE | FileObserver.DELETE) {
                    @Override
                    public void onEvent(int event, String path) {
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
            walkDir(baseDirectory, items);
            Collections.sort(items, comparator);
            return items;
        }

        private void walkDir(File dir, List<ModelObject> items) {
            if (dir == null || dir.length() == 0) return;
            final File[] files = dir.listFiles();
            if (files == null) return;

            for (File f : files) {
                if (isLoadInBackgroundCanceled()) return;
                if (f.isHidden()) continue;
                if (f.isDirectory()) {
                    walkDir(f, items);
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


}
