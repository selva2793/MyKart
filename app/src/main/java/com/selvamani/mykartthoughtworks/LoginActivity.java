package com.selvamani.mykartthoughtworks;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.selvamani.mykartthoughtworks.LocalDatabase.LocalDBConnection;
import com.selvamani.mykartthoughtworks.Model.ProductsModel;
import com.selvamani.mykartthoughtworks.Model.UserModel;
import com.selvamani.mykartthoughtworks.Utils.Popup;
import com.selvamani.mykartthoughtworks.Utils.hideSoftInputFromWindow;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    Button btnSubmit;
    EditText edtUsername;
    EditText edtPassword;
    LinearLayout profileDetailsLayout;
    UserModel userModel;
    ProgressDialog progressDialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    String TAG = LoginActivity.class.getName();
    private String btnclickText = "";
    private boolean calLogin = false;
    List<ProductsModel> products;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        btnSubmit = (Button) findViewById(R.id.bt_submit);
        btnRegister = (Button) findViewById(R.id.bt_register);
        btnLogin = (Button) findViewById(R.id.bt_login);
        profileDetailsLayout = (LinearLayout) findViewById(R.id.ll_details);
        edtUsername = (EditText) findViewById(R.id.et_username);
        edtPassword = (EditText) findViewById(R.id.et_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

//        checkAndRequestPermissions(this);
        progressDialog.dismiss();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("login", "onClick: ");
                btnclickText = "Register";
                clickFunction();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("login", "onClick: ");
                btnclickText = "Login";
                clickFunction();
            }
        });

        createProductModel();

    }

    private void createProductModel(){
        ProductsModel productsModel = new ProductsModel();
        products = new ArrayList<>();
        productsModel.setProdNmae("Microwave oven");
        productsModel.setProdCategory("Electronics");
        productsModel.setProdPrice("8000");

        products.add(productsModel);

        productsModel = new ProductsModel();
        productsModel.setProdNmae("Television");
        productsModel.setProdCategory("Electronics");
        productsModel.setProdPrice("20000");

        products.add(productsModel);

        productsModel = new ProductsModel();
        productsModel.setProdNmae("Vacuum Cleaner");
        productsModel.setProdCategory("Electronics");
        productsModel.setProdPrice("16000");

        products.add(productsModel);

        productsModel = new ProductsModel();
        productsModel.setProdNmae("Table");
        productsModel.setProdCategory("Furniture");
        productsModel.setProdPrice("30000");

        products.add(productsModel);

        productsModel = new ProductsModel();
        productsModel.setProdNmae("Chair");
        productsModel.setProdCategory("Furniture");
        productsModel.setProdPrice("10000");

        products.add(productsModel);

        productsModel = new ProductsModel();
        productsModel.setProdNmae("Almirah");
        productsModel.setProdCategory("Furniture");
        productsModel.setProdPrice("22000");

        products.add(productsModel);
    }


    public static boolean checkAndRequestPermissions(final Activity context) {

        int LocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int CoarseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (LocationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (CoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    private void clickFunction(){
        btnLogin.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        profileDetailsLayout.setVisibility(View.VISIBLE);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                hideSoftInputFromWindow hidesoftkeyBoard = new hideSoftInputFromWindow();
                hidesoftkeyBoard.hideSoftInputmWindow(getApplicationContext(), btnSubmit);

                Log.d("login", "onClick: ");
                // TODO Auto-generated method stub
                if (edtUsername.getText().toString().trim().length() == 0) {
                    edtUsername.setError("UserName is not entered");
                    edtUsername.requestFocus();
                    return;
                }
                if (edtPassword.getText().toString().trim().length() == 0) {
                    edtPassword.setError("Password is not entered");
                    edtPassword.requestFocus();
                    return;
                }

                if (btnclickText.equalsIgnoreCase("login")){
                    CallAsyncTask(edtUsername.getText().toString(), edtPassword.getText().toString());
//                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
//                    i.putExtra("username", edtUsername.getText().toString());
//                    startActivity(i);
                }
                else if (btnclickText.equalsIgnoreCase("register")){
//                    userModel.setName("selva");
//                    userModel.setPassword("dfddd");
                    CallAsyncTask(edtUsername.getText().toString(), edtPassword.getText().toString());
                }
            }
        });
    }
    private void CallAsyncTask(final String userName, final String password) {

        userModel = new UserModel();
        userModel.setName(userName);
        userModel.setPassword(password);

        class SaveUser extends AsyncTask<Void, Void, Void> {
            int userid;
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Log.d(TAG, "doInBackground: "+userName+"     "+password);
                    int ID = LocalDBConnection.getInstance(getApplicationContext()).
                            LoadUserdata().checkUserExist(userName, password); //chcek id exists or not

                    int ProdID = LocalDBConnection.getInstance(getApplicationContext()).
                            LoadUserdata().checkProductdataExist(); //chcek productdata exists or not
                    Log.d(TAG, "doInBackground: "+ProdID);


                    if (ID == 1){
                        if (btnclickText.equalsIgnoreCase("login")){
                            userid = LocalDBConnection.getInstance(getApplicationContext()).
                                    LoadUserdata().getUserid(userName, password);
                            calLogin = true;
                        }
                        if (ProdID == 0){
                            for (ProductsModel productsModel : products) {
                                LocalDBConnection.getInstance(getApplicationContext()).
                                        LoadUserdata().setProductMoel(productsModel);
                            }
                        }
                    }
                    else{
                        if (btnclickText.equalsIgnoreCase("register")) {
                            LocalDBConnection.getInstance(getApplicationContext()).
                                    LoadUserdata().setUserMoel(userModel);

                            userid = LocalDBConnection.getInstance(getApplicationContext()).
                                    LoadUserdata().getUserid(userName, password);
                            calLogin = true;
                        }

                        if (ProdID == 0){
                            for (ProductsModel productsModel : products) {
                                LocalDBConnection.getInstance(getApplicationContext()).
                                        LoadUserdata().setProductMoel(productsModel);
                            }
                        }
                    }

                } catch (Exception ex) {

                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (calLogin){
                    calLogin = false;
                    Intent i = new Intent(getApplicationContext(), ProductlistActivity.class);
                    i.putExtra("username", edtUsername.getText().toString());
                    i.putExtra("userid", String.valueOf(userid));
                    startActivity(i);
                }
                else{
                    showToast();
                }
            }

            private void showToast(){
                if (btnclickText.equalsIgnoreCase("register")) {
                    Popup.ShowErrorMessageString(getApplicationContext(), "Login credentials already exist", 10);
                }
                else{
                    Popup.ShowErrorMessageString(getApplicationContext(), "Login credentials wrong", 10);
                }
            }
        }
        SaveUser saveUser = new SaveUser();
        saveUser.execute();
    }
}
