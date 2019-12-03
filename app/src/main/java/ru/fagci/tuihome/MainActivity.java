package ru.fagci.tuihome;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.neurenor.permissions.PermissionsHelper;
import ru.fagci.tuihome.decoration.SpacesItemDecoration;
import ru.fagci.tuihome.model.AppModel;
import ru.fagci.tuihome.model.ModelObject;
import ru.fagci.tuihome.vm.AppViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.*;

public class MainActivity extends AppCompatActivity  {
    private PermissionsHelper permissionHelper;
    private final String[] permissions = new String[]{
            CALL_PHONE,
            READ_CONTACTS,
            PACKAGE_USAGE_STATS,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
    };

    private static List<ModelObject> modelObjects = new ArrayList<>();

    private TextView output;
    private RecyclerView cmdChain;
    private CmdChainAdapter cmdChainAdapter;

//    private void makeSearch(final String query) {
//        Runnable r = new Runnable() {
//            final ArrayList<ModelObject> filteredList = new ArrayList<>();
//            final Pattern p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
//
//            @Override
//            public void run() {
//                for (final ModelObject item : modelObjects) {
//                    if (item.search(query, p)) filteredList.add(item);
//                }
//                cmdChainAdapter.edit().replaceAll(filteredList).commit();
//                cmdChain.scrollToPosition(0);
//            }
//        };
//
//        r.run();
//    }

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


//        permissionHelper = new PermissionsHelper(this);
//        permissionHelper.requestPermissions(permissions, new PermissionCallback() {
//            @Override
//            public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> p) {
//                for (String perm : p.keySet()) {
//                    PermissionsHelper.PermissionGrant g = p.get(perm);
//                    if (g == null || !g.equals(PermissionsHelper.PermissionGrant.GRANTED)) continue;
//                    switch (perm) {
//                        case Manifest.permission.READ_CONTACTS:
//                            loaderManager.initLoader(LOADER_CONTACTS, null, MainActivity.this);
//                            break;
//                        case Manifest.permission.READ_EXTERNAL_STORAGE:
//                            loaderManager.initLoader(LOADER_MEDIA, null, MainActivity.this);
//                            break;
//                    }
//                }
//            }
//        });
//
//        cmdLine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String p1) {
//                // TODO: Implement this method
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(final String query) {
//                makeSearch(query);
//                return true;
//            }
//        });


        AppViewModel appModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appModel.getData().observe(this, new Observer<List<AppModel>>() {
            @Override
            public void onChanged(List<AppModel> appModels) {
                cmdChainAdapter.setData(appModels);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(permissions, grantResults);
    }
}
