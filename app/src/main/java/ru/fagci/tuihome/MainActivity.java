package ru.fagci.tuihome;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.neurenor.permissions.PermissionsHelper;
import ru.fagci.tuihome.adapters.ModelCategoryListAdapter;
import ru.fagci.tuihome.adapters.ModelListAdapter;
import ru.fagci.tuihome.decoration.SpacesItemDecoration;
import ru.fagci.tuihome.repository.AppRepository;
import ru.fagci.tuihome.repository.ContactsRepository;
import ru.fagci.tuihome.repository.MediaRepository;
import ru.fagci.tuihome.vm.*;

import java.util.ArrayList;
import java.util.HashMap;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity {
    private final String[] permissions = new String[]{
            CALL_PHONE,
            READ_CONTACTS,
            PACKAGE_USAGE_STATS,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
    };

    private ModelListAdapter modelListAdapter;
    private ModelCategoryListAdapter modelCategoryListAdapter;
    private PermissionsHelper permissionsHelper;

    private MergedViewModel mergedViewModel;

    private FilterState filter = new FilterState();

    private void makeSearch(final String query) {
        filter.setQuery(query);
        mergedViewModel.filterModel(filter);
        mergedViewModel.getData().observe(this, this::updateModelListItems);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        modelListAdapter = new ModelListAdapter();
        modelCategoryListAdapter = new ModelCategoryListAdapter();

        SearchView cmdLine = findViewById(R.id.cmdline);
        RecyclerView modelListView = findViewById(R.id.main_commandsChain);
        RecyclerView modelCategoryListView = findViewById(R.id.main_modelCategorySwitcher);

        cmdLine.setIconifiedByDefault(false);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, RecyclerView.VERTICAL, false);

        modelListView.addItemDecoration(new SpacesItemDecoration(8));
        modelListView.setLayoutManager(layoutManager);


        modelListView.setAdapter(modelListAdapter);
        modelListView.setNestedScrollingEnabled(false);

        modelCategoryListView.setAdapter(modelCategoryListAdapter);
        RecyclerView.LayoutManager linearLayoutManager = new StaggeredGridLayoutManager(1, RecyclerView.HORIZONTAL);
        modelCategoryListView.setLayoutManager(linearLayoutManager);

        mergedViewModel = ViewModelProviders.of(this).get(MergedViewModel.class);

        Log.i("LC", "Mounting Apps live data");
        ModelViewModel appViewModel = ViewModelProviders.of(this, new ViewModelFactory(new AppRepository(getApplication()))).get(AppViewModel.class);
        modelCategoryListAdapter.addItem(appViewModel);
        mergedViewModel.addDataSource(appViewModel.getData());

        permissionsHelper = new PermissionsHelper(this);
        permissionsHelper.requestPermissions(permissions, this::onResponseReceived);

        mergedViewModel.getData().observe(this, this::updateModelListItems);

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

    private void updateModelListItems(ModelObjectMap models) {
        modelListAdapter.setData(new ArrayList<>(models.values()));
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        permissionsHelper.onRequestPermissionsResult(permissions, grantResults);
    }

    private void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> grants) {
        final String permissions = "Permissions";
        for (String perm : grants.keySet()) {
            PermissionsHelper.PermissionGrant g = grants.get(perm);
            if (g == null || !g.equals(PermissionsHelper.PermissionGrant.GRANTED)) continue;
            switch (perm) {
                case READ_CONTACTS:
                    Log.i(permissions, "Mounting Contacts live data with perm " + perm);
                    ModelViewModel contactViewModel = ViewModelProviders.of(this, new ViewModelFactory(new ContactsRepository(getApplication()))).get(ContactViewModel.class);
                    modelCategoryListAdapter.addItem(contactViewModel);
                    mergedViewModel.addDataSource(contactViewModel.getData());
                    break;
                case READ_EXTERNAL_STORAGE:
                    Log.i(permissions, "Mounting Internal storage live data with perm " + perm);
                    ModelViewModel mediaViewModel = ViewModelProviders.of(this, new ViewModelFactory(new MediaRepository(getApplication()))).get(MediaViewModel.class);
                    modelCategoryListAdapter.addItem(mediaViewModel);
                    mergedViewModel.addDataSource(mediaViewModel.getData());
                    break;
                default:
                    Log.i(permissions, "Unexpected value: " + perm);
            }
        }
    }
}
