package com.selvamani.mykartthoughtworks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.selvamani.mykartthoughtworks.LocalDatabase.LocalDBConnection;
import com.selvamani.mykartthoughtworks.Model.CartModel;
import com.selvamani.mykartthoughtworks.Model.ProductsModel;
import com.selvamani.mykartthoughtworks.Utils.Popup;

public class ProductDetailActivity extends AppCompatActivity {

    ProductsModel productsModel;
    ImageView prodImage;
    TextView prodName;
    TextView prodPrice;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        prodImage = (ImageView) findViewById(R.id.iv_productimage);
        prodName = (TextView) findViewById(R.id.tv_prodname);
        prodPrice = (TextView) findViewById(R.id.tv_price);

        productsModel = (ProductsModel) getIntent().getSerializableExtra("selectedproduct");
        userId =  getIntent().getStringExtra("userid");


//        prodImage.setImageResource(R.drawable.oven);
        prodName.setText(productsModel.getProdNmae());
        prodPrice.setText("Price : "+productsModel.getProdPrice()+" Rs");

        if(productsModel.getProdNmae().toLowerCase().contains("microwave")){
            prodImage.setImageResource(R.drawable.oven);
        }
        else if(productsModel.getProdNmae().toLowerCase().contains("television")){
            prodImage.setImageResource(R.drawable.television);
        }
        else if(productsModel.getProdNmae().toLowerCase().contains("vacuum")){
            prodImage.setImageResource(R.drawable.vacuumcleaner);
        }
        else if(productsModel.getProdNmae().toLowerCase().contains("table")){
            prodImage.setImageResource(R.drawable.table);
        }
        else if(productsModel.getProdNmae().toLowerCase().contains("chair")){
            prodImage.setImageResource(R.drawable.chairs);
        }
        else if(productsModel.getProdNmae().toLowerCase().contains("almirah")){
            prodImage.setImageResource(R.drawable.almirah);
        }
    }

    public void viewCartPage(View v){
        this.finish();
        Intent i = new Intent(this, MyCartActivity.class);
        i.putExtra("userid",userId);
        startActivity(i);
    }

    public void addtoCart(View v){
        AddtoCartaCallAsyncTask(String.valueOf(productsModel.getProductId()));
    }

    private void AddtoCartaCallAsyncTask(String prodId) {
        class ProductTable extends AsyncTask<Void, Void, Void> {
            int exist = 0;

            @Override
            protected Void doInBackground(Void... voids) {
                try {

                     exist = LocalDBConnection.getInstance(getApplicationContext()).
                            LoadUserdata().checkProdIdExist(prodId, userId);

                    if (exist == 1){
                    }
                    else{
                        CartModel cartModel = new CartModel();
                        cartModel.setUserId(Long.parseLong(userId));
                        cartModel.setCartProdID(productsModel.getProductId());
                        LocalDBConnection.getInstance(getApplicationContext()).
                                LoadUserdata().insertCart(cartModel);

                    }


                } catch (Exception ex) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (exist == 1){
                    Popup.ShowSuccessMessage(getApplicationContext(), "Item already exist in cart", 10);
                }
                else{
                    Popup.ShowSuccessMessage(getApplicationContext(), "Item added to cart successfully", 10);

                }

            }

        }
        ProductTable saveUser = new ProductTable();
        saveUser.execute();
    }


}
