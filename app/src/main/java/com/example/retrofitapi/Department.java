package com.example.retrofitapi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Department{
    @SerializedName("DeptId")
    private String DeptId;
    @SerializedName("Deptname")
    private String Deptname;

    public String getDeptId() {
        return DeptId;
    }

    public void setDeptId(String deptId) {
        DeptId = deptId;
    }

    public String getDeptname() {
        return Deptname;
    }

    public void setDeptname(String deptname) {
        Deptname = deptname;
    }
}
