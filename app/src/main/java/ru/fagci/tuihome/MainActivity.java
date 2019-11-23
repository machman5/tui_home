package ru.fagci.tuihome;

import android.Manifest;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.neurenor.permissions.PermissionCallback;
import com.neurenor.permissions.PermissionsHelper;
import ru.fagci.tuihome.loader.AppLoaderTask;
import ru.fagci.tuihome.loader.ContactLoaderTask;
import ru.fagci.tuihome.loader.MediaLoaderTask;
import ru.fagci.tuihome.model.ModelObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ModelObject>> {
    private static final int LOADER_APPS = 1;
    private static final int LOADER_CONTACTS = 2;
    private static final int LOADER_MEDIA = 3;

    private static HashMap<String, Long> timing = new HashMap<>();

    private PermissionsHelper permissionHelper;
    private String[] permissions = new String[]{
            CALL_PHONE,
            READ_CONTACTS,
            PACKAGE_USAGE_STATS,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
    };

    private static LoaderManager loaderManager;
    private static List<ModelObject> modelObjects = new ArrayList<>();

    private TextView output;
    private RecyclerView cmdChain;
    private CmdChainAdapter cmdChainAdapter;

    private void makeSearch(final String query) {
        Runnable r = new Runnable() {
            final ArrayList<ModelObject> filteredList = new ArrayList<>();
            final Pattern p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);

            @Override
            public void run() {
                for (final ModelObject item : modelObjects) {
                    if (item.search(query, p)) filteredList.add(item);
                }
                cmdChainAdapter.edit().replaceAll(filteredList).commit();
                cmdChain.scrollToPosition(0);
            }
        };

        r.run();
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked cast")
    public Loader<List<ModelObject>> onCreateLoader(int loaderId, @Nullable Bundle args) {
        Loader<?> loader = null;

        switch (loaderId) {
            case LOADER_CONTACTS:
                loader = new ContactLoaderTask(getApplicationContext());
                break;
            case LOADER_APPS:
                loader = new AppLoaderTask(getApplicationContext());
                break;
            case LOADER_MEDIA:
                loader = new MediaLoaderTask(getApplicationContext());
                break;
        }

        if (loader != null) {
            String name = loader.getClass().getSimpleName();
            timing.put(name, System.nanoTime());
            output.append(name + " start\n");
        }
        return (Loader<List<ModelObject>>) loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<ModelObject>> loader, List<ModelObject> items) {
        modelObjects.addAll(items);
        cmdChainAdapter.edit().replaceAll(items).commit();
        String name = loader.getClass().getSimpleName();
        long t = System.nanoTime() - timing.get(name);
        output.append(name + " finished (" + items.size() + "), " + String.format("%.2fms", t / 1000000.0f) + "\n");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<ModelObject>> loader) {
        output.append(loader.getClass().getSimpleName() + " reset\n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SearchView cmdLine = findViewById(R.id.cmdline);
        output = findViewById(R.id.output);
        cmdChain = findViewById(R.id.main_commandsChain);

        cmdLine.setIconifiedByDefault(false);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, RecyclerView.HORIZONTAL, false);
        cmdChain.setLayoutManager(layoutManager);
        cmdChainAdapter = new CmdChainAdapter(this);
        cmdChain.setAdapter(cmdChainAdapter);
        cmdChain.setNestedScrollingEnabled(false);

        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOADER_APPS, null, this);

        permissionHelper = new PermissionsHelper(this);
        permissionHelper.requestPermissions(permissions, new PermissionCallback() {
            @Override
            public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> p) {
                for (String perm : p.keySet()) {
                    PermissionsHelper.PermissionGrant g = p.get(perm);
                    if (g == null || !g.equals(PermissionsHelper.PermissionGrant.GRANTED)) continue;
                    switch (perm) {
                        case Manifest.permission.READ_CONTACTS:
                            loaderManager.initLoader(LOADER_CONTACTS, null, MainActivity.this);
                            break;
                        case Manifest.permission.READ_EXTERNAL_STORAGE:
                            loaderManager.initLoader(LOADER_MEDIA, null, MainActivity.this);
                            break;
                    }
                }
            }
        });

        cmdLine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String p1) {
                // TODO: Implement this method
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                makeSearch(query);
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(permissions, grantResults);
    }
}
