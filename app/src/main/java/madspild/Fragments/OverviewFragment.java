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
import java.util.List;
import java.util.UUID;

import madspild.Adapters.OverviewListAdapter;
import madspild.HttpClient.OverviewClient;
import madspild.HttpClient.ProductClient;
import madspild.Models.Overview;

public class OverviewFragment extends Fragment {
    GridView overviewGrid;
    Button overviewButtonDelete;
    View view;

    OverviewListAdapter overViewListAdapter;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        view = i.inflate(R.layout.fragment_overview, container, false);

        OverviewClient overviewClient = new OverviewClient();
        overviewClient.getUserOverview(false, (respObject) -> {
            List<Overview> overviewList = (List<Overview>) respObject;
            // Sort the overview by date
            Collections.sort(overviewList);

            new Handler(Looper.getMainLooper()).post(() -> {
                // OverViewListApdater
                overViewListAdapter = new OverviewListAdapter(getActivity(),R.layout.fragment_overview_listitem, overviewList);

                // Insert in grid
                overviewGrid = view.findViewById(R.id.overviewGrid);
                overviewGrid.setAdapter(overViewListAdapter);
                overviewGrid.setNumColumns(1);

                overviewGrid.setOnItemClickListener((parent, view, position, id) -> {
                    overviewList.get(position).setMarked(!(overviewList.get(position).getMarked()));
                    overViewListAdapter.notifyDataSetChanged();
                });

                overviewButtonDelete = view.findViewById(R.id.overview_button_delete);
                overviewButtonDelete.setOnClickListener(v -> deleteProductsFromInventory(overviewList));
            });
        }, (respError) -> {
            System.out.println(respError);
        });

        return view;
    }


    public void deleteProductsFromInventory(List<Overview> overviewList){
        List<UUID> ids = new ArrayList<>();
        for(int i=0;i<overviewList.size();i++){
            if(overviewList.get(i).getMarked()){
                overviewList.get(i).setDeleted(true);
                ids.add(overviewList.get(i).getProductId());
            }
        }

        ProductClient productClient = new ProductClient();
        productClient.deleteProducts(ids, (respObject) -> {
            for (Overview overview: overviewList) {
                if(ids.contains(overview.getProductId())){
                    overviewList.remove(overview);
                }
            }
            new Handler(Looper.getMainLooper()).post(() -> {
                overViewListAdapter.notifyDataSetChanged();
            });
        }, (respError) -> {
            System.out.println(respError);
        });
    }

}