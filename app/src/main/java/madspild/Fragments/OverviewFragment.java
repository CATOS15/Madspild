package madspild.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.madspild.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import madspild.Adapters.OverviewListAdapter;
import madspild.HttpClient.OverviewClient;
import madspild.HttpClient.ProductClient;
import madspild.Models.Overview;
import madspild.Models.OverviewSorting;

public class OverviewFragment extends Fragment {
    GridView overviewGrid;
    View view;

    OverviewClient overviewClient;
    OverviewListAdapter overviewListAdapter;
    OverviewSorting overviewSorting = OverviewSorting.BYDATE;
    MaterialAlertDialogBuilder dialog;
    MaterialToolbar topbarView;
    List<Overview> overviewList;
    Snackbar overview_snackbar;

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
        view = i.inflate(R.layout.fragment_overview, container, false);

        dialog = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
        topbarView = view.findViewById(R.id.fragment_overview_topbar_view);

        overviewClient = new OverviewClient();
        overviewClient.getUserOverview(false, (respObject) -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                overviewList = (List<Overview>) respObject;
                sortOverviewList(overviewList);
                initOverviewListAdapter(overviewList);
            });

        }, (respError) -> {
            System.out.println(respError);
        });

        // Topbar button handler handler
        topbarView.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.fragment_overview_topbar_view_menu_sortby_date:
                    overviewSorting = OverviewSorting.BYDATE;
                    sortOverviewList(overviewList);
                    return true;
                case R.id.fragment_overview_topbar_view_menu_sortby_alphabet:
                    overviewSorting = OverviewSorting.BYNAME;
                    sortOverviewList(overviewList);
                    return true;
                default:
                    return false;
            }
        });

        return view;
    }

    // Action mode
    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menuInflater.inflate(R.menu.fragment_overview_topbar_delete,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }


        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.fragment_overview_topbar_icon_delete){
                deleteProductsFromInventory(overviewListAdapter.getAllItems());
                return true;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            overviewListAdapter.resetListColor();
            overviewListAdapter.resetTopbar();
        }
    };

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
                overviewListAdapter.resetTopbar();
                overview_snackbar = Snackbar.make(view,respObject.toString(), 3000);
                overview_snackbar.show();
            });
        }, (respError) -> {
            System.out.println(respError);
            overviewListAdapter.resetTopbar();
            overview_snackbar = Snackbar.make(view,respError.toString(), 3000);
            overview_snackbar.show();
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


    @Override
    public void onResume() {
        super.onResume();
        overviewClient.getUserOverview(false, (respObject) -> {
            overviewList = (List<Overview>) respObject;
            sortOverviewList(overviewList);
            initOverviewListAdapter(overviewList);
            super.onPause();
        }, (respError) -> {
            new Handler(Looper.getMainLooper()).post(() -> {
                errorMessageDialog("Fejl",respError);
            });
        });
    }

    @Override
    public void onPause() {
        if(overview_snackbar != null){
            overview_snackbar.dismiss();
        }

        if(overviewListAdapter != null){
            overviewListAdapter.resetTopbar();
        }
        super.onPause();
    }

    public void errorMessageDialog(String title, String message){
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setOnDismissListener(dialogInterface -> {
            onResume();
        });
        dialog.show();
    }

    private void initOverviewListAdapter(List<Overview> overviewList){
        new Handler(Looper.getMainLooper()).post(() -> {
            // OverViewListApdater
            overviewListAdapter = new OverviewListAdapter(getActivity(),R.layout.fragment_overview_listitem, overviewList);

            overviewListAdapter.initTopbar(mActionModeCallback, topbarView);

            // Insert in grid
            overviewGrid = view.findViewById(R.id.overviewGrid);
            overviewGrid.setAdapter(overviewListAdapter);
            overviewGrid.setNumColumns(1);
        });
    }
}