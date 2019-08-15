package com.selvamani.mykartthoughtworks.LocalDatabase.DAO;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.selvamani.mykartthoughtworks.Model.CartModel;
import com.selvamani.mykartthoughtworks.Model.ProductsModel;
import com.selvamani.mykartthoughtworks.Model.UserModel;

import java.util.List;

@Dao
public interface DAO {

    @Query("SELECT 1 FROM tbl_Users WHERE  name = :name and password =:pswd")
    int checkUserExist(String name, String pswd); // id Exists means not insert the specific id

    @Query("SELECT 1 FROM tbl_Products WHERE  productId != '' ")
    int checkProductdataExist(); // id Exists means not insert the specific id

    @Insert
    long setUserMoel(UserModel userModel);


    @Insert
    long setProductMoel(ProductsModel productsModel);

    @Insert
    long insertCart(CartModel productsModel);

    @Query("SELECT id FROM tbl_Users WHERE  name = :name and password =:pswd")
    int getUserid(String name, String pswd); // id Exists means not insert the specific id

    @Query("SELECT * FROM tbl_Products")
    List<ProductsModel> loadProducts(); //

    @Query("SELECT * FROM tbl_MyCart WHERE userId =:userid")
    List<CartModel> checkUserExistCart(String userid); //

    @Query("SELECT * FROM tbl_Products WHERE productId =:prodid")
    ProductsModel insertProductofCart(String prodid); //

    @Query("DELETE FROM tbl_MyCart WHERE cartProdID = :pId and userId =:userid")
    void deleteProductofCart(String pId, String userid);

    @Query("SELECT 1 FROM tbl_MyCart WHERE  cartProdID = :prodId and userId =:userid")
    int checkProdIdExist(String prodId, String userid); // id Exists means not insert the specific id

}