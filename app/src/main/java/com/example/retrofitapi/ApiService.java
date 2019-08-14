package com.example.retrofitapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("department/getAll")
    Call<List<Department>> getAllDepartment();
    @DELETE("department/deleteById")
    Call<String> deleteDepartmentById(@Query("DeptId") String id);
    @POST("department/Add")
    Call<String> addDepartment(@Body Department department);
}
