package ru.fagci.tuihome.action;

import android.content.Context;
import android.net.Uri;
import androidx.core.content.FileProvider;
import ru.fagci.tuihome.model.MediaModel;

public class ModelActionOpen extends ModelAction {
    public ModelActionOpen() {
        super(ACTION_VIEW);
    }

    @Override
    public Object execute(Context context, Object modelObject) {
        if (modelObject instanceof MediaModel) {
            MediaModel m = (MediaModel) modelObject;

            Uri uri = FileProvider.getUriForFile(context, "ru.fagci.tuihome", m.file);
            setDataAndType(uri, m.getMimeType());
            addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            if (null != resolveActivity(context.getPackageManager())) {
                context.startActivity(this);
            }
        }
        return modelObject;
    }
}
