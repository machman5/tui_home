package ru.fagci.tuihome.loader;

import android.content.Context;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public abstract class ModelLoaderTask extends AsyncTaskLoader<List<?>> {
    protected Context context;
    private List<?> data;

    protected ModelLoaderTask(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void deliverResult(List<?> items) {
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
