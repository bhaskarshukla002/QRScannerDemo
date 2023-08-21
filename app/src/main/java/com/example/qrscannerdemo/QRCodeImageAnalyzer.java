package com.example.qrscannerdemo;

import static android.graphics.ImageFormat.YUV_420_888;
import static android.graphics.ImageFormat.YUV_422_888;
import static android.graphics.ImageFormat.YUV_444_888;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;

import java.nio.ByteBuffer;

public class QRCodeImageAnalyzer implements ImageAnalysis.Analyzer {
    private final QRCodeFoundListener listener;
    
    // Constructor to initialize the QRCodeFoundListener
    public QRCodeImageAnalyzer(QRCodeFoundListener listener) {
        this.listener = listener;
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        // Check if the image format is compatible for QR code analysis
        if (image.getFormat() == YUV_420_888 || image.getFormat() == YUV_422_888 || image.getFormat() == YUV_444_888) {
            // Extract image data from the image proxy
            ByteBuffer byteBuffer = image.getPlanes()[0].getBuffer();
            byte[] imageData = new byte[byteBuffer.capacity()];
            byteBuffer.get(imageData);
        
            // Create a PlanarYUVLuminanceSource from the image data
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    imageData,
                    image.getWidth(), image.getHeight(),
                    0, 0,
                    image.getWidth(), image.getHeight(),
                    false
            );
        
            // Create a BinaryBitmap from the luminance source using a HybridBinarizer
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        
            try {
                // Attempt to decode a QR code from the binary bitmap
                Result result = new QRCodeMultiReader().decode(binaryBitmap);
                // Notify the listener when a QR code is found
                listener.onQRCodeFound(result.getText());
            } catch (Exception e) {
                // Notify the listener when no QR code is found
                listener.qrCodeNotFound();
            }
        }
        // Close the image proxy to release resources
        image.close();
    }
}