package ru.fagci.tuihome.vm;

import ru.fagci.tuihome.repository.Repository;

public class ContactViewModel extends ModelViewModel {
    public ContactViewModel(Repository repository) {
        super(repository);
        icon = android.R.drawable.sym_contact_card;
    }
}
