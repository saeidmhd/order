package com.mahak.order.common;

import java.util.ArrayList;

public class State {

    String Name;
    ArrayList<String> arrayCity;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<String> getArrayCity() {
        return arrayCity;
    }

    public void setArrayCity(ArrayList<String> arrayCity) {
        this.arrayCity = arrayCity;
    }

}
