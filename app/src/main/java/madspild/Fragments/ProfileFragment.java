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
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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
    TextView statetxtgraph;


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

        barChart = view.findViewById(R.id.fragment_profile_barChart);
        amountTitle = view.findViewById(R.id.fragment_profile_amountTitle);
        amount = view.findViewById(R.id.fragment_profile_amount);
        amountWasteTitle = view.findViewById(R.id.fragment_profile_amountWasteTitle);
        amountWaste = view.findViewById(R.id.fragment_profile_amountWaste);
        username = view.findViewById(R.id.fragment_profile_username);
        statetxtgraph = view.findViewById(R.id.fragment_profile_statetxtgraph);


        amountTitle.setText("Totalt antal produkter");
        amount.setText("Antal");

        amountWasteTitle.setText("Produkter udløbet");
        amountWaste.setText("Udløbet");

        getOverview();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        getOverview();
    }

    public void getOverview()
    {
        overviewList = new ArrayList<>();
        overviewClient = new OverviewClient();
        productTypeHashMap = new HashMap<>();
        expireAmount = 0;
        highestProduct = 0;

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
        barChart.invalidate();
        barChart.refreshDrawableState();

    }

    public void setupBarChart()
    {

        //data for values
        ArrayList<BarEntry> valueSet = new ArrayList<>();
        if(productTypeHashMap.get(ProductType.BEVERAGES) != null)
        {
            BarEntry entry = new BarEntry(0f, productTypeHashMap.get(ProductType.BEVERAGES));
            valueSet.add(entry);
        }
        if(productTypeHashMap.get(ProductType.BAKERY) != null)
        {
            BarEntry entry = (new BarEntry(1f, productTypeHashMap.get(ProductType.BAKERY)));
            valueSet.add(entry);

        }
        if(productTypeHashMap.get(ProductType.CANNED) != null)
        {
            BarEntry entry = (new BarEntry(2f, productTypeHashMap.get(ProductType.CANNED)));
            valueSet.add(entry);

        }
        if(productTypeHashMap.get(ProductType.DAIRY) != null)
        {
            BarEntry entry = (new BarEntry(3f, productTypeHashMap.get(ProductType.DAIRY)));
            valueSet.add(entry);

        }
        if(productTypeHashMap.get(ProductType.DRY) != null)
        {
            BarEntry entry = (new BarEntry(4f, productTypeHashMap.get(ProductType.DRY)));
            valueSet.add(entry);

        }
        if(productTypeHashMap.get(ProductType.FROZEN) != null)
        {
            BarEntry entry = (new BarEntry(5f, productTypeHashMap.get(ProductType.FROZEN)));
            valueSet.add(entry);
        }
        if(productTypeHashMap.get(ProductType.MEAT) != null)
        {
            BarEntry entry = (new BarEntry(6f, productTypeHashMap.get(ProductType.MEAT)));
            valueSet.add(entry);
        }
        if(productTypeHashMap.get(ProductType.FRUIT) != null)
        {
            BarEntry entry = (new BarEntry(7f, productTypeHashMap.get(ProductType.FRUIT)));
            valueSet.add(entry);
        }
        if(productTypeHashMap.get(ProductType.VEGETABLES) != null)
        {
            BarEntry entry = (new BarEntry(8f, productTypeHashMap.get(ProductType.VEGETABLES)));
            valueSet.add(entry);
        }
        if(productTypeHashMap.get(ProductType.OTHER) != null)
        {
            BarEntry entry = (new BarEntry(9f, productTypeHashMap.get(ProductType.OTHER)));
            valueSet.add(entry);
        }


        //data for label name
        ArrayList<String> products = new ArrayList<>();
        products.add(""+ProductType.BEVERAGES);
        products.add(""+ProductType.BAKERY);
        products.add(""+ProductType.CANNED);
        products.add(""+ProductType.DAIRY);
        products.add(""+ProductType.DRY);
        products.add(""+ProductType.FROZEN);
        products.add(""+ProductType.MEAT);
        products.add(""+ProductType.FRUIT);
        products.add(""+ProductType.VEGETABLES);
        products.add(""+ProductType.OTHER);

        BarData data = new BarData();
        BarDataSet bds = new BarDataSet(valueSet, " ");
        String[] xAxisLabels = products.toArray(new String[0]);
        bds.setStackLabels(xAxisLabels);
        data.addDataSet(bds);
        data.setDrawValues(true);
        data.setBarWidth(0.4f);

        XAxis xaxis = barChart.getXAxis();

        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1f);
        xaxis.setDrawLabels(true);
        xaxis.setDrawAxisLine(false);

        xaxis.setValueFormatter(new IndexAxisValueFormatter(products));
        xaxis.setLabelCount(products.size());



        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0);



        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);

        yAxisRight.setDrawLabels(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);


        barChart.setDrawValueAboveBar(true);
        barChart.setFitBars(true);
        barChart.setData(data);
        Description description = new Description();
        description.setText("Chart af totalt antal");
        barChart.setDescription(description);
        barChart.setTouchEnabled(false);
        barChart.invalidate();
        barChart.refreshDrawableState();


    }

}