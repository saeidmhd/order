package com.mahak.order.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaxInSell_Extra_Data {

    @SerializedName("TaxInSellCost")
    @Expose
    private boolean taxInSellCost;

    public boolean getTaxInSellCost() {
        return taxInSellCost;
    }

    public void setTaxInSellCost(boolean taxInSellCost) {
        this.taxInSellCost = taxInSellCost;
    }

}
