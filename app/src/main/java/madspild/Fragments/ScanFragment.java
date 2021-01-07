package madspild.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.AuthenticationClient;
import madspild.HttpClient.ProductClient;
import madspild.Models.Product;

import static java.lang.Long.parseLong;

public class ScanFragment extends Fragment {
    private CodeScanner codeScanner;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DataMatrixDataPref = "DataMatrixData";

    ProductClient productClient;
    Product product;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.fragment_scan, container, false);
        codeScanner = new CodeScanner(Objects.requireNonNull(getActivity()), (CodeScannerView)root.findViewById(R.id.code_scanner_view));

        productClient = new ProductClient();

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
                        //hvad er det her???
                        product.setDataMatrixData(result.getText());
                        pc.addProduct(this.context, whatever);
                        */

                        //get Datamatrix data from result (from the scanner)
                        String DataMatrixData = result.getText();
                        saveData(DataMatrixData);

                        //clean up string from weird symbols
                        DataMatrixData = DataMatrixData.replaceAll("[^a-zA-Z0-9]", "");
                        System.out.println(DataMatrixData);

                        //substring numbers: GTIN: 3-16, S/N: 19-29, Batch: 32-37, Expiry: 40-45
                        Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();

                        if (DataMatrixData.length() == 45){
                            product = new Product();
                            product = MatrixtoProduct(DataMatrixData);

                            productClient.createProduct(product, (respObject) -> {
                                Product product = (Product) respObject;
                                System.out.println("bananskrald");
                            }, (respError) -> {
                                Log.println(Log.ERROR, "du har lavet en fejl", respError);
                            });
                        } else{
                            System.out.println("fejl i barcode");
                        }
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

    //MatrixtoProduct, MtP
    public Product MatrixtoProduct(String DataMatrixString){

        DateFormat format = new SimpleDateFormat("yyMMdd", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(DataMatrixString.substring(39, 45));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Product MtPproduct= new Product();
        String SerialNumberTemp = DataMatrixString.substring(18,29);
        if(SerialNumberTemp.equals("00000000000")){
            MtPproduct.setSerialnumber(null);
        } else {
            MtPproduct.setSerialnumber(SerialNumberTemp);
        }
        //MtPproduct.setSerialnumber(DataMatrixString.substring(18,29));
        //MtPproduct.setSerialnumber(null);
        MtPproduct.setExpdate(date);
        MtPproduct.setBatchnumber(DataMatrixString.substring(31,37));
        MtPproduct.setGtin(DataMatrixString.substring(2,16));
        MtPproduct.setFamilyid(HttpClientHelper.user.getFamilyid());
        MtPproduct.setDeleted(false);

        return MtPproduct;
    }

    public Product MatrixtoProductSpecial(){
        /*
        //making a char array for datamatrix data
        // Creating array of string length
        char[] DataMatrixArr = new char[DataMatrixData.length()];

        // Copy character by character into array
        for (int i = 0; i < DataMatrixData.length(); i++) {
            DataMatrixArr[i] = DataMatrixData.charAt(i);
        }

        boolean done = false;
        int dmC = 0; //DataMatrixCounter

        while (!done){
            if (dmC>DataMatrixArr.length){
                done = true;
                continue;
            }

            if(DataMatrixArr[dmC] == 0 && DataMatrixArr[dmC+1] == 1){
                dmC+=14;
            }


            else{
                dmC++;
            }
        }

        String temp;
         */
        return product;
    }

    private boolean hasCameraPermission(){
        return ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA},1);
    }
}