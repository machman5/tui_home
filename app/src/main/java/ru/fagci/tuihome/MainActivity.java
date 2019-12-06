package ru.fagci.tuihome;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.neurenor.permissions.PermissionsHelper;
import ru.fagci.tuihome.decoration.SpacesItemDecoration;
import ru.fagci.tuihome.repository.AppRepository;
import ru.fagci.tuihome.repository.ContactsRepository;
import ru.fagci.tuihome.repository.MediaRepository;
import ru.fagci.tuihome.vm.*;

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

    private TextView output;
    private RecyclerView cmdChain;
    private CmdChainAdapter cmdChainAdapter;
    private PermissionsHelper permissionsHelper;

    private MergedViewModel mergedViewModel;

    private FilterState filter = new FilterState();

    private void makeSearch(final String query) {
        filter.setQuery(query);
        mergedViewModel.filterModel(filter);
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

        cmdChain.addItemDecoration(new SpacesItemDecoration(8));
        cmdChain.setLayoutManager(layoutManager);
        cmdChainAdapter = new CmdChainAdapter();
        cmdChain.setAdapter(cmdChainAdapter);
        cmdChain.setNestedScrollingEnabled(false);

        mergedViewModel = ViewModelProviders.of(this).get(MergedViewModel.class);

        Log.i("LC", "Mounting Apps live data");
        ModelViewModel appViewModel = ViewModelProviders.of(this, new ViewModelFactory(new AppRepository(getApplication()))).get(AppViewModel.class);
        mergedViewModel.addDataSource(appViewModel.getData());

        permissionsHelper = new PermissionsHelper(this);
        permissionsHelper.requestPermissions(permissions, this::onResponseReceived);

        mergedViewModel.getData().observe(this, models -> cmdChainAdapter.setData(models));

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
                    mergedViewModel.addDataSource(contactViewModel.getData());
                    break;
                case READ_EXTERNAL_STORAGE:
                    Log.i(permissions, "Mounting Internal storage live data with perm " + perm);
                    ModelViewModel mediaViewModel = ViewModelProviders.of(this, new ViewModelFactory(new MediaRepository(getApplication()))).get(MediaViewModel.class);
                    mergedViewModel.addDataSource(mediaViewModel.getData());
                    break;
                default:
                    Log.i(permissions, "Unexpected value: " + perm);
            }
        }
    }
}
