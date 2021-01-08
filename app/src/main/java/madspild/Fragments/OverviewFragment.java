package madspild.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    Button overview_button_delete;
    View view;

    OverviewListAdapter overViewListAdapter;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        view = i.inflate(R.layout.fragment_overview, container, false);

        OverviewClient overviewClient = new OverviewClient();
        overviewClient.getUserOverview(null, (respObject) -> {
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

                overviewGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        overviewList.get(position).setMarked(!(overviewList.get(position).getMarked()));
                        overViewListAdapter.notifyDataSetChanged();
                    }
                });

                overview_button_delete = view.findViewById(R.id.overview_button_delete);
                overview_button_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        deleteProductsFromInventory(overviewList);
                    }
                });
            });
        }, (respError) -> {
            System.out.println("Blah blah fejl " + respError);
        });

        return view;
    }


    public void deleteProductsFromInventory(List<Overview> overviewList){
        // Set marked product to deleted
        List<UUID> ids = new ArrayList<>();
        for(int i=0;i<overviewList.size();i++){
            if(overviewList.get(i).getMarked()){
                overviewList.get(i).setDeleted(true);
                ids.add(overviewList.get(i).getProductId());
            }
        }
        // Opdater frontend
        overViewListAdapter.notifyDataSetChanged();

        ProductClient productClient = new ProductClient();
        productClient.deleteProducts(ids, (respObject) -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                    //Toast toast = Toast.makeText(view.getContext(), "Produktet slettet", Toast.LENGTH_LONG);
                //toast.show();
            });

        }, (respError) -> {
            //Toast toast = Toast.makeText(view.getContext(), "Produktet blev ikke slettet", Toast.LENGTH_LONG);
            //toast.show();
        });
    }

}