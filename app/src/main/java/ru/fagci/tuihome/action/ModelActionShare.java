package ru.fagci.tuihome.action;

import android.content.Context;
import android.content.Intent;
import ru.fagci.tuihome.model.MediaModel;

public class ModelActionShare extends ModelAction {
    public ModelActionShare() {
        super(ACTION_SEND);
    }

    @Override
    public Object execute(Context context, Object a, Object b) {
        if (a instanceof MediaModel) {
            MediaModel m = (MediaModel) a;

            if (m.file.exists()) {
                Intent i = new Intent(ACTION_SEND);
                i.setType(m.getMimeType());
                i.putExtra(EXTRA_STREAM, m.getUri());
                i.putExtra(EXTRA_SUBJECT, "Share subject");
                i.putExtra(EXTRA_TEXT, "Share text");
                context.startActivity(createChooser(i, "Share " + m.name));
            }
        }

        return a;
    }
}
