package ru.fagci.tuihome.action;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class ModelAction extends Intent {
    public ModelAction(String name) {
        super(name);
    }


    public Object execute(Context c, Object a, Object b) {
        PackageManager packageManager = c.getPackageManager();
        ComponentName cn = resolveActivity(packageManager);
        if (null == cn) return null;
        return null; // TODO: понять, что тут предполагалось сделать
    }

    public String getName() {
        return getClass().getSimpleName().replace("ModelAction", "");
    }

    public String toString() {
        return getName();
    }
}
