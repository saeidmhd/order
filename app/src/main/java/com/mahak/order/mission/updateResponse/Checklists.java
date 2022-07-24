
package com.mahak.order.mission.updateResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Checklists {

    @SerializedName("results")
    @Expose
    private List<Object> results = null;

    public List<Object> getResults() {
        return results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

}
