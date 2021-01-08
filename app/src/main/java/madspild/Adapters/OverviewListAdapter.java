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

import com.example.madspild.R;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import madspild.Models.Overview;

public class OverviewListAdapter extends ArrayAdapter {

    List<Overview> overviewList;

    public OverviewListAdapter(Context context, int textViewResourceId, List<Overview> overviewList) {
        super(context, textViewResourceId, overviewList);
        this.overviewList = overviewList;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get Inflate the given views
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.fragment_overview_listitem, parent);

        if (overviewList.get(position).getDeleted()) {
            v.setVisibility(View.GONE);
            v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 0));
        } else {
            // Get the view elements on the inflated view
            ImageView listitem_image_border = v.findViewById(R.id.listitem_image_border);
            ImageView listitem_image_clock = v.findViewById(R.id.listitem_image_clock);
            ImageView listitem_image_foodcategory = v.findViewById(R.id.listitem_image_foodcategory);

            TextView listitem_text_productname = v.findViewById(R.id.listitem_text_productname);
            TextView listitem_text_expiredate = v.findViewById(R.id.listitem_text_expiredate);
            TextView listitem_text_remainingdays = v.findViewById(R.id.listitem_text_remainingdays);

            // Set row height
            v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 250));


            // Get the day difference + get corresponding color
            int dayDiff = getDifferenceDays(overviewList.get(position).getExpdate());
            String remainingdays = dayDiff + " dag(e)";
            listitem_text_remainingdays.setText(remainingdays);

            if (dayDiff < 2) {
                listitem_text_remainingdays.setTextColor(Color.rgb(194, 100, 50));
                listitem_image_clock.setColorFilter(Color.rgb(194, 100, 50));
            } else if (dayDiff < 4) {
                listitem_text_remainingdays.setTextColor(Color.rgb(189, 194, 50));
                listitem_image_clock.setColorFilter(Color.rgb(189, 194, 50));
            } else {
                listitem_text_remainingdays.setTextColor(Color.rgb(44, 156, 6));
                listitem_image_clock.setColorFilter(Color.rgb(44, 156, 6));
            }

            final Handler handler = new Handler();

            // Set food category
            // Set Text fields
            listitem_text_productname.setText(overviewList.get(position).getName());
            listitem_text_expiredate.setText(dateToString(overviewList.get(position).getExpdate()));
        }
        return v;
    }

    private String dateToString(Date date){
        int day = date.getDate();
        int month = date.getMonth()+1;
        int year = date.getYear() + 1900;
        return day +"/"+ month +"/"+ year;
    }

    private int getDifferenceDays(Date expdate) {
        Date today = new Date();
        long diff = expdate.getTime() - today.getTime();
        return (int) Math.ceil(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
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


