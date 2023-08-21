# QR Code Scanner App

## About the Project

This Android application is a QR code scanner that uses the device's camera to detect and decode QR codes. It provides real-time scanning of QR codes and displays the decoded content on the screen. This project serves as an example of how to use the Android CameraX library for camera access and the ZXing library for QR code decoding.

![App Screenshot](./images/app_screenshot.png)

## Architecture

The project follows the default (MVC) Model-View-Controller architecture

![Architecture Diagram](./images/architecture_diagram.png)

## How It Works Internally

### MainActivity

- The `MainActivity` is the entry point of the app.
- It handles camera permissions, starts the camera, and displays the camera feed on the screen.
- It sets up an `ImageAnalysis` use case to continuously analyze camera frames for QR codes.
- When a QR code is detected, it updates the UI to display the scanned QR code.

### QRCodeImageAnalyzer

- The `QRCodeImageAnalyzer` class implements the `ImageAnalysis.Analyzer` interface.
- It receives frames from the camera preview and checks if they contain QR codes.
- If a QR code is found, it uses the ZXing library to decode the QR code and notifies the listener.
- If no QR code is found, it notifies the listener that a QR code was not found.

### Layout (activity_main.xml)

- The layout file defines the user interface of the `MainActivity`.
- It contains a `PreviewView` to display the camera feed and a `TextView` to display the scanned QR code.

### Dependencies

This project relies on the following main dependencies:

- **ZXing (Zebra Crossing)**: ZXing is a popular open-source barcode image processing library. In this application, it is used for QR code decoding.
   - Dependency: `com.google.zxing:core:3.4.1`

- **AndroidX Camera Camera2**: This module is part of AndroidX CameraX and provides support for using the Camera2 API, which offers more advanced camera control capabilities for modern Android devices.
   - Dependency: `androidx.camera:camera-camera2:1.0.2`

- **AndroidX Camera Lifecycle**: This module integrates CameraX with AndroidX's lifecycle components, ensuring that camera resources are managed properly based on the app's lifecycle.
   - Dependency: `androidx.camera:camera-lifecycle:1.0.2`

- **AndroidX Camera View**: The CameraX View module provides a simple and easy-to-integrate view for displaying camera previews. It abstracts the complexities of handling camera preview surfaces.
   - Dependency: `androidx.camera:camera-view:1.0.0-alpha31`

## Getting Started

Follow these instructions to build and run the project on your local machine.

### Prerequisites

- Android Studio (latest version)
- Android SDK with API level 21 or higher
- Java Development Kit (JDK) 8 or higher

### Building and Running

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/qr-code-scanner.git

2. Open the project in Android Studio.

3. Build and run the app on an Android emulator or a physical device.

## TODO

- Implement barcode scanning for other formats (e.g., UPC, EAN).
- Add a history feature to store scanned QR codes.
- Improve UI/UX with additional features and customization options.

## Contributing

Contributions are welcome! If you'd like to contribute to this project, please follow these guidelines:

1. Fork the repository and create a new branch for your feature or bug fix.

2. Make your changes and ensure that the code is properly documented.

3. Test your changes thoroughly.

4. Create a pull request with a clear description of the problem or feature and why it's important.

Thank you for considering contributing to this project!
