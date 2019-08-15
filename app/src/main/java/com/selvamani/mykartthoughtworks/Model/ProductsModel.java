package com.selvamani.mykartthoughtworks.Model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName= "tbl_Products")
public class ProductsModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long productId;
    private String prodNmae;
    private String prodCategory;
    private String prodPrice;


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProdNmae() {
        return prodNmae;
    }

    public void setProdNmae(String prodNmae) {
        this.prodNmae = prodNmae;
    }

    public String getProdCategory() {
        return prodCategory;
    }

    public void setProdCategory(String prodCategory) {
        this.prodCategory = prodCategory;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }


}
