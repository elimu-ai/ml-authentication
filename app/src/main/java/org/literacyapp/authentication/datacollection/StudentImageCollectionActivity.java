package org.literacyapp.authentication.datacollection;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.literacyapp.R;
import org.literacyapp.util.DeviceInfoHelper;
import org.literacyapp.util.MediaPlayerHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Collect images of student where eye gaze is directed towards the camera of the device as much
 * as possible.
 */
public class StudentImageCollectionActivity extends AppCompatActivity {

    private RelativeLayout takePictureButton;
    private TextureView textureView;
    private ImageView imageViewAnimal;
    private String animal;
    private String cameraId;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptureSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private int imageCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_image_collection);

        textureView = (TextureView) findViewById(R.id.texture);
        textureView.setSurfaceTextureListener(textureListener);

        imageViewAnimal = (ImageView) findViewById(R.id.animal_image);
        String[] animals = new String[] {"elephant", "sheep", "pig", "cow"};
        int randomIndex = (int) (Math.random() * animals.length);
        Log.i(getClass().getName(), "randomIndex: " + randomIndex);
        animal = animals[randomIndex];
        Log.i(getClass().getName(), "animal: " + animal);
        if ("elephant".equals(animal)) {
            imageViewAnimal.setImageDrawable(getDrawable(R.drawable.elephant));
        } else if ("sheep".equals(animal)) {
            imageViewAnimal.setImageDrawable(getDrawable(R.drawable.sheep));
        } else if ("pig".equals(animal)) {
            imageViewAnimal.setImageDrawable(getDrawable(R.drawable.pig));
        } else if ("cow".equals(animal)) {
            imageViewAnimal.setImageDrawable(getDrawable(R.drawable.cow));
        }

        takePictureButton = (RelativeLayout) findViewById(R.id.btn_takepicture);
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(getClass().getName(), "onClick");
                takePicture();
            }
        });
    }

    @Override
    protected void onStart() {
        Log.i(getClass().getName(), "onStart");
        super.onStart();

        MediaPlayerHelper.play(getApplicationContext(), R.raw.face_instruction);
        Toast.makeText(getApplicationContext(), "Take your tablet and turn your face towards the picture", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MediaPlayerHelper.play(getApplicationContext(), R.raw.face_instruction_press_button);
                Toast.makeText(getApplicationContext(), "Press the button to take a picture", Toast.LENGTH_LONG).show();
            }
        }, 6000);
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.i(getClass().getName(), "onSurfaceTextureAvailable");
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.i(getClass().getName(), "onSurfaceTextureSizeChanged");
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.i(getClass().getName(), "onSurfaceTextureDestroyed");
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//            Log.i(getClass().getName(), "onSurfaceTextureUpdated");
        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            Log.i(getClass().getName(), "onOpened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            Log.i(getClass().getName(), "onDisconnected");
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            Log.w(getClass().getName(), "onError");
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            Log.i(getClass().getName(), "onCaptureCompleted");
            super.onCaptureCompleted(session, request, result);
            createCameraPreview();
        }
    };

    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void takePicture() {
        Log.i(getClass().getName(), "takePicture");

        if (cameraDevice == null) {
            return;
        }

        if ("elephant".equals(animal)) {
            MediaPlayerHelper.play(getApplicationContext(), R.raw.elephant);
        } else if ("sheep".equals(animal)) {
            MediaPlayerHelper.play(getApplicationContext(), R.raw.sheep);
        } else if ("pig".equals(animal)) {
            MediaPlayerHelper.play(getApplicationContext(), R.raw.pig);
        } else if ("cow".equals(animal)) {
            MediaPlayerHelper.play(getApplicationContext(), R.raw.cow);
        }

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if ((jpegSizes != null) && (jpegSizes.length > 0)) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            String collectionPath = Environment.getExternalStorageDirectory() + "/.literacyapp/face_recognition/dataset_collection/";
            File collectionDir = new File(collectionPath);
            if (!collectionDir.exists()) {
                collectionDir.mkdirs();
            }
            String dateFormatted = (String) DateFormat.format("yyyy-MM-dd-HHmmss", Calendar.getInstance());
            String fileName = DeviceInfoHelper.getDeviceId(getApplicationContext()) + "_" + dateFormatted + ".jpg";
            final File file = new File(collectionDir, fileName);
            Log.i(getClass().getName(), "file: " + file);

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Log.i(getClass().getName(), "onImageAvailable");

                    Image image = null;
                    try {
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        Log.e(getClass().getName(), null, e);
                    } catch (IOException e) {
                        Log.e(getClass().getName(), null, e);
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }

                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (output != null) {
                            output.close();
                        }
                    }
                }
            };
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    Log.i(getClass().getName(), "onCaptureCompleted");
                    super.onCaptureCompleted(session, request, result);
                    createCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    Log.i(getClass().getName(), "onConfigured");
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        Log.e(getClass().getName(), null, e);
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {
                    Log.w(getClass().getName(), "onConfigureFailed");
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(getClass().getName(), null, e);
        }

        imageCounter++;
        if (imageCounter < 5) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (imageCounter == 1) {
                        MediaPlayerHelper.play(getApplicationContext(), R.raw.face_instruction_4_more);
                        Toast.makeText(getApplicationContext(), "Take 4 more pictures", Toast.LENGTH_LONG).show();
                    } else if (imageCounter == 2) {
                        MediaPlayerHelper.play(getApplicationContext(), R.raw.face_instruction_3_more);
                        Toast.makeText(getApplicationContext(), "Take 3 more pictures", Toast.LENGTH_LONG).show();
                    } else if (imageCounter == 3) {
                        MediaPlayerHelper.play(getApplicationContext(), R.raw.face_instruction_2_more);
                        Toast.makeText(getApplicationContext(), "Take 2 more pictures", Toast.LENGTH_LONG).show();
                    } else if (imageCounter == 4) {
                        MediaPlayerHelper.play(getApplicationContext(), R.raw.face_instruction_1_more);
                        Toast.makeText(getApplicationContext(), "Take 1 more picture", Toast.LENGTH_LONG).show();
                    }
                }
            }, 2000);
        } else {
            finish();
        }
    }

    protected void createCameraPreview() {
        Log.i(getClass().getName(), "createCameraPreview");

        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Log.i(getClass().getName(), "onConfigured");

                    if (cameraDevice == null) {
                        return;
                    }

                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Log.w(getClass().getName(), "onConfigureFailed");
                }
            }, null);
        } catch (CameraAccessException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    private void openCamera() {
        Log.i(getClass().getName(), "openCamera");

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = manager.getCameraIdList()[1];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(cameraId, stateCallback, null);
        } catch (CameraAccessException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    protected void updatePreview() {
        Log.i(getClass().getName(), "updatePreview");

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(getClass().getName(), null, e);
        }
    }

    private void closeCamera() {
        Log.i(getClass().getName(), "closeCamera");

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (imageReader != null) {
            imageReader.close();
            imageReader = null;
        }
    }

    @Override
    protected void onResume() {
        Log.i(getClass().getName(), "onResume");
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    @Override
    protected void onPause() {
        Log.i(getClass().getName(), "onPause");
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }
}
