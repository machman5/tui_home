package ru.fagci.tuihome.comparator;
import ru.fagci.tuihome.model.*;
import java.util.*;

public class LastModifiedComparator implements Comparator<ModelObject>
{
  @Override
  public int compare(ModelObject p1, ModelObject p2) {
    if (p1.lastModified < p2.lastModified) {
      return 1;
    } else if (p1.lastModified > p2.lastModified) {
      return -1;
    }
    return 0;
  }
}
