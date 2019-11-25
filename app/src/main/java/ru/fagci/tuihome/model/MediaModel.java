package ru.fagci.tuihome.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.action.ModelActionOpen;
import ru.fagci.tuihome.utils.FileUtils;
import ru.fagci.tuihome.utils.GraphicsUtils;

import java.io.File;
import java.util.List;

public class MediaModel extends ModelObject {
    public File file;

    public MediaModel(File f) {
        super(f.getName(), f.getName());
        this.file = f;
        size = f.length();
        lastModified = f.lastModified();
        String hash = String.valueOf(lastModified);
        uid = getClass().getSimpleName() + ":" + file.getAbsolutePath() + ":" + hash;
    }


    public Uri getUri() {
        return Uri.parse("file://" + file.getAbsolutePath());
    }

    public String getMimeType() {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(getExtension());
    }

    private String getExtension() {
        return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
    }

    @Override
    public String getDescription() {
        return file.getParent() + "\n" + FileUtils.getReadableFileSize(file.length());
    }

    @Override
    public Bitmap createBitmap(Context context) {
        if (bitmap != null) return bitmap;
        if (file.getName().endsWith(".apk")) {
            bitmap = GraphicsUtils.getApkIcon(file, context);
            if (null != bitmap) return bitmap;
        }
        try {
            bitmap = FileUtils.getThumbnailSimple(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != bitmap) return bitmap;
        bitmap = FileUtils.getStringBitmap(getExtension());
        return bitmap;
    }

    @Override
    public List<ModelAction> getAvailableActions() {
        List<ModelAction> aa = super.getAvailableActions();
        aa.add(new ModelActionOpen());
        return aa;
    }
}
