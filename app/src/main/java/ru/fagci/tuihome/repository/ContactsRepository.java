package ru.fagci.tuihome.repository;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import androidx.lifecycle.MutableLiveData;
import ru.fagci.tuihome.loader.ModelLoaderTask;
import ru.fagci.tuihome.model.ContactModel;
import ru.fagci.tuihome.receivers.PackageReceiver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactsRepository {
    private MutableLiveData<List<ContactModel>> contacts;
    private PackageReceiver receiver;

    public ContactsRepository(Application context) {
        contacts = new MutableLiveData<>();
        ContactLoaderTask task = new ContactLoaderTask(context);
        task.loadInBackground();
    }

    public MutableLiveData<List<ContactModel>> getContacts() {
        return contacts;
    }

    class ContactLoaderTask extends ModelLoaderTask {
        ContactLoaderTask(Context context) {
            super(context);
        }

        @Override
        public List<ContactModel> loadInBackground() {
            List<ContactModel> entries = new ArrayList<>();

            final ContentResolver contentResolver = context.getContentResolver();

            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cursor == null || cursor.getCount() == 0) return entries;

            while (cursor.moveToNext()) {
                if (isLoadInBackgroundCanceled()) break;
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

                    entries.add(info);
                }

                cursorInfo.close();
            }

            cursor.close();
            contacts.postValue(entries);
            return entries;
        }
    }

}
