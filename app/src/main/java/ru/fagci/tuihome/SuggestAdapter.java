package ru.fagci.tuihome;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ru.fagci.tuihome.model.ModelIconCache;
import ru.fagci.tuihome.model.ModelObject;
import ru.fagci.tuihome.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;

public class SuggestAdapter extends ArrayAdapter<ModelObject> {
    private final MainActivity activity;
    public List<ModelObject> originalItems, items;
    //private final ModelObjectFilter filter = null;
    TaskExecutor taskExecutor;
    private ModelIconCache bitmapCache;
    private SparseArray<AsyncTask> tasks = new SparseArray<>();


    public SuggestAdapter(MainActivity activity, int resource, List<ModelObject> items) {
        super(activity, resource, items);
        this.activity = activity;
        this.originalItems = items;
        this.items = new ArrayList<>(items);

        bitmapCache = new ModelIconCache();

        taskExecutor = new TaskExecutor(activity);
    }

    @Override
    public void notifyDataSetChanged() {
        //Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        //v.vibrate(15);
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ModelObject getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parentView) {
        SuggestRow row;
        final ModelObject m = getItem(i);
        if (view == null) {
            row = new SuggestRow(activity, null);
        } else {
            row = (SuggestRow) view;
        }

        row.resetIcon();
        row.setTag(m.uid);

        row.showProgress();
        AsyncTask task = new AsyncTask<SuggestRow, Void, Bitmap>() {
            private SuggestRow v;

            @Override
            protected Bitmap doInBackground(SuggestRow... params) {
                v = params[0];
                Bitmap b = bitmapCache.get(m.uid);
                if (null != b) return b;
                b = m.getBitmap(activity);
                if (null == b) return null;
                bitmapCache.put(m.uid, b);
                return b;
            }

            @Override
            protected void onPreExecute() {
                if (v == null) return;
                if (v.getTag() != m.uid) return;
                v.resetIcon();
                v.showProgress();
            }

            @Override
            protected void onCancelled() {
                if (v == null) return;
                if (v.getTag() != m.uid) return;
                v.resetIcon();
                v.hideProgress();
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);

                tasks.remove(m.getIncrementedId());
                if (v.getTag() != m.uid) return;
                if (null == result) {
                    v.setIcon(null);
                    v.hideProgress();
                    return;
                }
                v.setIcon(Bitmap.createBitmap(result));
                v.hideProgress();
            }
        }.execute(row);
        tasks.append(m.getIncrementedId(), task);

        row.setTitle(m.name);
        row.setSubTitle(m.getDescription());
        row.setDesc(String.valueOf(m.searchWeight));


        return row;
    }

//  @Override
//  public Filter getFilter() {
//    return filter;
//  }

    private void stopAndRemoveTask(int id) {
        AsyncTask t = tasks.get(id);
        if (t != null) t.cancel(true);
        tasks.remove(id);
    }

    @Override
    public void remove(ModelObject object) {
        stopAndRemoveTask(object.getIncrementedId());
        super.remove(object);
    }
}
