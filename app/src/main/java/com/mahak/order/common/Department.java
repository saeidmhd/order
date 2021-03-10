package com.mahak.order.common;

import android.content.Context;

public class Department {

    private String Name;
    private String Email;
    private String Ename;
    public static String TAG_NAME = "Name";
    public static String TAG_ENAME = "EName";
    public static String TAG_EMAIL = "Email";
    private static Context mcontext;

    public Department() {
    }

    public Department(Context context) {
        Department.mcontext = context;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEname() {
        return Ename;
    }

    public void setEname(String ename) {
        Ename = ename;
    }
}
