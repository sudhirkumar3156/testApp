package com.test.demo.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.test.demo.R;

import org.w3c.dom.Text;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    SurfaceView cameraView;
    CameraSource cameraSource;
    TextView outputTextView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        cameraView = (SurfaceView) root.findViewById(R.id.camera_view);
        outputTextView = (TextView)root.findViewById(R.id.text_home);
        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(getContext())
//                        .setBarcodeFormats(Barcode.EAN_13)
//                        .setBarcodeFormats(Barcode.PDF417)
//                        .setBarcodeFormats(Barcode.ISBN)
                        .setBarcodeFormats(Barcode.QR_CODE)//QR_CODE)
                        .build();
        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(2640, 1480)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
//                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    try {
                        outputTextView.setText(barcodes.valueAt(0).displayValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Log.d("sudhir", barcodes.valueAt(0).displayValue);
//                    cameraSource.stop();

//                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
//                        public void run() {
//                            barcodeInfo.setText(    // Update the TextView
//                                    barcodes.valueAt(0).displayValue
//                            );
//                        }
//                    });
                }
            }
        });


        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });
        return root;
    }
}