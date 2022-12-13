package arnold.aijuka.midterm_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class Camera extends AppCompatActivity {
    //Initialize variables
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
//            ActivityCompat.requestPermissions(Activity new String[] {Manifest.permission.CAMERA}, requestCode);
//        }

        // hide the status bar
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        //Connect to our preview view in the xml
        previewView = findViewById(R.id.preview_view);

        //Requesting a Camera Provider Instance
        cameraProviderFuture = ProcessCameraProvider.getInstance(getApplicationContext());

        //Request for the camera if it is available
        cameraProviderFuture.addListener(() -> {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        bindPreview(cameraProvider);
                    }
                    //Print the error if an error is to occur
                    catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                },
                ContextCompat.getMainExecutor(getApplicationContext()));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        //Unbind all previous bindings to the camera
        cameraProvider.unbindAll();

        //Create our preview use case (This is integrated inside cameraX)
        Preview preview = new Preview.Builder().build();

        //Select our camera alongside defining the direction the lens is facing (This is also integrated in cameraX)
        CameraSelector cameraSelector = new CameraSelector.Builder().
                requireLensFacing(CameraSelector.LENS_FACING_BACK).build();

        //Setting our preview view from the xml to the cameraX preview instance
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        //Setting camera picture
        ImageCapture imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();

        //Creating the image analysis use case
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().
                setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        //Binding the camera to the preview instance and the camera selector
        androidx.camera.core.Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture, imageAnalysis);

//        imageAnalysis.setAnalyzer();
//        imageCapture.takePicture(new ImageCapture.OnImageCapturedCallback());


    }
}