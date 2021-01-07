package madspild.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.madspild.R;
import com.google.zxing.Result;

import java.util.Objects;

import madspild.Models.Datamatrix;

public class ScanFragment extends Fragment {
    private CodeScanner codeScanner;

    Datamatrix product;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DataMatrixDataPref = "DataMatrixData";

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.fragment_scan, container, false);
        codeScanner = new CodeScanner(Objects.requireNonNull(getActivity()), (CodeScannerView)root.findViewById(R.id.code_scanner_view));

        if(!hasCameraPermission()) {
            requestCameraPermission();
        }

        //til at skanne
        final Activity activity = getActivity();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {

                    private Context context; //skal bruge en context

                    @Override
                    public void run() {
                        /*
                        product.setDataMatrixData(result.getText());
                        pc.addProduct(this.context, whatever);
                        */
                        String DataMatrixData = result.getText();
                        saveData(DataMatrixData);

                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return root;
    }

    public void saveData(String DataMatrixData) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DataMatrixDataPref, DataMatrixData);
        editor.apply();
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