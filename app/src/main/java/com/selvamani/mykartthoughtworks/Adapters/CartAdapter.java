package com.selvamani.mykartthoughtworks.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selvamani.mykartthoughtworks.LocalDatabase.LocalDBConnection;
import com.selvamani.mykartthoughtworks.Model.ProductsModel;
import com.selvamani.mykartthoughtworks.ProductDetailActivity;
import com.selvamani.mykartthoughtworks.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartAdapter<ItemViewHolder>extends RecyclerView.Adapter<CartAdapter.ItemViewHolder> {

    int totalpricce = 0;
    private List<ProductsModel> myList = new ArrayList<>(  );
    Context mcontext;
    private List<ProductsModel> itemsPendingRemoval  = new ArrayList<ProductsModel>(  );
    private String userId;
    private static final Object PENDING_REMOVAL_TIMEOUT =3000 ;
    private Handler handler = new Handler();
    HashMap<ProductsModel, Runnable> pendingRunnables = new HashMap<ProductsModel, Runnable>(); // map of items to pending runnable, to cancel the removal



    public CartAdapter(Context context, List<ProductsModel> myList, String userid) {
        this.mcontext = context;
        this.myList = myList;
        this.userId = userid;
        itemsPendingRemoval = new ArrayList<ProductsModel>();
    }

    @Override
    public CartAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate( R.layout.customlayout, parent, false );
        return new CartAdapter.ItemViewHolder( itemView );
    }

    @Override
    public void onBindViewHolder(CartAdapter.ItemViewHolder holder,  int position) {

        final ProductsModel productsModel = myList.get(position);
        String pName = productsModel.getProdNmae();
        String pPrice = productsModel.getProdPrice();
        Log.d( "fgdhfumghj", "onBindViewHolder: " + myList.get( position ));


        if (productsModel != null) {
            int i = Integer.parseInt(productsModel.getProdPrice());
            totalpricce += i;
//            Log.d("sjnjssda", "onBindViewHolder: "+totalpricce);
        }

        if (itemsPendingRemoval != null) {
            if (itemsPendingRemoval.contains( productsModel )) {
                /** show swipe layout and hide regular layout */
                holder.regularLayout.setVisibility( View.GONE );
                holder.swipeLayout.setVisibility( View.VISIBLE );
                holder.undo.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoOpt( productsModel );
                    }

                } );
            } else {
                /** show regular layout and hide swipe layout*/
                holder.regularLayout.setVisibility( View.VISIBLE );
                holder.swipeLayout.setVisibility( View.GONE );
                holder.productName.setText(pName);
                holder.productPrice.setText("Rs."+pPrice);

                if(productsModel.getProdNmae().toLowerCase().contains("microwave")){
                    holder.prodImage.setImageResource(R.drawable.oven);
                }
                else if(productsModel.getProdNmae().toLowerCase().contains("television")){
                    holder.prodImage.setImageResource(R.drawable.television);
                }
                else if(productsModel.getProdNmae().toLowerCase().contains("vacuum")){
                    holder.prodImage.setImageResource(R.drawable.vacuumcleaner);
                }
                else if(productsModel.getProdNmae().toLowerCase().contains("table")){
                    holder.prodImage.setImageResource(R.drawable.table);
                }
                else if(productsModel.getProdNmae().toLowerCase().contains("chair")){
                    holder.prodImage.setImageResource(R.drawable.chairs);
                }
                else if(productsModel.getProdNmae().toLowerCase().contains("almirah")){
                    holder.prodImage.setImageResource(R.drawable.almirah);
                }

                if (position == myList.size()-1){
                    holder.totalLayout.setVisibility(View.VISIBLE);
                    holder.totalPrice.setText("Total- Rs."+totalpricce);
                }
                else{
                    holder.totalLayout.setVisibility(View.GONE);
                }

                holder.regularLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mcontext, ProductDetailActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("selectedproduct",myList.get(position));
                        i.putExtra("userid", userId);
                        mcontext.startActivity(i);
                    }
                });
            }
        }
        Log.d( "nmbkghbdfjkgyhkbixcug", "onBindViewHolder: " );
    }

    private void undoOpt(ProductsModel productsMode) {

        Runnable pendingRemovalRunnable = (Runnable) pendingRunnables.get( productsMode );
        pendingRunnables.remove( productsMode );
        if (pendingRemovalRunnable != null)
            handler.removeCallbacks( pendingRemovalRunnable );
        itemsPendingRemoval.remove( productsMode );
        // this will rebind the row in "normal" state
        notifyItemChanged( myList.indexOf( productsMode ) );

    }
    @Override
    public int getItemCount() {
        return myList.size();
    }


    public void pendingRemoval(int position) {
        final ProductsModel productsModel = myList.get( position );
//        final String data =  myList.get( position );
        if (!itemsPendingRemoval.contains( productsModel )) {
            itemsPendingRemoval.add(productsModel);
            // this will redraw row in "undo" state
            notifyItemChanged( position );
            //create, store and post a runnable to remove the data
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove( myList.indexOf( productsModel ) );
                }
            };
            handler.postDelayed( pendingRemovalRunnable, (int) PENDING_REMOVAL_TIMEOUT );
            pendingRunnables.put( productsModel, pendingRemovalRunnable );
        }
    }

    public void remove(int position) {
        ProductsModel productsModel = myList.get( position );
//        String data =  myList.get( position );
        if (itemsPendingRemoval.contains( productsModel )) {
            itemsPendingRemoval.remove( productsModel );
            totalpricce = 0;
            notifyItemRemoved( position );
            RemoveProdIDCallAsyncTask(position);
        }
        if (myList.contains( productsModel )) {
            myList.remove( position );
            notifyItemRemoved( position );
//            RemoveProdIDCallAsyncTask(position);
        }

        notifyDataSetChanged();
    }

    public boolean isPendingRemoval(int position) {
        ProductsModel productsModel = myList.get( position );
        return itemsPendingRemoval.contains( productsModel );
    }

    private void RemoveProdIDCallAsyncTask(int position) {
        class ProductTable extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
//
                    LocalDBConnection.getInstance(mcontext).
                            LoadUserdata().deleteProductofCart(String.valueOf(myList.get(position).getProductId()), userId);

                } catch (Exception ex) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }


        }

        ProductTable saveUser = new ProductTable();
        saveUser.execute();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {


        public View regularLayout;
        public View swipeLayout;
        public View undo;
        public TextView productName;
        public TextView productPrice;
        public TextView totalPrice;
        public LinearLayout totalLayout;
        public ImageView prodImage;

        public ItemViewHolder(View itemView) {

            super( itemView );

            regularLayout=(LinearLayout) itemView.findViewById( R.id.regularLayout);
            swipeLayout =  (LinearLayout) itemView.findViewById( R.id.swipeLayout);
            undo = (TextView) itemView.findViewById( R.id.undo );
            productName = (TextView)itemView.findViewById( R.id.tv_prodname );
            productPrice = (TextView)itemView.findViewById( R.id.tv_prodprice );
            totalPrice = (TextView)itemView.findViewById( R.id.tv_totalscore );
            totalLayout = (LinearLayout)itemView.findViewById( R.id.lv_totalscorelayout);
            prodImage = (ImageView) itemView.findViewById( R.id.iv_prodimage );
        }

    }

}
