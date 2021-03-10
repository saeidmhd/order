package com.mahak.order.common.request.SetAllDataResult.TransferStoreDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mahak.order.common.request.SetAllDataResult.Orders.Result;

import java.util.List;

public class TransferStoreDetails {
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
