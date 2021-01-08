package madspild.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.madspild.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import madspild.Models.Overview;

public class OverviewListAdapter extends ArrayAdapter<Overview> {

    List<Overview> overviewList;

    public OverviewListAdapter(Context context, int textViewResourceId, List<Overview> overviewList) {
        super(context, textViewResourceId, overviewList);
        this.overviewList = overviewList;
    }

    @NonNull
    @Override
    public View getView(int position, View overviewListView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Overview overview = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (overviewListView == null) {
            overviewListView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_overview_listitem, parent, false);
        }
        // Lookup view for data population
        //TextView tvName = (TextView) overviewListView.findViewById(R.id.tvName);
        //TextView tvHome = (TextView) overviewListView.findViewById(R.id.tvHome);
        // Populate the data into the template view using the data object
        //tvName.setText(user.name);
        //tvHome.setText(user.hometown);
        // Return the completed view to render on screen
        return overviewListView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private int getProductIcon(int index){
        switch (overviewList.get(index).getProductType()) {
            case FRUIT:
                return R.drawable.freezer_temp;
            case DAIRY:
                return R.drawable.freezer_temp;
            case BAKERY:
                return R.drawable.vegetables_temp;
            case BEVERAGES:
                return R.drawable.vegetables_temp;
            case CANNED:
                return R.drawable.freezer_temp;
            case DRY:
                return R.drawable.freezer_temp;
            case FROZEN:
                return R.drawable.freezer_temp;
            case VEGETABLES:
                return R.drawable.freezer_temp;
            case MEAT:
                return R.drawable.freezer_temp;
            case OTHER:
                return R.drawable.freezer_temp;
            default:
                return R.drawable.freezer_temp;
        }
    }

}


