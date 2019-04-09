package ru.fagci.tuihome.model;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import java.util.*;
import ru.fagci.tuihome.action.*;
import ru.fagci.tuihome.loader.*;
import java.util.regex.*;

public class AppModel extends ModelObject {
  public ApplicationInfo appInfo;

  public AppModel(ApplicationInfo appInfo) {
    super(appInfo.loadLabel(AppLoaderTask.pm).toString(), appInfo.loadLabel(AppLoaderTask.pm).toString() + " " + appInfo.packageName);
    this.appInfo = appInfo;
    this.uid = getClass().getSimpleName() + ":" + appInfo.packageName;
  }

  @Override
  public boolean search(String q, Pattern pattern) {
    boolean res = super.search(q, pattern);
    if(!pattern.matcher(name).find()) searchWeight *= 0.75;
    return res;
  }

  @Override
  public String getDescription() {
    return appInfo.packageName;
  }

  @Override
  public Bitmap createBitmap(Context context) {
    if(bitmap == null) {
      bitmap = getBitmapFromDrawable(appInfo.loadIcon(AppLoaderTask.pm));
    }
    return bitmap;
  }

  @Override
  public List<ModelAction> getAvailableActions() {
    List<ModelAction> aa = super.getAvailableActions();
    aa.add(0,new ModelActionRun());
    return aa;
  }
  
  public static Bitmap getBitmapFromDrawable(Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      return ((BitmapDrawable) drawable).getBitmap();
    } else if (drawable instanceof AdaptiveIconDrawable) {
      final Drawable[] drr = new Drawable[]{
        ((AdaptiveIconDrawable) drawable).getBackground(),
        ((AdaptiveIconDrawable) drawable).getForeground()
      };
      
      final LayerDrawable layerDrawable = new LayerDrawable(drr);

      int width = 96; //layerDrawable.getIntrinsicWidth();
      int height = 96; //layerDrawable.getIntrinsicHeight();

      Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

      Canvas canvas = new Canvas(bitmap);

      layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      layerDrawable.draw(canvas);

      return bitmap;
    }
    return null;
  }
  
  @Override
  public int getColor() {
    return Color.rgb(128, 180, 200);
  }
}
