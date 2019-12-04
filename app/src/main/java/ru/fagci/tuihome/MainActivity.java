package ru.fagci.tuihome;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.neurenor.permissions.PermissionsHelper;
import ru.fagci.tuihome.decoration.SpacesItemDecoration;
import ru.fagci.tuihome.vm.AppViewModel;
import ru.fagci.tuihome.vm.ContactViewModel;
import ru.fagci.tuihome.vm.MediaViewModel;

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

    private AppViewModel appViewModel;
    private ContactViewModel contactViewModel;
    private MediaViewModel mediaViewModel;

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


        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);

        appViewModel.getData().observe(this, models -> cmdChainAdapter.setData(models));

        permissionsHelper = new PermissionsHelper(this);
        permissionsHelper.requestPermissions(permissions, grants -> {
            for (String perm : grants.keySet()) {
                PermissionsHelper.PermissionGrant g = grants.get(perm);
                if (g == null || !g.equals(PermissionsHelper.PermissionGrant.GRANTED)) continue;
                switch (perm) {
                    case READ_CONTACTS:
                        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
                        contactViewModel.getData().observe(this, models -> cmdChainAdapter.setData(models));
                        break;
                    case READ_EXTERNAL_STORAGE:
                        mediaViewModel = ViewModelProviders.of(this).get(MediaViewModel.class);
                        mediaViewModel.getData().observe(this, models -> cmdChainAdapter.setData(models));
                        break;
                }
            }
        });
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


    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        permissionsHelper.onRequestPermissionsResult(permissions, grantResults);
    }
}
