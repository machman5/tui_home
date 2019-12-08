package ru.fagci.tuihome.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import ru.fagci.tuihome.repository.Repository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Repository repository;

    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ModelViewModel.class)) {
            return (T) new ModelViewModel(repository);
        }
        if (modelClass.isAssignableFrom(AppViewModel.class)) {
            return (T) new AppViewModel(repository);
        }
        if (modelClass.isAssignableFrom(ContactViewModel.class)) {
            return (T) new ContactViewModel(repository);
        }
        if (modelClass.isAssignableFrom(MediaViewModel.class)) {
            return (T) new MediaViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}