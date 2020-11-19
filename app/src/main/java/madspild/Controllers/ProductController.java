package madspild.Controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import madspild.Models.Product;

public class ProductController {
    public void setProducts(Context context, List<Product> products){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);//aktivitet

        Gson gson = new Gson();
        String productJson = gson.toJson(products);

        prefs.edit().putString("productJson", productJson).apply();
    }

    public ArrayList<Product> getProducts(Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);//aktivitet
        Gson gson = new Gson();
        ArrayList<Product> entries = gson.fromJson(prefs.getString("productJson","[]"),new TypeToken<ArrayList<Product>>(){}.getType());
        return entries;
    }

    public void addProduct(Context context, String DataMatrixData){
        Product myproduct = new Product(DataMatrixData);
        ArrayList<Product> currentproducts = getProducts(context);
        currentproducts.add(myproduct);
        //currentproducts.sort(Comparator.comparingInt(Product::getDataMatrixData).reversed());
        setProducts(context, currentproducts);
    }
}
