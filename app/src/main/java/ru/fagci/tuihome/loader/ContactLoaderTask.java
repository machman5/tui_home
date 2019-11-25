package ru.fagci.tuihome.loader;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import ru.fagci.tuihome.model.ContactModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactLoaderTask extends ModelLoaderTask {
    public ContactLoaderTask(Context context) {
        super(context);
    }

    @Override
    public List<ContactModel> loadInBackground() {
        List<ContactModel> entries = new ArrayList<>();

        final ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(Contacts.CONTENT_URI, null, null, null, null);

        if (cursor == null || cursor.getCount() == 0) return entries;

        while (cursor.moveToNext()) {
            if (isLoadInBackgroundCanceled()) break;
            if (cursor.getInt(cursor.getColumnIndex(Contacts.HAS_PHONE_NUMBER)) == 0) continue;

            String id = cursor.getString(cursor.getColumnIndex(Contacts._ID));

            Cursor cursorInfo = contentResolver.query(Phone.CONTENT_URI, null,
                    Phone.CONTACT_ID + " = ?", new String[]{id}, null);

            if (cursorInfo == null) continue;

            InputStream inputStream = Contacts.openContactPhotoInputStream(contentResolver,
                    ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(id)));

            Bitmap photo = inputStream != null ? BitmapFactory.decodeStream(inputStream) : null;

            while (cursorInfo.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
                String number = cursorInfo.getString(cursorInfo.getColumnIndex(Phone.NUMBER));
                ContactModel info = new ContactModel(name, number);

                if (null != photo) info.bitmap = Bitmap.createScaledBitmap(photo, 48, 48, false);

                entries.add(info);
            }

            cursorInfo.close();
        }

        cursor.close();
        return entries;
    }
}
