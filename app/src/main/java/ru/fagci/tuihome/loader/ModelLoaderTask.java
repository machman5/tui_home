package ru.fagci.tuihome.loader;

import android.app.Application;
import android.os.AsyncTask;
import android.util.TimingLogger;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.ModelObjectMap;

public abstract class ModelLoaderTask extends AsyncTask<Void, Void, ModelObjectMap> {
    @NonNull
    private Application application;
    private MutableLiveData<ModelObjectMap> liveData;
    private MutableLiveData<Boolean> isLoading;

    private TimingLogger timingLogger = new TimingLogger("AsyncTask", "Instance: " + getClass().getSimpleName());

    public ModelLoaderTask(@NonNull Application application, @NonNull MutableLiveData<ModelObjectMap> liveData, @NonNull MutableLiveData<Boolean> isLoading) {
        super();
        this.application = application;
        this.liveData = liveData;
        this.isLoading = isLoading;
    }

    @NonNull
    public Application getContext() {
        return application;
    }

    @Override
    @MainThread
    protected void onPostExecute(ModelObjectMap modelObjectMap) {
        liveData.postValue(modelObjectMap);
        isLoading.postValue(false);
        timingLogger.addSplit("End");
        timingLogger.dumpToLog();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        timingLogger.addSplit("Start");
    }
}
