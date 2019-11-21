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
    public Object execute(Context c, Object a, Object b) {
        if (a instanceof MediaModel) {
            MediaModel m = (MediaModel) a;

            Uri uri = FileProvider.getUriForFile(c, "ru.fagci.tuihome", m.file);
            setDataAndType(uri, m.getMimeType());
            addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            if (null != resolveActivity(c.getPackageManager())) {
                c.startActivity(this);
            }
        }
        return a;
    }
}
