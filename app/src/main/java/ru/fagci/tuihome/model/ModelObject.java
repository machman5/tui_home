package ru.fagci.tuihome.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import androidx.core.text.HtmlCompat;
import ru.fagci.tuihome.FilterState;
import ru.fagci.tuihome.action.ModelAction;
import ru.fagci.tuihome.action.ModelActionInfo;
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
    public final String name;
    private final String searchString;
    public long lastModified = 0;
    public int searchWeight = 100;
    Bitmap bitmap;
    String uid;
    long size = 0;

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
        aa.add(new ModelActionInfo());
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

    public String getUid() {
        return uid;
    }

    @SuppressWarnings("deprecation")
    public Spanned getInfoHtml() {
        String info = "";
        info += "<b>UID:</b> " + uid + "<br>";
        info += "<b>Name:</b> " + name + "<br>";
        info += "<b>Search string:</b> " + searchString + "<br>";
        info += "<b>Search weight:</b> " + searchWeight + "<br>";
        info += "<b>Last modified:</b> " + getModifiedDate() + "<br>";
        info += "<b>Size:</b> " + getSize() + "<br>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(info, HtmlCompat.FROM_HTML_MODE_LEGACY);
        }
        return Html.fromHtml(info);
    }
}
