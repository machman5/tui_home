package ru.fagci.tuihome.model;
import android.graphics.*;
import android.net.*;
import java.util.*;
import android.content.*;
import ru.fagci.tuihome.action.*;


public class ContactModel extends ModelObject {
  @Override
  public Bitmap createBitmap(Context context) {
    return bitmap;
  }

  public String mobileNumber;
  public Uri photoURI;
  
  public ContactModel(String name, String mobileNumber) {
    super(name, name + " " + mobileNumber);
    this.mobileNumber = mobileNumber;
  }

  @Override
  public String getDescription() {
    return mobileNumber;
  }

  @Override
  public List<ModelAction> getAvailableActions() {
    List<ModelAction> aa = super.getAvailableActions();
    aa.add(new ModelActionPhoneCall());
    return aa;
  }
  
  @Override
  public int getColor() {
    return Color.rgb(200, 128, 128);
  }
}
