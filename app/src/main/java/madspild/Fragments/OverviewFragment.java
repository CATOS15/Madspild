package madspild.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madspild.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import madspild.Controllers.ProductController;
import madspild.Models.Product;

public class OverviewFragment extends Fragment {

    static class LandeOgByerData {
        List<String> Produkter = Arrays.asList("Mælk", "Banan", "Makrald", "Island", "Færøerne", "Finland",
                "Frankrig", "Spanien", "Portugal", "Nepal", "Indien", "Kina", "Japan", "Thailand");

        List<List<String>> Detaljer = Arrays.asList(
                Arrays.asList("H2O", "ARLA", "udløbsdato: 29/49/2085"),
                Arrays.asList("5x", "Calcium", "købt: 1/1/42"),
                Arrays.asList("Fisk", "fiskemanden", "Udløbsdato: når det lugter"),
                Arrays.asList("Reykjavík", "Kópavogur", "Hafnarfjörður", "Dalvík"),
                Arrays.asList("Tórshavn", "Klaksvík", "Fuglafjørður"),
                Arrays.asList("Helsinki", "Espoo", "Tampere", "Vantaa"),
                Arrays.asList("Paris", "Lyon"),
                Arrays.asList("Madrid", "Barcelona", "Sevilla"),
                Arrays.asList("Lissabon", "Porto"),
                Arrays.asList("Kathmandu", "Bhaktapur"),
                Arrays.asList("Mumbai", "Delhi", "Bangalore"),
                Arrays.asList("Shanghai", "Zhengzhou"),
                Arrays.asList("Tokyo", "Osaka", "Hiroshima", "Kawasaki", "Yokohama"),
                Arrays.asList("Bankok", "Sura Thani", "Phuket"));
    }

    LandeOgByerData data = new LandeOgByerData();

    HashSet<Integer> openCountries = new HashSet<>(); // hvilke lande der lige nu er åbne

    RecyclerView recyclerView;
    ListView listviewtest;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String DataMatrixDataPref = "DataMatrixData";

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {

        View view = i.inflate(R.layout.fragment_overview, container, false);

        recyclerView = view.findViewById(R.id.OverviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        //åbneLande = (HashSet<Integer>) savedInstanceState.getSerializable("åbneLande");
        //recyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable("liste"));

        listviewtest = view.findViewById(R.id.listviewtest);
        //showlistview();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        String DataMatrixDataString = sharedPreferences.getString(DataMatrixDataPref, "");
        Toast.makeText(getActivity(),DataMatrixDataString,Toast.LENGTH_SHORT).show();

        return view;
    }


    public void showtextview(){

    }

    RecyclerView.Adapter adapter = new RecyclerView.Adapter<EkspanderbartListeelemViewholder>() {

        @Override
        public int getItemCount()  {
            return data.Produkter.size();
        }

        @Override
        public EkspanderbartListeelemViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout rodLayout = new LinearLayout(parent.getContext());
            rodLayout.setOrientation(LinearLayout.VERTICAL);
            EkspanderbartListeelemViewholder vh = new EkspanderbartListeelemViewholder(rodLayout);
            vh.rodLayout = rodLayout;
            vh.landeview = getLayoutInflater().inflate(R.layout.lekt04_listeelement, parent, false);
            vh.overskrift = vh.landeview.findViewById(R.id.listeelem_overskrift);
            vh.beskrivelse = vh.landeview.findViewById(R.id.listeelem_beskrivelse);
            vh.openCloseImage = vh.landeview.findViewById(R.id.listeelem_billede);
            vh.landeview.setOnClickListener(vh);
            vh.landeview.setBackgroundResource(android.R.drawable.list_selector_background); // giv visuelt feedback når der trykkes på baggrunden
            vh.openCloseImage.setOnClickListener(vh);
            vh.rodLayout.addView(vh.landeview);
            return vh;
        }

        @SuppressLint("SetTextI18n")//fjerner warning beskeder fra når man skal commite
        @Override
        public void onBindViewHolder(EkspanderbartListeelemViewholder vh, int position) {
            boolean open = openCountries.contains(position);
            //vh.overskrift.setText(data.Produkter.get(position) +" åben="+open);
            //vh.beskrivelse.setText("Land nummer " + position + " på vh@"+Integer.toHexString(vh.hashCode()));
            vh.overskrift.setText(data.Produkter.get(position));
            vh.beskrivelse.setText("" + position + "");

            if(position%4 == 0){
                vh.openCloseImage.setImageResource(R.drawable.milk_temp);
            }
            else if(position%3 == 0){
                vh.openCloseImage.setImageResource(R.drawable.freezer_temp);
            }
            else if(position%2 == 0){
                vh.openCloseImage.setImageResource(R.drawable.can_temp);
            }
            else {
                vh.openCloseImage.setImageResource(R.drawable.vegetables_temp);
            }

            if (!open) {
                //vh.openCloseImage.setImageResource(android.R.drawable.ic_input_add); // vis 'åbn' ikon
                for (View underview : vh.underviews) underview.setVisibility(View.GONE); // skjul underelementer
            } else {
                //vh.openCloseImage.setImageResource(android.R.drawable.ic_delete); // vis 'luk' ikon

                List<String> byerILandet = data.Detaljer.get(position);

                while (vh.underviews.size()<byerILandet.size()) { // sørg for at der er nok underviews
                    TextView underView = new TextView(vh.rodLayout.getContext());
                    //underView.setPadding(0, 20, 0, 20);
                    underView.setBackgroundResource(android.R.drawable.list_selector_background);
                    underView.setOnClickListener(vh);      // lad viewholderen håndtere evt klik
                    underView.setId(vh.underviews.size()); // unik ID så vi senere kan se hvilket af underviewne der klikkes på
                    vh.rodLayout.addView(underView);
                    vh.underviews.add(underView);
                }

                for (int i=0; i<vh.underviews.size(); i++) { // sæt underviews til at vise det rigtige indhold
                    TextView underView = vh.underviews.get(i);
                    if (i<byerILandet.size()) {
                        underView.setText(byerILandet.get(i));
                        underView.setVisibility(View.VISIBLE);
                    } else {
                        underView.setVisibility(View.GONE);      // for underviewet skal ikke bruges
                    }
                }

            }
        }
    };


    class EkspanderbartListeelemViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout rodLayout;
        TextView overskrift;
        TextView beskrivelse;
        ImageView openCloseImage;
        View landeview;
        ArrayList<TextView> underviews = new ArrayList<>();

        public EkspanderbartListeelemViewholder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            String DataMatrixDataString = sharedPreferences.getString(DataMatrixDataPref, "");
            Toast.makeText(getActivity(),DataMatrixDataString,Toast.LENGTH_SHORT).show();

            if (v == openCloseImage || v==landeview) { // Klik på billede åbner/lukker for listen af byer i dette land
                boolean Open = openCountries.contains(position);
                if (Open) openCountries.remove(position); // luk
                else openCountries.add(position); // åbn
                adapter.notifyItemChanged(position);
            } else {
                int id = v.getId();
                Toast.makeText(v.getContext(), "Klik på by nummer " + id + " i "+data.Produkter.get(position), Toast.LENGTH_SHORT).show();
            }
        }
    }




}