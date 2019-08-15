package com.selvamani.mykartthoughtworks;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.selvamani.mykartthoughtworks.Adapters.CartAdapter;
import com.selvamani.mykartthoughtworks.LocalDatabase.LocalDBConnection;
import com.selvamani.mykartthoughtworks.Model.CartModel;
import com.selvamani.mykartthoughtworks.Model.ProductsModel;
import com.selvamani.mykartthoughtworks.Utils.Popup;
import com.selvamani.mykartthoughtworks.Utils.SwipeUtil;

import java.util.ArrayList;
import java.util.List;

public class MyCartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String userId;
    List<ProductsModel> currentproductsModels;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        recyclerView = (RecyclerView) findViewById( R.id.rv_recyclerview );

        userId =  getIntent().getStringExtra("userid");

        LoadCartaCallAsyncTask();
    }

    private void LoadCartaCallAsyncTask() {
        final List<ProductsModel> productsModels = new ArrayList<>();
        class ProductTable extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
//                    CartModel cartModel = new CartModel();
//                    cartModel.setUserId(Long.parseLong(userId));
//                    cartModel.setCartProdID(productsModel.getProductId());
//                    LocalDBConnection.getInstance(getApplicationContext()).
//                            LoadUserdata().insertCart(cartModel);
                    List<CartModel> prodIdsarray = new ArrayList<>();
//                    List<ProductsModel> productsModels = new ArrayList<>();

                    prodIdsarray = LocalDBConnection.getInstance(getApplicationContext()).
                            LoadUserdata().checkUserExistCart(userId);

                    if (prodIdsarray.size() != 0){
                        for (int k=0; k<prodIdsarray.size();k++){
                            ProductsModel mproductsModels = new ProductsModel();
                            mproductsModels = LocalDBConnection.getInstance(getApplicationContext()).
                                    LoadUserdata().insertProductofCart(String.valueOf(prodIdsarray.get(k).getCartProdID()));
                            productsModels.add(mproductsModels);
                        }
                    }


                } catch (Exception ex) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                currentproductsModels = new ArrayList<>();
                currentproductsModels = productsModels;


                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext() );
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new CartAdapter( getApplicationContext(), productsModels, userId);
                recyclerView.setAdapter(adapter);

                setSwipeForRecyclerView();
                Popup.ShowMessage(getApplicationContext(), "Swipe left to delete item from cart", 10);
            }

            private void setSwipeForRecyclerView() {

                SwipeUtil swipeHelper = new SwipeUtil(0, ItemTouchHelper.LEFT, getApplicationContext()) {
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int swipedPosition = viewHolder.getAdapterPosition();
                        adapter = (CartAdapter)recyclerView.getAdapter();
                        adapter.pendingRemoval(swipedPosition);
                    }

                    @Override
                    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        int position = viewHolder.getAdapterPosition();
                        adapter = (CartAdapter) recyclerView.getAdapter();
                        if (adapter.isPendingRemoval(position)) {
                            return 0;
                        }
                        return super.getSwipeDirs(recyclerView, viewHolder);
                    }
                };

                ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(swipeHelper);
                mItemTouchHelper.attachToRecyclerView(recyclerView);
                //set swipe label
                swipeHelper.setLeftSwipeLable("Delete");
                //set swipe background-Color
                swipeHelper.setLeftcolorCode( ContextCompat.getColor(getApplicationContext(), R.color.swipebackground));
            }


        }
        ProductTable saveUser = new ProductTable();
        saveUser.execute();
    }


    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}