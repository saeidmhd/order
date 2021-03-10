package com.mahak.order.common.request.SetAllDataResult.Products;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Products {

    @SerializedName("Results")
    @Expose
    private List<Result> Results = null;


    public List<Result> getResults() {
        return Results;
    }

    public void setResults(List<Result> results) {
        Results = results;
    }

}