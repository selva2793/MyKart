package com.selvamani.mykartthoughtworks.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "tbl_MyCart", indices = @Index(value = "userId"))
public class CartModel {

    @PrimaryKey(autoGenerate = true)
    private long mycartId;
    private long userId;
    private long cartProdID;

    public long getMycartId() {
        return mycartId;
    }

    public void setMycartId(long mycartId) {
        this.mycartId = mycartId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCartProdID() {
        return cartProdID;
    }

    public void setCartProdID(long cartProdID) {
        this.cartProdID = cartProdID;
    }

}
