package ru.fagci.tuihome.comparator;

import android.app.usage.UsageStats;

import java.util.Comparator;

class RecentUseComparator implements Comparator<UsageStats> {

    @Override
    public int compare(UsageStats lhs, UsageStats rhs) {
        return Long.compare(rhs.getLastTimeUsed(), lhs.getLastTimeUsed());
    }
}
