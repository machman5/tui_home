package ru.fagci.tuihome.model;
import android.content.*;
import android.graphics.*;
import java.util.*;
import java.util.regex.*;
import ru.fagci.tuihome.*;
import ru.fagci.tuihome.action.*;

public abstract class ModelObject implements SortedListAdapter.ViewModel{
  private static int __id = 0;
  private int _id = 0;
  public String uid;
  public long lastModified = 0;

  public final String name;
  public final String searchString;

  public int searchWeight = 100;
  public Bitmap bitmap;

  public ModelObject(String name, String searchString) {
    _id = __id++;
    this.name = name;
    this.searchString = searchString;
    this.uid = getClass().getSimpleName() +":"+String.valueOf(_id);
  }

  public String getDescription() {
    if(lastModified==0) return null;
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
    if(null != bitmap) return bitmap;
    bitmap = createBitmap(context);
    return bitmap;
  }

  public abstract Bitmap createBitmap(Context context);

  public List<ModelAction> getAvailableActions() {
    final List<ModelAction> aa = new ArrayList<ModelAction>();
    aa.add(new ModelActionShare());
    return aa;
  }
  
  public int getIncrementedId() {
    return this._id;
  }
  
  public int getColor() {
    return Color.GRAY;
  }
}
