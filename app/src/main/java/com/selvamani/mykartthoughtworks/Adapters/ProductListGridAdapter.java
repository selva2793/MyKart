package com.selvamani.mykartthoughtworks.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.selvamani.mykartthoughtworks.Model.ProductsModel;
import com.selvamani.mykartthoughtworks.R;

import java.util.List;

public class ProductListGridAdapter extends BaseAdapter {
    private Context mContext;
    List<ProductsModel> mproductsModelsLists;

    // Constructor
    public ProductListGridAdapter(Context c, List<ProductsModel> productsModelsLists) {
        mContext = c;
        mproductsModelsLists = productsModelsLists;
    }

    public int getCount() {
        return mproductsModelsLists.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(mproductsModelsLists.get(position).getProdNmae());

            if(mproductsModelsLists.get(position).getProdNmae().toLowerCase().contains("microwave")){
                imageView.setImageResource(R.drawable.oven);
            }
            else if(mproductsModelsLists.get(position).getProdNmae().toLowerCase().contains("television")){
                imageView.setImageResource(R.drawable.television);
            }
            else if(mproductsModelsLists.get(position).getProdNmae().toLowerCase().contains("vacuum")){
                imageView.setImageResource(R.drawable.vacuumcleaner);
            }
            else if(mproductsModelsLists.get(position).getProdNmae().toLowerCase().contains("table")){
                imageView.setImageResource(R.drawable.table);
            }
            else if(mproductsModelsLists.get(position).getProdNmae().toLowerCase().contains("chair")){
                imageView.setImageResource(R.drawable.chairs);
            }
            else if(mproductsModelsLists.get(position).getProdNmae().toLowerCase().contains("almirah")){
                imageView.setImageResource(R.drawable.almirah);
            }

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}