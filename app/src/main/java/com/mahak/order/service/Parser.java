package com.mahak.order.service;

import android.content.Context;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.mahak.order.BaseActivity;
import com.mahak.order.common.Contact;
import com.mahak.order.common.Department;
import com.mahak.order.common.ServiceTools;
import com.mahak.order.common.TrackingConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.mahak.order.common.ProductInReturn;
//import com.mahak.order.common.ReturnOfSale;
//import com.mahak.order.common.DeliveryOrder;
//import com.mahak.order.common.ProductInOrder;

public class Parser {

    public static ArrayList<TrackingConfig> getTrackingConfig(String jData) {

        ArrayList<TrackingConfig> arrayConfig = new ArrayList<>();
        TrackingConfig item;
        try {
            JSONArray jArrayConfig = new JSONArray(jData);
            for (int i = 0; i < jArrayConfig.length(); i++) {
                JSONObject jObjItem = jArrayConfig.getJSONObject(i);

                item = new TrackingConfig();
                if (jObjItem.getString(TrackingConfig.TAG_AdminControl).length() != 0)
                    item.setAdminControl(jObjItem.getInt(TrackingConfig.TAG_AdminControl));
                if (jObjItem.getString(TrackingConfig.TAG_TrackingControl).length() != 0)
                    item.setTrackingControl(jObjItem.getInt(TrackingConfig.TAG_TrackingControl));

                arrayConfig.add(item);
            }//End of for

        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            // TODO: handle exception
        }
        return arrayConfig;
    }

    /**
     * @param json     is String type
     * @param mcontext is Context type
     * @return arrayList from Department calss
     */
    public static ArrayList<Department> getDepartment(String json, Context mcontext) {
        ArrayList<Department> array = new ArrayList<>();
        Department item;
        try {
            JSONArray Jarray = new JSONArray(json);
            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject Jobject = Jarray.getJSONObject(i);
                item = new Department(mcontext);
                item.setName(Jobject.getString(Department.TAG_NAME));
                item.setEmail(Jobject.getString(Department.TAG_EMAIL));
                item.setEname(Jobject.getString(Department.TAG_ENAME));
                array.add(item);
            }//end of for

        } catch (Exception e) {
            ServiceTools.logToFireBase(e);
            // TODO: handle exception
        }
        return array;
    }

    /**
     * @param contact is Context type
     * @return String type
     */
    public static String SendEmail(Contact contact) {
        String json_format = "[{";
        json_format += "\"Name\":\"" + contact.getName() + "\",\"Email\":\"" + contact.getEmail() + "\",\"Tell\":\"" + contact.getTell() + "\",\"Department\":\"" + contact.getDepartment() + "\",\"Subject\":\"" + contact.getSubject() + "\",\"Body\":\"" + contact.getBody() + "\"";
        json_format += "}]";
        return json_format;
    }


}
