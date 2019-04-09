package ru.fagci.tuihome.loader;
import android.content.*;
import android.content.pm.*;
import java.util.*;
import ru.fagci.tuihome.model.*;

public class AppLoaderTask extends ModelLoaderTask {
  public static PackageManager pm;
  //private static final AlphaComparator alphaComparator = new AlphaComparator();

  @Override
  public List<AppModel> loadInBackground() {
    final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

    final List<AppModel> entries = new ArrayList<>();

    for (ApplicationInfo info: pm.getInstalledApplications(0)) {
      if(isLoadInBackgroundCanceled()) break;
      if (pm.getLaunchIntentForPackage(info.packageName) == null) continue;
      entries.add(new AppModel(info));
    }

    //Collections.sort(entries, alphaComparator);

    return entries;
  }

//  public static boolean queryUsageStats(Context context, String packageName) {
//    
//    RecentUseComparator mRecentComp = new RecentUseComparator();
//    long ts = System.currentTimeMillis();
//    UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");
//    List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000*60*60*24*30, ts);
//    if (usageStats == null || usageStats.size() == 0) {
//      //if (context.getPackageManager().h == false) {
//        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//        Toast.makeText(context, "权限不够\n请打开手机设置，点击安全-高级，在有权查看使用情况的应用中，为这个App打上勾", Toast.LENGTH_SHORT).show();
//      //}
//      return false;
//    }
//    Collections.sort(usageStats, mRecentComp);
//    String currentTopPackage = usageStats.get(0).getPackageName();
//    if (currentTopPackage.equals(packageName)) {
//      return true;
//    } else {
//      return false;
//    }
//  }

  public AppLoaderTask(Context context) {
    super(context);
    pm = context.getPackageManager();
  }
}
