package ru.fagci.tuihome.comparator;
import java.util.*;
import ru.fagci.tuihome.model.*;

public class WeightComparator implements Comparator<ModelObject> {
  @Override
  public int compare(ModelObject p1, ModelObject p2) {  
    return p2.searchWeight - p1.searchWeight;
  }
}

