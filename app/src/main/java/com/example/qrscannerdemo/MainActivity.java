package com.example.qrscannerdemo;

import static android.Manifest.permission.CAMERA;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Size;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    
    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private TextView qrCodeTextView;
    private String qrCode;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        // Initialize UI elements
        previewView = findViewById(R.id.activity_main_previewView);
        qrCodeTextView=findViewById(R.id.textView);
    
        // Clear the QR code text when the TextView is clicked
        qrCodeTextView.setOnClickListener(view-> qrCodeTextView.setText(""));
        // Initialize camera provider future
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        // Request camera permission
        requestCamera();
    }
    
    private void requestCamera() {
        // Check if camera permission is granted
        if (ActivityCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Camera permission granted, start the camera
            startCamera();
        } else {
            // Request camera permission if not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, start the camera
                startCamera();
            } else {
                // Show a message if camera permission is denied
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void startCamera() {
        // Listen for the camera provider future
        cameraProviderFuture.addListener(() -> {
            try {
                // Get the camera provider
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            
                // Bind the camera preview
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // Handle any errors when starting the camera
                Toast.makeText(this, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }
    
    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        // Create a preview for the camera
        Preview preview = new Preview.Builder()
                .build();
    
        // Specify the camera selector (e.g., rear camera)
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
    
        // Set the preview surface provider
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
    
        // Create an image analysis use case for processing frames
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
    
        // Set an analyzer to process frames for QR code detection
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                // Handle the case when a QR code is found
                qrCode = _qrCode;
                qrCodeTextView.setText(qrCode);
            }
        
            @Override
            public void qrCodeNotFound() {
                // Handle the case when no QR code is found (optional)
            }
        }));
    
        // Bind the camera to the lifecycle
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageAnalysis, preview);
    }
}