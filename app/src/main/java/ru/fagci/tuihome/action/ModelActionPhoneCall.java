package ru.fagci.tuihome.action;

import android.content.Context;
import android.net.Uri;
import ru.fagci.tuihome.model.ContactModel;

public class ModelActionPhoneCall extends ModelAction {
    public ModelActionPhoneCall() {
        super(ACTION_CALL);
    }

    @Override
    public Object execute(Context context, Object modelObject) {
        if (modelObject instanceof ContactModel) {
            addFlags(FLAG_ACTIVITY_NEW_TASK);
            setData(Uri.parse("tel:" + ((ContactModel) modelObject).mobileNumber));
            if (null != resolveActivity(context.getPackageManager())) {
                context.startActivity(this);
            }
        }
        return modelObject;
    }

}
