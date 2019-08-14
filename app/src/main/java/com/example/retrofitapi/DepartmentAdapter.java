package com.example.retrofitapi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {
    public List<Department> listDepartment;
    private OnDepartmentItemClickListener listener;
    public DepartmentAdapter(List<Department> departments, OnDepartmentItemClickListener listener) {
        this.listDepartment = departments;
        this.listener = listener;
    }

    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.department_list_row, viewGroup, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DepartmentViewHolder departmentViewHolder, int i) {
        final Department department = listDepartment.get(i);
        departmentViewHolder.department.setText(department.getDeptname().toString());
        departmentViewHolder.departmentId.setText(department.getDeptId().toString());
        departmentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clicked position
//                int position = peopleViewHolder.getAdapterPosition();
                listener.onDepartmentItemClick(department);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDepartment.size();
    }

    public class DepartmentViewHolder extends RecyclerView.ViewHolder {
        public TextView departmentId,department;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            departmentId = itemView.findViewById(R.id.tvDepartmentId);
            department = itemView.findViewById(R.id.tvDepartment);
        }
    }

    interface OnDepartmentItemClickListener {
        void onDepartmentItemClick(Department department);
    }
}
