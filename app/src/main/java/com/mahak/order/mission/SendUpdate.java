
package com.mahak.order.mission;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendUpdate {

    @SerializedName("missions")
    @Expose
    private ArrayList<Mission> missions = null;

    public ArrayList<Mission> getMissions() {
        return missions;
    }

    public void setMissions(ArrayList<Mission> missions) {
        this.missions = missions;
    }

}
