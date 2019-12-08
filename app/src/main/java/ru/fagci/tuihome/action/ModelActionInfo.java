package ru.fagci.tuihome.action;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import ru.fagci.tuihome.ModelInfoFragment;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.model.ModelObject;

import static android.service.notification.Condition.SCHEME;

public class ModelActionInfo extends ModelAction {
    public ModelActionInfo() {
        super("");
    }

    @Override
    public Object execute(Context context, Object modelObject) {

        if (modelObject instanceof AppModel) {
            AppModel appModel = (AppModel) modelObject;
            setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, appModel.appInfo.packageName, null);
            setData(uri);
            context.startActivity(this);
        } else {
            ModelInfoFragment modelInfoFragment = new ModelInfoFragment();
            modelInfoFragment.setModel((ModelObject) modelObject);
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            modelInfoFragment.show((appCompatActivity).getSupportFragmentManager(), "info");
        }
        return modelObject;
    }
}
