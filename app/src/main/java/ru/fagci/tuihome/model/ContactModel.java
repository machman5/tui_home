package ru.fagci.tuihome.model;

import android.net.Uri;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.action.ModelActionPhoneCall;

import java.util.List;


public class ContactModel extends ModelObject {
    public String mobileNumber;
    public Uri photoURI;

    public ContactModel(String name, String mobileNumber) {
        super(name, name + " " + mobileNumber);
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String getDescription() {
        return mobileNumber;
    }

    @Override
    public List<ModelAction> getAvailableActions() {
        List<ModelAction> aa = super.getAvailableActions();
        aa.add(new ModelActionPhoneCall());
        return aa;
    }
}
