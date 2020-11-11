package madspild.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.madspild.R;

import java.util.Objects;

public class ScanFragment extends Fragment {
    private CodeScanner codeScanner;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.fragment_scan, container, false);
        codeScanner = new CodeScanner(Objects.requireNonNull(getActivity()), (CodeScannerView)root.findViewById(R.id.scanner_view));

        if(!hasCameraPermission()) {
            requestCameraPermission();
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(hasCameraPermission())  {
            codeScanner.startPreview();
        }
    }

    @Override
    public void onPause() {
        if(hasCameraPermission()) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }

    private boolean hasCameraPermission(){
        return ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA},1);
    }
}