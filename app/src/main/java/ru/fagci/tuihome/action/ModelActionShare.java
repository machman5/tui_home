package ru.fagci.tuihome.action;
import android.content.*;
import ru.fagci.tuihome.model.*;

public class ModelActionShare extends ModelAction {
  public ModelActionShare() {
    super(Intent.ACTION_SEND);
  }

  @Override
  public Object execute(Context context, Object a, Object b) {
    if (a == null) return a;

    if (a instanceof MediaModel) {
      MediaModel m = (MediaModel) a;
      
      if (m.file.exists()) {
        setType(m.getMimeType());
        putExtra(Intent.EXTRA_STREAM, m.getUri());
        putExtra(Intent.EXTRA_SUBJECT, "Share subject");
        putExtra(Intent.EXTRA_TEXT, "Share text");
        context.startActivity(Intent.createChooser(this, "Share " + m.name));
      }
    }

    return a;
  }
}
