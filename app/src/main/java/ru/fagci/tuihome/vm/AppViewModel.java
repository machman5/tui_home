package ru.fagci.tuihome.vm;

import ru.fagci.tuihome.repository.Repository;

public class AppViewModel extends ModelViewModel {
    AppViewModel(Repository repository) {
        super(repository);
        this.icon = android.R.drawable.sym_def_app_icon;
    }
}
