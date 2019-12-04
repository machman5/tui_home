package ru.fagci.tuihome.vm;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.model.ContactModel;
import ru.fagci.tuihome.repository.ContactsRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private MutableLiveData<List<ContactModel>> data;
    private ContactsRepository contactsRepository;


    public ContactViewModel(@NonNull Application application) {
        super(application);
        contactsRepository = new ContactsRepository(application);
    }

    public LiveData<List<ContactModel>> getData() {
        if (data == null) {
            data = contactsRepository.getContacts();
        }
        return data;
    }
}
