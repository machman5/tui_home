package ru.fagci.tuihome.repository;

import android.app.Application;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.MediaModel;

import java.io.File;

public class MediaRepository extends Repository {
    public MediaRepository(Application context) {
        super();
        task = new MediaLoaderTask(context, items, isLoading);
    }

    private static class MediaLoaderTask extends ModelLoaderTask {
        //        private final Comparator<ModelObject> comparator = new LastModifiedComparator();
        private File baseDirectory;

        MediaLoaderTask(@NonNull Application context, @NonNull MutableLiveData<ModelObjectMap> items, @NonNull MutableLiveData<Boolean> isLoading) {
            super(context, items, isLoading);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                this.baseDirectory = Environment.getExternalStorageDirectory();
            }
        }

        private void walkDir(File dir, ModelObjectMap items) {
            if (dir == null || dir.length() == 0) return;
            final File[] files = dir.listFiles();
            if (files == null) return;

            for (File f : files) {
                if (isCancelled()) return;
                if (f.isHidden()) continue;
                if (f.isDirectory()) {
                    walkDir(f, items);
                    continue;
                }
                MediaModel mediaModel = new MediaModel(f);
                items.put(mediaModel.getUid(), mediaModel);
            }
        }

        @Override
        @WorkerThread
        protected ModelObjectMap doInBackground(Void... voids) {
            ModelObjectMap entries = new ModelObjectMap();
            walkDir(baseDirectory, entries);
//            Collections.sort(entries, comparator); // TODO: кто сортирует?
            return entries;
        }
    }
}
