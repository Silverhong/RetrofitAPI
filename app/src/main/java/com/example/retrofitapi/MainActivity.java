package com.example.retrofitapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DepartmentAdapter.OnDepartmentItemClickListener {
    RecyclerView recyclerView;
    List<Department> listDepartment;
    DepartmentAdapter departmentAdapter;
    Department selectedDepartment;
    TextView txtDepartment;
    ApiService apiService;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Init
        txtDepartment = findViewById(R.id.txtDepartment);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listDepartment = new ArrayList<>();
        departmentAdapter = new DepartmentAdapter(listDepartment,this);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Clear();
                Init();
            }
        });
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://c84be254.ngrok.io/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiService = retrofit.create(ApiService.class);
        Init();
    }
    private void Init(){
        swipeRefreshLayout.setRefreshing(true);
            final Call<List<Department>> req = apiService.getAllDepartment();
                req.enqueue(new Callback<List<Department>>() {
                    @Override
                    public void onResponse(Call<List<Department>> call, Response<List<Department>> response) {
                        if(response.isSuccessful()) {
                            View(response.body());
                            Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    @Override
                    public void onFailure(Call<List<Department>> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Failed to connect to server",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void View(List<Department> listDepartment){
        departmentAdapter.listDepartment = listDepartment;
        recyclerView.setAdapter(departmentAdapter);
        departmentAdapter.notifyDataSetChanged();
    }
    private void Insert(Department department){
        Call<String> request = apiService.addDepartment(department);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Init();
                txtDepartment.setText("");
                Toast.makeText(getApplicationContext(),"Insert successful",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(),"Insert unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void Delete(String id){
        Call<String> request = apiService.deleteDepartmentById(id);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Init();
                txtDepartment.setText("");
                Toast.makeText(getApplicationContext(),"Delete successful",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Delete unsuccessful",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDepartmentItemClick(Department department) {
        selectedDepartment=department;
        txtDepartment.setText(department.getDeptname());
    }

    public void btnRemoveClicked(View view) {
        if(txtDepartment.length()>0){
            Delete(selectedDepartment.getDeptId());
        }
    }

    public void btnInsertClicked(View view) {
        if(txtDepartment.length()>0){
            Department department = new Department();
            department.setDeptname(txtDepartment.getText().toString());
            Insert(department);
        }
    }
    private void Clear(){
        departmentAdapter.listDepartment.clear();
        departmentAdapter.notifyDataSetChanged();
    }
}
