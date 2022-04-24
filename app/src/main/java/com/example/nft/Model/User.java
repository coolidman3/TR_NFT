package com.example.nft.Model;

import java.util.List;

public class User {
    private String name,owner,avatar,id;
    private Long price;
    List<String>history;

    public User(){

    }



    public User(String name, Long price, String owner, String avatar){
        this.name = name;
        this.price = price;
        this.owner = owner;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
