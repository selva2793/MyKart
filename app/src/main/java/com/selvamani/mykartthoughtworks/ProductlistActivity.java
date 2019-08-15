package com.selvamani.mykartthoughtworks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.selvamani.mykartthoughtworks.Adapters.ProductListGridAdapter;
import com.selvamani.mykartthoughtworks.LocalDatabase.LocalDBConnection;
import com.selvamani.mykartthoughtworks.Model.ProductsModel;

import java.util.ArrayList;
import java.util.List;

public class ProductlistActivity extends AppCompatActivity {

    GridView gridview;
    Spinner categorySpinner;
    TextView category;

    List<ProductsModel> productsModelsList;
    List<String> categories;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist2);
        gridview = (GridView) findViewById(R.id.gridview);
        categorySpinner = (Spinner) findViewById(R.id.sp_category);
        category = (TextView) findViewById(R.id.tv_category);

        userId =  getIntent().getStringExtra("userid");

        ProductTabledataProductTabledataCallAsyncTask();
    }

    private void ProductTabledataProductTabledataCallAsyncTask() {

        class ProductTable extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    productsModelsList = new ArrayList<>();
                    productsModelsList = LocalDBConnection.getInstance(getApplicationContext()).
                            LoadUserdata().loadProducts(); //chcek id exists or not

                } catch (Exception ex) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                loadGridAdapter(productsModelsList);

                categories = new ArrayList<String>();
                categories.add("All");
                for (ProductsModel productsModel : productsModelsList){
                    categories.add(productsModel.getProdCategory());
                }

                for (int i = 0; i < categories.size(); i++) {
                    for (int j = 1 + i; j < categories.size(); j++)
                        if (categories.get(j).equals(categories.get(i))) {
                            categories.remove(categories.get(j)); // this would remove the current element only if the previous element is same as the current element
                        }
                    else{}
                }

                for (int i = 0; i < categories.size(); i++) {
                    for (int j = 1 + i; j < categories.size(); j++)
                        if (categories.get(j).equals(categories.get(i))) {
                            categories.remove(categories.get(j)); // this would remove the current element only if the previous element is same as the current element
                        }
                        else{}
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProductlistActivity.this, android.R.layout.simple_spinner_dropdown_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (position == 0){
                            loadGridAdapter(productsModelsList);
                        }
                        else{
                           List<ProductsModel> mproductsModelsList = new ArrayList<>();
                            String categoryyy = categories.get(position);
                            category.setText(categoryyy);
                            Log.d("dsdaa", "onItemSelected: "+categories);
                            for (ProductsModel productsModel : productsModelsList){
                                if (productsModel.getProdCategory().equalsIgnoreCase(categoryyy)) {
                                    mproductsModelsList.add(productsModel);
                                }
                            }


                            loadGridAdapter(mproductsModelsList);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


                category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        categorySpinner.performClick();
                    }
                });

            }

        }
        ProductTable saveUser = new ProductTable();
        saveUser.execute();
    }

    public void loadGridAdapter(List<ProductsModel> productsModelsLists){


        ProductListGridAdapter productListGridAdapter = new ProductListGridAdapter(this, productsModelsLists);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(productListGridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ProductsModel productsModel = new ProductsModel();
                productsModel = productsModelsLists.get(position);
                gotoDetailPage(productsModel);
            }
        });
    }

    private void gotoDetailPage(ProductsModel productsModel){
        Intent i = new Intent(this, ProductDetailActivity.class);
        i.putExtra("selectedproduct",productsModel);
        i.putExtra("userid", userId);
        startActivity(i);
    }

    public void viewCartPage(View v){
        Intent i = new Intent(this, MyCartActivity.class);
        i.putExtra("userid",userId);
        startActivity(i);
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
