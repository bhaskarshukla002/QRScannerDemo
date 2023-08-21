package com.example.qrscannerdemo;

public interface QRCodeFoundListener {
    void onQRCodeFound(String qrCode);
    void qrCodeNotFound();
}