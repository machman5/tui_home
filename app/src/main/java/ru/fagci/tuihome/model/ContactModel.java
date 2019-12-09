package ru.fagci.tuihome.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.action.ModelActionPhoneCall;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ContactModel extends ModelObject {
    private Uri photoURI;
    private List<String> contacts = new ArrayList<>();

    public ContactModel(String name) {
        super(name, name);
    }

    public void addContact(String contact) {
        contacts.add(contact);
    }

    @Override
    public String getDescription() {
        return "not impl yet";
    }

    @Override
    public List<ModelAction> getAvailableActions() {
        List<ModelAction> aa = super.getAvailableActions();
        aa.add(new ModelActionPhoneCall());
        return aa;
    }

    public void setPhotoURI(Uri photoURI) {
        this.photoURI = photoURI;
    }

    @Override
    public Bitmap createBitmap(Context context) {
        InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(), photoURI);
        Bitmap photo = inputStream != null ? BitmapFactory.decodeStream(inputStream) : null;
        if (null == photo) return null;
        bitmap = Bitmap.createScaledBitmap(photo, 48, 48, false);
        return bitmap;
    }
}
