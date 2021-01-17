package madspild.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.madspild.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.sentry.Sentry;
import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.ProductClient;
import madspild.Models.Product;

public class ScanFragment extends Fragment {
    private CodeScanner codeScanner;

    ProductClient productClient;
    Product product;

    //Material design elementer til fejl/bekræftelses beskeder
    MaterialAlertDialogBuilder dialog;
    Snackbar snackbar;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        View root = i.inflate(R.layout.fragment_scan, container, false);
        codeScanner = new CodeScanner(Objects.requireNonNull(getActivity()), (CodeScannerView)root.findViewById(R.id.fragment_scan_codescanner));
        productClient = new ProductClient();

        if(!hasCameraPermission()) {
            requestCameraPermission();
        }

        //Material design dialog til fejl beskeder
        dialog = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));

        //til at skanne
        final Activity activity = getActivity();
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {

                    private Context context; //skal bruge en context

                    @Override
                    public void run() {
                        //get Datamatrix data from result (from the scanner)
                        String DataMatrixData = result.getText();

                        //clean up string from weird symbols
                        DataMatrixData = DataMatrixData.replaceAll("[^a-zA-Z0-9]", "");
                        System.out.println(DataMatrixData);

                        //En række af tjek på den skannede stregkode
                        //længden af dataen, varen er tilbagekaldt, varen for gammel og om den er succesfuldt gemt til profilen
                        if (DataMatrixData.length() == 45){
                            product = new Product();
                            product = MatrixtoProduct(DataMatrixData);

                            //specifik ekstra check ønsket af projektstiller
                            if(specielScanEnFisk(product)){
                                errorMessageDialog("Success","Fisk er blevet tilføjet til systemet");
                            }

                            else if(tilbagekaldtBatchTest(product)){
                                errorMessageDialog("Fare","Denne batch er blevet tilbagekaldt, kontakt medarbejder");
                            }

                            else if(expDateChecker(product)){
                                productClient.createProduct(product, (respObject) -> {
                                    Product product = (Product) respObject;
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        //dialog message
                                        //errorMessageDialog("Success","Produkt tilføjet");

                                        //en snackbar i stedet for
                                        snackbar = Snackbar.make(root,"Produkt tilføjet ", 3000);
                                        snackbar.setAnchorView(R.id.activity_main_viewpager_navigation);
                                        snackbar.show();

                                        onResume();
                                    });
                                }, (respError) -> {
                                    //Log.println(Log.ERROR, "du har lavet en fejl", respError);
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        //dialog message, ex: varen er allerede skannet
                                        errorMessageDialog("Fejl",respError);
                                        Sentry.captureMessage("Error - failed adding product to inventory of user");
                                    });
                                });
                            }
                            else {
                                //hvis varen er udløbet
                                errorMessageDialog("Fejl","Varen er udløbet");
                            }
                        } else{
                            //for kort eller forkert data
                            errorMessageDialog("Fejl","Fejl i barcode");
                        }
                    }
                });
            }
        });
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
        if(snackbar != null){
            snackbar.dismiss();
        }
        if(hasCameraPermission()) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }

    public boolean specielScanEnFisk(Product product){
        String fiskegtin = product.getGtin();
        String fiskeSerienummer = product.getSerialnumber();
        return fiskegtin.equals("12345678912345") && fiskeSerienummer.equals("54321543215");
    }

    //et sæt af varer til simulering af tilbagekaldte varer udfra batchnr.
    public boolean tilbagekaldtBatchTest(Product TestonBatch){
        String BatchNo = TestonBatch.getBatchnumber();
        ArrayList<String> TilbagekaldteBatches = new ArrayList<String>();
        TilbagekaldteBatches.add("003GS7"); //frosne ærter
        TilbagekaldteBatches.add("003GS8"); //kidney bønner
        TilbagekaldteBatches.add("002GS6"); //coca cola

        return TilbagekaldteBatches.contains(BatchNo);
    }

    //dialog bokse med fejlbeskeder
    public void errorMessageDialog(String title, String message){
        //System.out.println("fejl i barcode");
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setOnDismissListener(dialogInterface -> {
            onResume();
        });
        dialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onResume();
            }
        });
        dialog.show();
    }

    //checker for om dato er for gammel
    public boolean expDateChecker(Product product){
        Date productExpiryDate = product.getExpdate();

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss",Locale.ENGLISH);
        Date currentdate = new Date();
        System.out.println(df.format(currentdate));
        System.out.println(df.format(productExpiryDate));

        return currentdate.getTime() < productExpiryDate.getTime();
    }

    //MatrixtoProduct, MtP. tager dataen og mapper det til vores product object
    public Product MatrixtoProduct(String DataMatrixString){
        //substring numbers: GTIN: 3-16, S/N: 19-29, Batch: 32-37, Expiry: 40-45
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

    private boolean hasCameraPermission(){
        return ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA},1);
    }
}