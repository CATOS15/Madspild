package madspild.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.madspild.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import madspild.Adapters.OverviewListAdapter;
import madspild.HttpClient.OverviewClient;
import madspild.HttpClient.ProductClient;
import madspild.Models.Overview;
import madspild.Models.OverviewSorting;

public class OverviewFragment extends Fragment {
    GridView overviewGrid;
    Button overviewButtonDelete, overviewButtonSort;
    View view;

    OverviewClient overviewClient;
    OverviewListAdapter overviewListAdapter;
    OverviewSorting overviewSorting = OverviewSorting.BYDATE;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        view = i.inflate(R.layout.fragment_overview, container, false);

        overviewClient = new OverviewClient();
        overviewClient.getUserOverview(false, (respObject) -> {
            List<Overview> overviewList = (List<Overview>) respObject;
            sortOverviewList(overviewList);
            initOverviewListAdapter(overviewList);
        }, (respError) -> {
            System.out.println(respError);
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        overviewClient.getUserOverview(false, (respObject) -> {
            List<Overview> overviewList = (List<Overview>) respObject;
            sortOverviewList(overviewList);
            initOverviewListAdapter(overviewList);
        }, (respError) -> {
            System.out.println(respError);
        });
    }

    private void initOverviewListAdapter(List<Overview> overviewList){
        new Handler(Looper.getMainLooper()).post(() -> {
            // OverViewListApdater
            overviewListAdapter = new OverviewListAdapter(getActivity(),R.layout.fragment_overview_listitem, overviewList);

            // Insert in grid
            overviewGrid = view.findViewById(R.id.overviewGrid);
            overviewGrid.setAdapter(overviewListAdapter);
            overviewGrid.setNumColumns(1);

            overviewButtonDelete = view.findViewById(R.id.fragment_overview_topbar_button_delete);
            overviewButtonDelete.setOnClickListener(v -> deleteProductsFromInventory(overviewList));

            overviewButtonSort = view.findViewById(R.id.fragment_overview_topbar_button_sort);
            overviewButtonSort.setOnClickListener((view) -> {
                overviewSorting = (overviewSorting == OverviewSorting.BYDATE ? OverviewSorting.BYNAME : OverviewSorting.BYDATE);
                sortOverviewList(overviewList);
            });
        });
    }


    private void sortOverviewList(List<Overview> overviewList){
        if(overviewSorting == OverviewSorting.BYDATE) {
            Comparator<Overview> compareByDate = (Overview o1, Overview o2) -> o1.getExpdate().compareTo(o2.getExpdate());
            Collections.sort(overviewList, compareByDate);
        }else if(overviewSorting == OverviewSorting.BYNAME){
            Comparator<Overview> compareByName = (Overview o1, Overview o2) -> o1.getName().compareTo(o2.getName());
            Collections.sort(overviewList, compareByName);
        }
        new Handler(Looper.getMainLooper()).post(() -> {
            if (overviewListAdapter != null) overviewListAdapter.notifyDataSetChanged();
        });
    }

    private void deleteProductsFromInventory(List<Overview> overviewList){
        List<UUID> ids = new ArrayList<>();
        for(int i=0;i<overviewList.size();i++){
            if(overviewList.get(i).isMarked()){
                overviewList.get(i).setDeleted(true);
                ids.add(overviewList.get(i).getProductId());
            }
        }
        ProductClient productClient = new ProductClient();
        productClient.deleteProducts(ids, (respObject) -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                List<Overview> overviewsToRemove = new ArrayList<>();
                for (Overview overview: overviewList) {
                    if(ids.contains(overview.getProductId())){
                        overviewsToRemove.add(overview);
                    }
                }
                overviewList.removeAll(overviewsToRemove);
                for (Overview overviewToRemove: overviewsToRemove) {
                    overviewListAdapter.remove(overviewToRemove);
                }
            });
        }, (respError) -> {
            System.out.println(respError);
        });
    }

}