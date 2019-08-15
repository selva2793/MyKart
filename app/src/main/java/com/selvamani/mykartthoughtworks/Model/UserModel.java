package com.selvamani.mykartthoughtworks.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName= "tbl_Users")
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    private long id = 0;
    private String name = "";
    private String password = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
