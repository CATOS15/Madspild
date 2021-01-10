package madspild.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.madspild.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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
    PieChart pieChart;
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

        pieChart = view.findViewById(R.id.fragment_profile_piechart);
        barChart = view.findViewById(R.id.fragment_profile_barChart);
        amountTitle = view.findViewById(R.id.fragment_profile_amountTitle);
        amount = view.findViewById(R.id.fragment_profile_amount);
        amountWasteTitle = view.findViewById(R.id.fragment_profile_amountWasteTitle);
        amountWaste = view.findViewById(R.id.fragment_profile_amountWaste);

        amountTitle.setText("Antal vare i alt");
        amount.setText("Antal");

        amountWasteTitle.setText("Antal vare Udløbet");
        amountWaste.setText("Udløbet");

//        username.setText(HttpClientHelper.user.getFirstname());

//        getOverview();
//        setupPieChart();
//        setupBarChart();




        //Visual until fixes


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(100f,"Uge 1"));
        entries.add(new PieEntry(10f,"Uge 2"));
        entries.add(new PieEntry(30f,"Uge 3"));

//        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Dummy data");
        pieChart.setDescription(description);
        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(true);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(5);
        pieChart.setRotationEnabled(false);
        //end visual






        return view;
    }

    public void getOverview()
    {

        overviewClient = new OverviewClient();

        overviewClient.getUserOverview(false, (respObject) -> {
           overviewList = (List<Overview>) respObject;

        }, (respError) -> {
            System.out.println(respError);
        });


        //dummy data
        Date date = new Date();
        Overview overview = new Overview();
        overview.setDeleted(false);
        overview.setExpdate(date);
        overview.setGtin("2222222");
        overview.setName("dummy");
        overview.setProductType(ProductType.MEAT);
        overview.setProductId(null);

        overviewList.add(overviewList.size(),overview);
        //end dummy data


        for (int i = 0; i <overviewList.size() ; i++) {

            if(productTypeHashMap.containsKey(overviewList.get(i).getProductType()))
            {
                productTypeHashMap.put(overviewList.get(i).getProductType(), productTypeHashMap.get(overviewList.get(i).getProductType()) + 1);
            }
            else
            {
                productTypeHashMap.put(overviewList.get(i).getProductType(), 1);
            }

        }
    }


    public void setupBarChart()
    {
        List<BarEntry> entries = new ArrayList<>();
        List<BarEntry> entries2 = new ArrayList<>();
        List<BarEntry> entries3 = new ArrayList<>();
        List<BarEntry> entries4 = new ArrayList<>();

        entries.add(new BarEntry(1, productTypeHashMap.get(ProductType.MEAT)));
        entries2.add(new BarEntry(2, productTypeHashMap.get(ProductType.FRUIT)));
        entries3.add(new BarEntry(3, productTypeHashMap.get(ProductType.DAIRY)));
        entries4.add(new BarEntry(4, productTypeHashMap.get(ProductType.OTHER)));



        List<IBarDataSet> bars = new ArrayList<IBarDataSet>();
        BarDataSet dataset = new BarDataSet(entries, "" + ProductType.MEAT);
        dataset.setColor(Color.RED);
        bars.add(dataset);
        BarDataSet dataset2 = new BarDataSet(entries2, "" + ProductType.FRUIT);
        dataset2.setColor(Color.BLUE);
        bars.add(dataset2);
        BarDataSet dataset3 = new BarDataSet(entries3, "" + ProductType.DAIRY);
        dataset3.setColor(Color.GREEN);
        bars.add(dataset3);
        BarDataSet dataset4 = new BarDataSet(entries4, "" + ProductType.OTHER);
        dataset4.setColor(Color.GRAY);
        bars.add(dataset4);

        BarData data = new BarData(bars);





//        BarDataSet dataset = new BarDataSet(entries, "First");
//        BarDataSet dataSet = new BarDataSet(entries, "bars");
//        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);


//        BarData data = new BarData(dataSet);
        barChart.setData(data);
        data.setBarWidth(1);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();
        barChart.getDescription().setEnabled(false);
    }

    public void setupPieChart()
    {
        //https://github.com/PhilJay/MPAndroidChart
        //https://weeklycoding.com/mpandroidchart-documentation/


        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(productTypeHashMap.get(ProductType.MEAT),"" + ProductType.MEAT));
        entries.add(new PieEntry(productTypeHashMap.get(ProductType.FRUIT),"" + ProductType.FRUIT));
        entries.add(new PieEntry(productTypeHashMap.get(ProductType.DAIRY),"" + ProductType.DAIRY));
        entries.add(new PieEntry(productTypeHashMap.get(ProductType.OTHER),"" + ProductType.OTHER));



//        pieChart.setUsePercentValues(true);
        Description description = new Description();
        description.setText("Antal typer vare");
        pieChart.setDescription(description);
        PieDataSet pieDataSet = new PieDataSet(entries,"");
        pieChart.setHoleRadius(30f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setDrawEntryLabels(true);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setSliceSpace(5);
        pieChart.setRotationEnabled(false);

    }

}