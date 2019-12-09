package ru.fagci.tuihome.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.ColumnIndexCache;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.ContactModel;

public class ContactsRepository extends Repository {
    public ContactsRepository(Application context) {
        super();
        task = new ContactLoaderTask(context, items, isLoading);
    }

    static class ContactLoaderTask extends ModelLoaderTask {

        ContactLoaderTask(@NonNull Application application, @NonNull MutableLiveData<ModelObjectMap> liveData, @NonNull MutableLiveData<Boolean> isLoading) {
            super(application, liveData, isLoading);
        }

        @Override
        @WorkerThread
        protected ModelObjectMap doInBackground(Void... voids) {
            ModelObjectMap entries = new ModelObjectMap();
            ColumnIndexCache cache = new ColumnIndexCache();

            final ContentResolver contentResolver = getContext().getContentResolver();

            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cursor == null || cursor.getCount() == 0) return entries;

            while (cursor.moveToNext()) {
                if (isCancelled()) break;
                if (cursor.getInt(cache.getColumnIndex(cursor, ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 0)
                    continue;

                String id = cursor.getString(cache.getColumnIndex(cursor, ContactsContract.Contacts._ID));

                Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                String name = cursor.getString(cache.getColumnIndex(cursor, ContactsContract.Contacts.DISPLAY_NAME));
                ContactModel info = new ContactModel(name);
                final Uri contactPhotoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
                info.setPhotoURI(contactPhotoUri);

                if (cursorInfo == null) continue;

                while (cursorInfo.moveToNext()) {
                    String number = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String normalizedNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    info.addContact(normalizedNumber == null ? number : normalizedNumber);
                    entries.put(info.getUid(), info);
                }

                cursorInfo.close();
            }

            cursor.close();

            cache.clear();
            return entries;
        }
    }

}
