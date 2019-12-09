package ru.fagci.tuihome.action;

import android.content.Context;
import android.net.Uri;
import ru.fagci.tuihome.model.ContactModel;

public class ModelActionPhoneCall extends ModelAction {
    private final String phone;

    public ModelActionPhoneCall(String phone) {
        super(ACTION_CALL);
        this.phone = phone;
    }

    @Override
    public String getName() {
        return "Call " + phone;
    }

    @Override
    public Object execute(Context context, Object modelObject) {
        if (modelObject instanceof ContactModel) {
            addFlags(FLAG_ACTIVITY_NEW_TASK);
            setData(Uri.parse("tel:" + phone));
            if (null != resolveActivity(context.getPackageManager())) {
                context.startActivity(this);
            }
        }
        return modelObject;
    }

}
