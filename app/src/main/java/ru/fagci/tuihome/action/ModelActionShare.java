package ru.fagci.tuihome.action;

import android.content.Context;
import android.content.Intent;
import ru.fagci.tuihome.model.MediaModel;

public class ModelActionShare extends ModelAction {
    public ModelActionShare() {
        super(ACTION_SEND);
    }

    @Override
    public Object execute(Context context, Object modelObject) {
        if (modelObject instanceof MediaModel) {
            MediaModel m = (MediaModel) modelObject;

            if (m.file.exists()) {
                Intent i = new Intent(ACTION_SEND);
                i.setType(m.getMimeType());
                i.putExtra(EXTRA_STREAM, m.getUri());
                i.putExtra(EXTRA_SUBJECT, m.name);
                i.putExtra(EXTRA_TEXT, "Share");
                context.startActivity(createChooser(i, "Share " + m.name));
            }
        }

        return modelObject;
    }
}
