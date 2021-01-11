package madspild.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.madspild.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import madspild.Activities.EditProfileActivity;
import madspild.Activities.StartActivity;
import madspild.Helpers.HttpClientHelper;
import madspild.HttpClient.OverviewClient;
import madspild.Models.Overview;
import madspild.Models.ProductType;


public class ProfileFragment extends Fragment {
    BarChart barChart;
    OverviewClient overviewClient;
    List<Overview> overviewList;
    HashMap<ProductType,Integer> productTypeHashMap;
    TextView amountTitle;
    TextView amount;
    TextView amountWasteTitle;
    TextView amountWaste;
    TextView username;
    View view;
    TextView txtgraph;
    TextView statetxtgraph;
    Button changegraph;


    int highestProduct = 0;
    int expireAmount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        view.findViewById(R.id.fragment_profile_topbar_button_settings).setOnClickListener((event) -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        view.findViewById(R.id.fragment_profile_topbar_button_logout).setOnClickListener((event) -> {
            HttpClientHelper.removeToken();
            Intent intent = new Intent(getActivity(), StartActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).finish();
        });

        productTypeHashMap = new HashMap<>();
        overviewList = new ArrayList<>();

        barChart = view.findViewById(R.id.fragment_profile_barChart);
        amountTitle = view.findViewById(R.id.fragment_profile_amountTitle);
        amount = view.findViewById(R.id.fragment_profile_amount);
        amountWasteTitle = view.findViewById(R.id.fragment_profile_amountWasteTitle);
        amountWaste = view.findViewById(R.id.fragment_profile_amountWaste);
        username = view.findViewById(R.id.fragment_profile_username);
        txtgraph = view.findViewById(R.id.fragment_profile_change_txtgraph);
        statetxtgraph = view.findViewById(R.id.fragment_profile_statetxtgraph);
        changegraph = view.findViewById(R.id.fragment_profile_changegraph);

        txtgraph.setText("Skift graf");
        statetxtgraph.setText("Antal vare");
        changegraph.setText("Skift");

        amountTitle.setText("Antal vare");
        amount.setText("Antal");

        amountWasteTitle.setText("Antal Udløbet");
        amountWaste.setText("Udløbet");

        getOverview();

        return view;
    }

