package ru.fagci.tuihome.vm;

import ru.fagci.tuihome.repository.Repository;

public class MediaViewModel extends ModelViewModel {
    MediaViewModel(Repository repository) {
        super(repository);
        icon = android.R.drawable.stat_notify_sdcard;
    }
}
