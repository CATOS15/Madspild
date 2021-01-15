package madspild.Adapters;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.madspild.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import madspild.Models.Overview;

public class OverviewListAdapter extends ArrayAdapter<Overview> {

    List<Overview> overviewList;
    ActionMode actionMode = null;
    ActionMode.Callback mActionModeCallback;
    View topbarView;
    int itemsSelectedCounter = 0;

    public OverviewListAdapter(Context context, int textViewResourceId, List<Overview> overviewList) {
        super(context, textViewResourceId, overviewList);
        this.overviewList = overviewList;
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
        View fragment_overview_view_divider = overviewView.findViewById(R.id.fragment_overview_view_divider);


        long diffInMillies = overview.getExpdate().getTime() - new Date().getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        String daysleftString = (diffInDays == 1 ? "1 dag" : diffInDays + " dage");
        daysleftText.setText(daysleftString);

        if(diffInDays < 0){
            daysleftImage.setColorFilter(getContext().getResources().getColor(R.color.newRed));
        }else if(diffInDays < 4){
            daysleftImage.setColorFilter(getContext().getResources().getColor(R.color.newYellow));
        }else{
            daysleftImage.setColorFilter(getContext().getResources().getColor(R.color.newGreen));
        }

        // Set text and image
        informationProductName.setText(overview.getName());
        String informationExpDateString = "Udløbsdato: " + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(overview.getExpdate());
        informationExpDate.setText(informationExpDateString);
        productTypeImage.setImageResource(getProductIcon(position));

        // Set divider
        if(overviewList.size()-1 != position){
            fragment_overview_view_divider.setVisibility(View.VISIBLE);
        }

        // Set init colors
        selectColorChange(listitem,overview.isMarked());

        overviewView.setOnClickListener((view) -> {
            boolean isMarked = overview.isMarked();
            overview.setMarked(!isMarked);
            selectColorChange(listitem,overview.isMarked());
            if(overview.isMarked()) itemsSelectedCounter++; else itemsSelectedCounter--;
            if(itemsSelectedCounter > 0){
                if(actionMode == null) actionMode = topbarView.startActionMode(mActionModeCallback);
                String selectTitle = itemsSelectedCounter + " valgt";
                actionMode.setTitle(selectTitle);
            }else{
                actionMode.finish();
                actionMode = null;
            }
            notifyDataSetChanged();
        });

        return overviewView;
    }

    public void initTopbar(ActionMode.Callback mActionModeCallback, View topbarView){
        this.topbarView = topbarView;
        this.mActionModeCallback = mActionModeCallback;
    }
    public void resetTopbar(){
        actionMode.finish();
        actionMode = null;
        this.itemsSelectedCounter = 0;
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

    public void selectColorChange(ConstraintLayout view, boolean isMarked){
        ValueAnimator colorAnimation;
        int colorWhite = getContext().getResources().getColor(R.color.white);
        int colorBlue = getContext().getResources().getColor(R.color.blueSelect);
        ColorDrawable currentColor = (ColorDrawable) view.getBackground();

        if(!isMarked && currentColor.getColor() != colorWhite){
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorBlue, colorWhite);
        } else if (isMarked && currentColor.getColor() != colorBlue){
            colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorWhite, colorBlue);
        } else{
            return;
        }

        colorAnimation.setDuration(100); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }


    public List<Overview> getAllItems() {
        ArrayList<Overview> result = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            result.add(getItem(i));
        }
        return result;
    }

    public void resetListColor(){
        for (int i = 0; i < overviewList.size(); i++) {
            if(overviewList.get(i).isMarked()){
                overviewList.get(i).setMarked(false);
            }
        }
        notifyDataSetChanged();
    }
}

