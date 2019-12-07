package ru.fagci.tuihome.loader;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

import java.util.HashMap;

public abstract class ModelLoaderTask extends AsyncTaskLoader<HashMap<String, ?>> {
    protected Context context;
    private HashMap<String, ?> data;

    protected ModelLoaderTask(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void deliverResult(HashMap<String, ?> items) {
        data = items;

        if (isStarted()) {
            super.deliverResult(items);
        }
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        }

        if (takeContentChanged() || data == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();

        if (data != null) {
            data = null;
        }
    }
}
