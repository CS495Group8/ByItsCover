// TODO: examine possible errors involved in the camera functions

import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.view.PreviewView;

public class Camera {
    // Instantiates a camera which calls callback for every image capture
    // and displays a preview on previewView
    public Camera(ImageCapture.OnImageCapturedCallback callback, PreviewView previewView);

    // Checks if app has permissions to capture images from the camera
    public static boolean checkPermissions();

    // Captures an image and calls the callback passed to the constructor
    public void capture();

    // Cleans up all resources associated with current camera instance
    public void release();
}