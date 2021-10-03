package com.example.librarymanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<student,myadapter.myviewholder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<student> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull student model) {
        holder.id.setText("Id: "+model.getId());
        holder.name.setText("Name: "+model.getName());
        holder.gender.setText("Gender: "+model.getGender());
        holder.dep.setText("Department: "+model.getDepartment());
        holder.phone.setText("Phone: "+model.getPhone());
        holder.email.setText("Email: "+model.getEmail());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_row,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView id,name,gender,dep,phone,email;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.stidtextview);
            name=itemView.findViewById(R.id.stnametextview);
            gender=itemView.findViewById(R.id.stgendertextview);
            dep=itemView.findViewById(R.id.stdptextview);
            phone=itemView.findViewById(R.id.stphonetextview);
            email=itemView.findViewById(R.id.stemailtextview);
        }
    }
}
