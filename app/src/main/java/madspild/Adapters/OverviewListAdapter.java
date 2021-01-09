package madspild.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.madspild.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import madspild.Models.Overview;

public class OverviewListAdapter extends ArrayAdapter<Overview> {

    List<Overview> overviewList;

    public OverviewListAdapter(Context context, int textViewResourceId, List<Overview> overviewList) {
        super(context, textViewResourceId, overviewList);
        this.overviewList = overviewList;
    }

    private void setVisibilityDeleteIcon(ViewGroup parent){
        boolean visible = false;
        for (Overview overview : overviewList) {
            if(overview.isMarked()){
                visible = overview.isMarked();
                break;
            }
        }
        MaterialButton overviewButtonSort = ((ConstraintLayout) parent.getParent()).findViewById(R.id.fragment_overview_topbar_button_sort);
        overviewButtonSort.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);

        MaterialButton overviewButtonDelete = ((ConstraintLayout) parent.getParent()).findViewById(R.id.fragment_overview_topbar_button_delete);
        overviewButtonDelete.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @NonNull
    @Override
    public View getView(int position, View overviewView, @NonNull ViewGroup parent) {
        Overview overview = getItem(position);

        if (overviewView == null) {
            overviewView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_overview_listitem, parent, false);
        }
        if(overview == null) return overviewView;

        ConstraintLayout listitem = overviewView.findViewById(R.id.fragment_overview_listitem);
        ImageView daysleftImage = overviewView.findViewById(R.id.fragment_overview_listitem_daysleft_image);
        TextView daysleftText = overviewView.findViewById(R.id.fragment_overview_listitem_daysleft_text);
        TextView informationProductName = overviewView.findViewById(R.id.fragment_overview_listitem_information_productname);
        TextView informationExpDate = overviewView.findViewById(R.id.fragment_overview_listitem_information_expdate);
        ImageView productTypeImage = overviewView.findViewById(R.id.fragment_overview_listitem_producttype_image);

        listitem.setBackgroundResource(R.drawable.fragment_overview_listitem_default);
        setVisibilityDeleteIcon(parent);

        long diffInMillies = overview.getExpdate().getTime() - new Date().getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        String daysleftString = (diffInDays == 1 ? "1 dag" : diffInDays + " dage");
        daysleftText.setText(daysleftString);

        if(diffInDays < 0){
            daysleftImage.setColorFilter(Color.RED);
        }else if(diffInDays < 4){
            daysleftImage.setColorFilter(Color.YELLOW);
        }else{
            daysleftImage.setColorFilter(Color.GREEN);
        }

        informationProductName.setText(overview.getName());

        String informationExpDateString = "Udløbsdato: " + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(overview.getExpdate());
        informationExpDate.setText(informationExpDateString);

        productTypeImage.setImageResource(getProductIcon(position));

        overviewView.setOnClickListener((view) -> {
            overview.setMarked(!overview.isMarked());
            if(overview.isMarked()){
                listitem.setBackgroundResource(R.drawable.fragment_overview_listitem_marked);
            }else{
                listitem.setBackgroundResource(R.drawable.fragment_overview_listitem_default);
            }
            setVisibilityDeleteIcon(parent);
        });

        return overviewView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private int getProductIcon(int index){
        switch (overviewList.get(index).getProductType()) {
            case FRUIT:
                return R.drawable.fragment_overview_listitem_producttype_image_fruit;
            case DAIRY:
                return R.drawable.fragment_overview_listitem_producttype_image_dairy;
            case BAKERY:
                return R.drawable.fragment_overview_listitem_producttype_image_bakery;
            case BEVERAGES:
                return R.drawable.fragment_overview_listitem_producttype_image_beverages;
            case CANNED:
                return R.drawable.fragment_overview_listitem_producttype_image_canned;
            case DRY:
                return R.drawable.fragment_overview_listitem_producttype_image_dry;
            case FROZEN:
                return R.drawable.fragment_overview_listitem_producttype_image_frozen;
            case VEGETABLES:
                return R.drawable.fragment_overview_listitem_producttype_image_vegetables;
            case MEAT:
                return R.drawable.fragment_overview_listitem_producttype_image_meat;
            case OTHER:
                return R.drawable.fragment_overview_listitem_producttype_image_other;
            default:
                return R.drawable.fragment_overview_listitem_producttype_image_other;
        }
    }
}