    public void getOverview()
    {

        overviewClient = new OverviewClient();

        overviewClient.getUserOverview(false, (respObject) -> {
           overviewList = (List<Overview>) respObject;
            new Handler(Looper.getMainLooper()).post(() -> {

                for (int i = 0; i <overviewList.size() ; i++) {

                    if(productTypeHashMap.containsKey(overviewList.get(i).getProductType()))
                    {
                        productTypeHashMap.put(overviewList.get(i).getProductType(), productTypeHashMap.get(overviewList.get(i).getProductType()) + 1);
                        if(productTypeHashMap.get(overviewList.get(i).getProductType()) > highestProduct)
                            highestProduct = productTypeHashMap.get(overviewList.get(i).getProductType());

                    }
                    else
                    {
                        productTypeHashMap.put(overviewList.get(i).getProductType(), 1);
                    }
                    if(overviewList.get(i).getExpdate().before(new Date()))
                        expireAmount++;

                }

                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    public void run() {
                        amount.setText(overviewList.size() + "");
                        amountWaste.setText(expireAmount + "");
                        username.setText(HttpClientHelper.user.getUsername() + "");

                    }
                });

                setupBarChart();

            });
        }, (respError) -> {
            System.out.println(respError);
        });

    }

    public void setupBarChart()
    {

        List<BarEntry> entries1 = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        List<BarEntry> entries3 = new ArrayList<>();
        List<BarEntry> entries4 = new ArrayList<>();
        List<BarEntry> entries5 = new ArrayList<>();
        List<BarEntry> entries6 = new ArrayList<>();
        List<BarEntry> entries7 = new ArrayList<>();
        List<BarEntry> entries8 = new ArrayList<>();
        List<BarEntry> entries9 = new ArrayList<>();
        List<BarEntry> entries10 = new ArrayList<>();



        List<IBarDataSet> bars = new ArrayList<IBarDataSet>();

        if(productTypeHashMap.get(ProductType.BEVERAGES) != null)
        {
            entries1.add(new BarEntry(0.5f, productTypeHashMap.get(ProductType.BEVERAGES)));
            BarDataSet dataset1 = new BarDataSet(entries1, "" + ProductType.BEVERAGES);
            dataset1.setColor(Color.RED);
            bars.add(dataset1);
        }
        if(productTypeHashMap.get(ProductType.BAKERY) != null)
        {
            entries2.add(new BarEntry(1f, productTypeHashMap.get(ProductType.BAKERY)));
            BarDataSet dataset2 = new BarDataSet(entries2, "" + ProductType.BAKERY);
            dataset2.setColor(Color.BLUE);
            bars.add(dataset2);
        }
        if(productTypeHashMap.get(ProductType.CANNED) != null)
        {
            BarDataSet dataset3 = new BarDataSet(entries3, "" + ProductType.CANNED);
            dataset3.setColor(Color.GREEN);
            bars.add(dataset3);
            entries3.add(new BarEntry(1.5f, productTypeHashMap.get(ProductType.CANNED)));

        }
        if(productTypeHashMap.get(ProductType.DAIRY) != null)
        {
            entries4.add(new BarEntry(2f, productTypeHashMap.get(ProductType.DAIRY)));
            BarDataSet dataset4 = new BarDataSet(entries4, "" + ProductType.DAIRY);
            dataset4.setColor(Color.GRAY);
            bars.add(dataset4);
        }
        if(productTypeHashMap.get(ProductType.DRY) != null)
        {
            entries5.add(new BarEntry(2.5f, productTypeHashMap.get(ProductType.DRY)));
            BarDataSet dataset5 = new BarDataSet(entries5, "" + ProductType.DRY);
            dataset5.setColor(Color.MAGENTA);
            bars.add(dataset5);
        }
        if(productTypeHashMap.get(ProductType.FROZEN) != null)
        {
            entries6.add(new BarEntry(3f, productTypeHashMap.get(ProductType.FROZEN)));
            BarDataSet dataset6 = new BarDataSet(entries6, "" + ProductType.FROZEN);
            dataset6.setColor(Color.YELLOW);
            bars.add(dataset6);
        }
        if(productTypeHashMap.get(ProductType.MEAT) != null)
        {
            BarDataSet dataset7 = new BarDataSet(entries7, "" + ProductType.MEAT);
            dataset7.setColor(Color.CYAN);
            bars.add(dataset7);
            entries7.add(new BarEntry(3.5f, productTypeHashMap.get(ProductType.MEAT)));

        }
        if(productTypeHashMap.get(ProductType.FRUIT) != null)
        {
            BarDataSet dataset8 = new BarDataSet(entries8, "" + ProductType.FRUIT);
            dataset8.setColor(Color.LTGRAY);
            bars.add(dataset8);
            entries8.add(new BarEntry(4f, productTypeHashMap.get(ProductType.FRUIT)));

        }
        if(productTypeHashMap.get(ProductType.VEGETABLES) != null)
        {
            BarDataSet dataset9 = new BarDataSet(entries9, "" + ProductType.VEGETABLES);
            dataset9.setColor(Color.DKGRAY);
            bars.add(dataset9);
            entries9.add(new BarEntry(4.5f, productTypeHashMap.get(ProductType.VEGETABLES)));

        }
        if(productTypeHashMap.get(ProductType.OTHER) != null)
        {
            entries10.add(new BarEntry(5f, productTypeHashMap.get(ProductType.OTHER)));
            BarDataSet dataset10 = new BarDataSet(entries10, "" + ProductType.OTHER);
            dataset10.setColor(Color.BLACK);
            bars.add(dataset10);

        }

        BarData data = new BarData(bars);

        barChart.setData(data);

        data.setBarWidth(0.5f);

        barChart.setData(data);
        Description description = new Description();
        description.setText("Antal typer vare");
        barChart.setDescription(description);

        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(20);

        XAxis bottomAxis = barChart.getXAxis();
        bottomAxis.setLabelCount(bars.size());

        barChart.getXAxis().setSpaceMax(1);

        barChart.setFitBars(true);
        barChart.setDragEnabled(true);
        barChart.setVisibleXRange(0,overviewList.size());
        YAxis y = barChart.getAxisLeft();
        y.setAxisMaxValue(highestProduct + 5);
        y.setAxisMinValue(0);

        XAxis x = barChart.getXAxis();
        x.setAxisMinValue(0);
        x.setAxisMaxValue(5);


    }

}