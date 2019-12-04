package ru.fagci.tuihome.vm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.model.MediaModel;
import ru.fagci.tuihome.repository.MediaRepository;

import java.util.List;

public class MediaViewModel extends AndroidViewModel {
    private MutableLiveData<List<MediaModel>> data;
    private MediaRepository mediaRepository;


    public MediaViewModel(@NonNull Application application) {
        super(application);
        mediaRepository = new MediaRepository(application);
    }

    public LiveData<List<MediaModel>> getData() {
        if (data == null) {
            data = mediaRepository.getMedia();
        }
        return data;
    }
}
