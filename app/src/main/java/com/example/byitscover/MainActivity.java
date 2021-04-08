package com.example.byitscover;

import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.byitscover.helpers.AsynchronousOperation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Main Activity behind the entire app. This creates the app on startup and calls the first page
 * to be created. There is also an accompanying .xml in src/res/layout which can be used to apply
 * layouts or elements to all fragments.
 *
 * @author Auto-Generated
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    private static final String[] REQUIRED_PERMISSION = new String[] {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_CODE_PERMISSIONS = 1001; //was 200 when working

    private boolean hasCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result1== PackageManager.PERMISSION_GRANTED && result2==PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        Toast.makeText(MainActivity.this, "Requesting Permission", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSIONS);
        Toast.makeText(MainActivity.this, "Requested Permission", Toast.LENGTH_SHORT).show();

    }

    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults){
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (locationAccepted && cameraAccepted) {
                    Toast.makeText(MainActivity.this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_SHORT).show();
                    //enableCamera();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_SHORT).show();
                    requestPermission();

                }
            }
        }
    }



    /**
     * Called when app is opened. Starts the entire process.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_first);//changed
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //View view = inflater.inflate(R.layout.fragment_first, container, false);
        findViewById(R.id.searchByTitleButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_from_first_to_info);
            }
        });

        Button enableCamera = findViewById(R.id.searchByCoverButton);
        enableCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    Toast.makeText(MainActivity.this, "Has Permission", Toast.LENGTH_SHORT).show();
                    //enableCamera();
                } else if (shouldShowRequestPermissionRationale("Need Access to Camera to move forward.")) {
                    Toast.makeText(MainActivity.this, "Need Access to Camera to move forward.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Needs to request permission", Toast.LENGTH_SHORT).show();

                    requestPermission();
                }
            }
        });
    }

    /**
     * Adds items to the action bar if it is present.
     * @param menu is the action bar that is created
     * @return successful completion
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    /**
     * Handle action bar item clicks here. The action bar will automatically handle clicks on the
     * Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item is the button that is selected
     * @return calls method to deal with specific item selected
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}