package ru.fagci.tuihome.comparator;
import android.app.usage.*;
import java.util.*;

class RecentUseComparator implements Comparator<UsageStats> {

  @Override
  public int compare(UsageStats lhs, UsageStats rhs) {
    return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
  }
}
