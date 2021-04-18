package com.example.byitscover;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Activity behind the first/main page. Contains the buttons for search by cover and search by title.
 *
 * @author Emily Schroeder
 * @version 1.0
 */
public class FirstActivity extends AppCompatActivity {
    private static final String[] REQUIRED_PERMISSION = new String[] {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_CODE_PERMISSIONS = 1001; //was 200 when working

    Button enableCamera;

    /**
     * Checks to see if the application has permission to use the camera and to write to external storage
     *
     * @return results of permission check for camera and external storage write
     */
    private boolean hasCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result1==PackageManager.PERMISSION_GRANTED && result2==PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests permission from the phone to use the camera and write to external storage
     */
    private void requestPermission() {
        Toast.makeText(FirstActivity.this, "Requesting Permission", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSIONS);
        Toast.makeText(FirstActivity.this, "Requested Permission", Toast.LENGTH_SHORT).show();

    }

    /**
     * Runs after the permissions have been checked.
     * If it has the necessary permissions, it will start the camera.
     * If it does not have the necessary permissions, it will request them.
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults){
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0) {

                boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (locationAccepted && cameraAccepted) {
                    Toast.makeText(FirstActivity.this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_SHORT).show();
                    enableCamera();
                    //enableCamera.setVisibility(View.GONE);
                    //enableCamera.setClickable(false);
                } else {
                    Toast.makeText(FirstActivity.this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_SHORT).show();
                    requestPermission();

                }
            }
        }
    }

    /**
     * This will start the camera activity
     */
    private void enableCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    /**
     * Called when app is opened. Displays the main page and sets listeners for the buttons to go to camera or info enter page.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstactivity);

        enableCamera = findViewById(R.id.searchByCoverButton);
        enableCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (hasCameraPermission()) {
                    Toast.makeText(FirstActivity.this, "Has Permission", Toast.LENGTH_SHORT).show();
                    enableCamera();
                } else if (shouldShowRequestPermissionRationale("Need Access to Camera to move forward.")) {
                    Toast.makeText(FirstActivity.this, "Need Access to Camera to move forward.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(FirstActivity.this, "Needs to request permission", Toast.LENGTH_SHORT).show();

                    requestPermission();
                }
            }
        });
        Button textEnter = findViewById(R.id.searchByTitleButton);
        textEnter.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, InfoPage.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Activity for the camera to work. Opens the camera and saves the image to local files, then moves to review page.
     */
    public static class CameraActivity extends AppCompatActivity {
        PreviewView previewView;
        Button captureImage;
        private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

        private Executor executor;


        /**
         * Will create the camera page
         * Set listener for previous button to return to home page
         * Set listener to capture the image and move to review page
         * @param savedInstanceState
         */
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_camera);

            Button prev = findViewById(R.id.previous);
            prev.setOnClickListener(new View.OnClickListener(){
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            executor = Executors.newSingleThreadExecutor();
            captureImage = findViewById(R.id.captureImg);
            previewView = findViewById(R.id.camera);
            cameraProviderFuture = ProcessCameraProvider.getInstance(this);
            cameraProviderFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        bindPreview(cameraProvider);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, ContextCompat.getMainExecutor(this));
        }

        /**
         * Gets path to directory to save the image to
         *
         * @return directory to save image to
         */
        public String getBatchDirectoryName() {

            String app_folder_path = "";
            app_folder_path = this.getFilesDir().getAbsolutePath() + "/images";
            Log.d("MYAPP", "Path: " + app_folder_path);
            File dir = new File(app_folder_path);
            if (!dir.exists() && !dir.mkdirs()) {

            }

            return app_folder_path;
        }

        /**
         * Binds the preview to the page so the user can see through the camera
         *
         * @param cameraProvider
         */
        void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {

            Preview preview = new Preview.Builder()
                    .build();

            CameraSelector cameraSelector = new CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build();

            ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                    .build();

            ImageCapture.Builder builder = new ImageCapture.Builder();

            //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
            HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

            // Query if extension is available (optional).
            if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
                // Enable the extension if available.
                hdrImageCaptureExtender.enableExtension(cameraSelector);
            }

            final ImageCapture imageCapture = builder
                    .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                    .build();
            preview.setSurfaceProvider(previewView.getSurfaceProvider());
            Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis, imageCapture);

            captureImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                    File file = new File(getBatchDirectoryName(), "SearchBook.jpg"); //changed to use the same name everytime

                    ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
                    imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback () {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                            Intent intent = new Intent(CameraActivity.this, LoadingActivity.class);
                            intent.putExtra("image_path", file.getAbsolutePath());
                            finish();
                            startActivity(intent);
                        }
                        @Override
                        public void onError(@NonNull ImageCaptureException error) {
                            error.printStackTrace();
                        }
                    });
                }
            });

        }
    }

}