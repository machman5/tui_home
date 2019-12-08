package ru.fagci.tuihome.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.ModelObjectMap;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.ContactModel;

import java.io.InputStream;

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

            final ContentResolver contentResolver = getContext().getContentResolver();

            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cursor == null || cursor.getCount() == 0) return entries;

            while (cursor.moveToNext()) {
                if (isCancelled()) break;
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) == 0) continue;

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                if (cursorInfo == null) continue;

                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver,
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id)));

                Bitmap photo = inputStream != null ? BitmapFactory.decodeStream(inputStream) : null;

                while (cursorInfo.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String number = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    ContactModel info = new ContactModel(name, number);

                    if (null != photo) info.bitmap = Bitmap.createScaledBitmap(photo, 48, 48, false);

                    entries.put(info.getUid(), info);
                }

                cursorInfo.close();
            }

            cursor.close();
            return entries;
        }
    }

}
