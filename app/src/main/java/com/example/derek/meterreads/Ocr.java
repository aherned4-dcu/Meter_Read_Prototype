package com.example.derek.meterreads;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
/**
 *
 * The Ocr class creates the functionality of the OCR activity screen.
 *
 *
 * @link {@link MainActivity}
 *
 * @author – Derek Aherne
 * @version – 25/04/2018
 */
public class Ocr extends BaseActivity {

    SurfaceView cameraView;
    TextView textView;
    TextView textView2;
    Button btn,btnSub;
    CameraSource cameraSource;
    String output;
    final int RequestCameraPermissionID = 1001;
    public static final String TAG = Ocr.class.getSimpleName(); //Log Tag
    String today = getDate();

    /**
     * A method to get permission to use the camera
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCameraPermissionID: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            break;
        }
    }

    @Override
    /**
     * The onCreate method set the content to activity_ocr.
     * Instantiates a TextRecognizer object to capture and concatenate
     * text block through a surface view
     *
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        /* Citation: Method contains code adapted from
         * URL: https://github.com/googlesamples/android-vision.git
         * Permission: MIT Licence Retrieved on:15th March 2018  */
        super.onCreate(savedInstanceState);
        hideAction();
        setContentView(R.layout.activity_ocr);

        cameraView = findViewById(R.id.surface_view);
        textView = findViewById(R.id.text_view);
        textView2 = findViewById(R.id.textView);
        btn =findViewById(R.id.takeReading);
        btnSub =findViewById(R.id.buttonSub);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build(); //Google Vision
        if (!textRecognizer.isOperational()) {
            Log.w(TAG, getString(R.string.detector_dep));
        } else {

            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) { //check for camera permissions

                    try {
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(Ocr.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    RequestCameraPermissionID);
                            return;
                        }
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) { //capture text blocks

                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0)
                    {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i =0;i<items.size();++i)
                                {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                output=stringBuilder.toString();
                                textView.setText(stringBuilder.toString()); //show the text being captured in real time
                            }
                        });
                    }
                }
            });


        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView2.setText(output); //set the captured text to the screen
            }
        } );


    }

    /**
     * A method that passes bundles and validates captured text from the TextRecognizer instance
     * An explicit intent starts Confirm.java
     * @param view
     *
     */
    public void submitOcr(View view) {

        Intent intent=getIntent();
        String mprn = intent.getStringExtra(Constants.MPRN_CON); //get the MPRN input
        String reading = textView2.getText().toString().trim();
        if (reading.matches("[a-zA-Z]+")) { //only continue if we capture numbers
            Toast.makeText(Ocr.this, R.string.num_warning,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent conIntent = new Intent (this,Confirm.class);
        conIntent.putExtra(Constants.MPRN_CON,mprn); //pass the data
        conIntent.putExtra(Constants.READING,reading); //pass the data
        conIntent.putExtra(Constants.DATE,today); //pass the data
        startActivity(conIntent);


    }
}