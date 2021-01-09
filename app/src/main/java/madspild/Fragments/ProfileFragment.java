package madspild.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.madspild.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Objects;

import madspild.Activities.EditProfileActivity;
import madspild.Activities.StartActivity;
import madspild.Helpers.HttpClientHelper;


public class ProfileFragment extends Fragment {
    PieChart pieChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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

        pieChart = view.findViewById(R.id.fragment_profile_piechart);
        setupPieChart();

        return view;
    }

    public void setupPieChart()
    {
        //https://github.com/PhilJay/MPAndroidChart
        //https://weeklycoding.com/mpandroidchart-documentation/
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


        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

//                int pos1 = e.toString().indexOf("(sum): ");
//                String text = e.toString().substring(pos1 +7);
//
//
//                String label = entries.get(pos1).getLabel();
//                float value = entries.get(pos1).getValue();
//
//                Toast.makeText(getActivity(), pos1 + " ", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

}