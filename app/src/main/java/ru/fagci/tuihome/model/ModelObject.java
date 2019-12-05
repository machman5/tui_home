package ru.fagci.tuihome.model;

import android.content.Context;
import android.graphics.Bitmap;
import ru.fagci.tuihome.FilterState;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.action.ModelActionShare;
import ru.fagci.tuihome.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public abstract class ModelObject {
    private static int __id = 0;

    String uid;
    public final String name;
    private final String searchString;
    long size = 0;

    public long lastModified = 0;
    public int searchWeight = 100;

    public Bitmap bitmap;

    public ModelObject(String name, String searchString) {
        this.name = name;
        this.searchString = searchString;
        this.uid = getClass().getSimpleName() + ":" + __id++;
    }

    public String getDescription() {
        if (lastModified == 0) return null;
        return new Date(lastModified).toString();
    }

    public boolean search(String q, Pattern pattern) {
        if (q != null && !q.isEmpty() && searchString.contains(q)) {
            searchWeight = 100;
            return true;
        }
        if (pattern.matcher(searchString).find()) {
            searchWeight = 99;
            return true;
        }
        return false; // temporary speed fix
//    final int w = FuzzySearch.partialRatio(q, searchString);
//
//    if (w >= 70) {
//      searchWeight = w;
//      return true;
//    }
//
//    return false;
    }

    @Override
    public String toString() {
        return name;
    }

    public final Bitmap getBitmap(Context context) {
        if (null != bitmap) return bitmap;
        bitmap = createBitmap(context);
        return bitmap;
    }

    public Bitmap createBitmap(Context context) {
        return bitmap;
    }

    public List<ModelAction> getAvailableActions() {
        final List<ModelAction> aa = new ArrayList<>();
        aa.add(new ModelActionShare());
        return aa;
    }

    public String getSize() {
        return FileUtils.getReadableFileSize(size);
    }

    public String getModifiedDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm", Locale.getDefault());
        Date date = new Date(lastModified);
        return format.format(date);
    }

    public boolean search(FilterState input) {
        return search(input.getQuery(), input.getPattern());
    }
}
