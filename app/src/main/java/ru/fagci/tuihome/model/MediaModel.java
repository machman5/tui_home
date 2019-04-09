package ru.fagci.tuihome.model;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.webkit.*;
import java.io.*;
import java.util.*;
import ru.fagci.tuihome.action.*;
import java.text.*;

public class MediaModel extends ModelObject {
  public File file;

  public MediaModel(File f) {
    super(f.getName(), f.getName());
    this.file = f;
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

  public String getExtension() {
    return MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
  }
  
  public static String getReadableFileSize(long size) {
    if(size <= 0) return "0";
    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }

  @Override
  public String getDescription() {
    return file.getParent() + "\n" + getReadableFileSize(file.length());
  }

  @Override
  public Bitmap createBitmap(Context context) {
    if (bitmap != null) return bitmap;
    if (file.getName().endsWith(".apk")) {
      bitmap = getApkIcon(file, context);
      if (null != bitmap) return bitmap;
    }
    try {
      bitmap = getThumbnailSimple(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (null != bitmap) return bitmap;
    bitmap = getStringBitmap(getExtension());
    return bitmap;
  }

  private static Rect setTextSizeForWidth(Paint paint, float maxW, float maxH,
                                          String text) {
    final float testTextSize = 48f;
    paint.setTextSize(testTextSize);
    Rect bounds = new Rect();
    paint.getTextBounds(text, 0, text.length(), bounds);
    float tsByW = testTextSize * maxW / bounds.width();
    float tsByH = testTextSize * maxH / bounds.height();
    paint.setTextSize(tsByW < tsByH ?tsByW: tsByH);
    paint.getTextBounds(text, 0, text.length(), bounds);
    return bounds;
  }

  private static Bitmap getStringBitmap(String t) {
    if (null == t) return null;
    final int W = 96;
    final int H = 96;

    Bitmap b = Bitmap.createBitmap(W, H, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(b);

    Paint p = new Paint();
    Rect bounds = setTextSizeForWidth(p, W - 16, H - 16, t);
    
    p.setTextAlign(Paint.Align.CENTER);
    p.setColor(Color.valueOf(0,0,0,128).toArgb());
    canvas.drawCircle(W/2,H/2,H/2,p);
    p.setColor(Color.WHITE);
    canvas.drawText(t, W / 2 , H / 2 + bounds.height() / 2, p);
    return b;
  }

  public static Bitmap getApkIcon(File file, Context context) {
    PackageInfo packageInfo = context.getPackageManager()
      .getPackageArchiveInfo(file.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
    if (packageInfo != null) {
      ApplicationInfo appInfo = packageInfo.applicationInfo;
      if (Build.VERSION.SDK_INT >= 8) {
        appInfo.sourceDir = file.getPath();
        appInfo.publicSourceDir = file.getPath();
      }
      Drawable icon = appInfo.loadIcon(context.getPackageManager());
      return AppModel.getBitmapFromDrawable(icon);
    }
    return null;
  }

  public static Bitmap getThumbnailSimple(File file) {
    final int THUMBSIZE = 96;

    return ThumbnailUtils.extractThumbnail(
      BitmapFactory.decodeFile(file.getAbsolutePath()), THUMBSIZE, THUMBSIZE);
  }

  @Override
  public List<ModelAction> getAvailableActions() {
    List<ModelAction> aa = super.getAvailableActions();
    aa.add(new ModelActionOpen());
    return aa;
  }

  @Override
  public int getColor() {
    return Color.rgb(230, 128, 200);
  }
}
