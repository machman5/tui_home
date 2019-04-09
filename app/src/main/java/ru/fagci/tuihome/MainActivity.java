package ru.fagci.tuihome;
import android.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.widget.*;
import android.widget.*;
import com.neurenor.permissions.*;
import java.util.*;
import java.util.regex.*;
import ru.fagci.tuihome.loader.*;
import ru.fagci.tuihome.model.*;

import android.widget.SearchView;
import ru.fagci.tuihome.lib.*;

public class MainActivity extends Activity {
  private static final int LOADER_APPS = 1;
  private static final int LOADER_CONTACTS = 2;
  private static final int LOADER_MEDIA = 3;
  
  private String[] permissions = new String[]{
    Manifest.permission.CALL_PHONE,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.PACKAGE_USAGE_STATS,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
  };

  private SearchView cmdLine;
  private TextView output;
  private RecyclerView cmdChain;
  
  private CmdChainAdapter cmdChainAdapter;
  
  private static LoaderManager loaderManager;
  private static HashMap<String,Long> timing = new HashMap<>();

  private static PermissionsHelper permissionHelper;
  
  private static List<ModelObject> modelObjects = new ArrayList<>();
 
  private final LoaderManager.LoaderCallbacks<?> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<ModelObject>>(){
    @Override
    public Loader<List<ModelObject>> onCreateLoader(int loaderId, Bundle args) {
      Loader<?> loader = null;

      switch (loaderId) {
        case LOADER_CONTACTS:
          loader =  new ContactLoaderTask(getApplicationContext());
          break;
        case LOADER_APPS:
          loader = new AppLoaderTask(getApplicationContext());
          break;
        case LOADER_MEDIA:
          loader = new MediaLoaderTask(getApplicationContext());
          break;
      }

      String name = loader.getClass().getSimpleName();
      timing.put(name, System.nanoTime());
      output.append(name + " start\n");
      return (Loader<List<ModelObject>>)loader;
    }

    @Override
    public void onLoadFinished(Loader<List<ModelObject>> loader, List<ModelObject> items) {
      modelObjects.addAll(items);
      cmdChainAdapter.edit().replaceAll(items).commit();
      //cmdChainAdapter.notifyDataSetChanged();
      String name = loader.getClass().getSimpleName();
      long t = System.nanoTime() - timing.get(name);
      output.append(name + " finished (" + items.size() + "), "+String.format("%.2fms", t/1000000.0f)+"\n");
    }

    @Override
    public void onLoaderReset(Loader<List<ModelObject>> loader) {
      output.append(loader.getClass().getSimpleName() + " reset\n");
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    cmdLine = findViewById(R.id.cmdline);
    output = findViewById(R.id.output);
    cmdChain = findViewById(R.id.main_commandsChain);

    cmdLine.setIconifiedByDefault(false);

    GridLayoutManager layoutManager= new GridLayoutManager(this, 4, LinearLayoutManager.HORIZONTAL, false);
    cmdChain.setLayoutManager(layoutManager);
    cmdChainAdapter = new CmdChainAdapter(this);
    cmdChain.setAdapter(cmdChainAdapter);
    //cmdChain.setHasFixedSize(true);
    cmdChain.setNestedScrollingEnabled(false);

    loaderManager = getLoaderManager();
    loaderManager.initLoader(LOADER_APPS, null, loaderCallbacks);

    permissionHelper = new PermissionsHelper(this);
    permissionHelper.requestPermissions(permissions, new PermissionCallback(){
        @Override
        public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> p) {
          for (String perm: p.keySet()) {
            PermissionsHelper.PermissionGrant g = p.get(perm);
            if (!g.equals(PermissionsHelper.PermissionGrant.GRANTED)) continue;
            switch (perm) {
              case Manifest.permission.READ_CONTACTS:
                loaderManager.initLoader(LOADER_CONTACTS, null, loaderCallbacks);
                break;
              case Manifest.permission.READ_EXTERNAL_STORAGE:
                loaderManager.initLoader(LOADER_MEDIA, null, loaderCallbacks);
                break;
            }
          }
        }
      });

    cmdLine.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
        @Override
        public boolean onQueryTextSubmit(String p1) {
          // TODO: Implement this method
          return false;
        }

        @Override
        public boolean onQueryTextChange(final String query) {
          final Pattern p = Pattern.compile(query,Pattern.CASE_INSENSITIVE);
          final ArrayList<ModelObject> filteredList = new ArrayList<>();
          Runnable r = new Runnable(){

            @Override
            public void run() {
              for(final ModelObject item: modelObjects) {
                if(item.search(query, p)) filteredList.add(item);
              }
            }
          };
          
          r.run();
          
          cmdChainAdapter.edit().replaceAll(filteredList).commit();
          cmdChain.scrollToPosition(0);
          return true;
        }
      });
  }

  @Override
  public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
    permissionHelper.onRequestPermissionsResult(permissions, grantResults);
  }
}
