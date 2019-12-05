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
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ModelViewModel.class)) {
            return (T) new ModelViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}