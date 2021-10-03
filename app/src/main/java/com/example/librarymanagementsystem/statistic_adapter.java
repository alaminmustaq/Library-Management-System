package com.example.librarymanagementsystem;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class statistic_adapter extends FirebaseRecyclerAdapter<issue,statistic_adapter.myviewholder> {
    public statistic_adapter(@NonNull FirebaseRecyclerOptions<issue> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull issue model) {
        holder.id.setText("Student id: "+model.getStudent_id());
        holder.name.setText("Book name: "+model.getBook_name());
        holder.author_name.setText("Author name: "+model.getAuthor_name());
        holder.issue_date.setText("Issue date: "+model.getIssue_date());
        holder.due_date.setText("Due date: "+model.getDue_date());
        holder.return_date.setText("Return date: "+model.getReturn_date());
        if(model.getStatus().equals("Issued"))
        {
            holder.status.setTextColor(Color.parseColor("#E47DA6"));
            holder.status.setText("Status: "+model.getStatus());
        }
        else
        {
            holder.status.setTextColor(Color.parseColor("#9BA5DD"));
            holder.status.setText("Status: "+model.getStatus());
        }

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistic_row,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView id,name,author_name,issue_date,due_date,return_date,status;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.stidtextview);
            name=itemView.findViewById(R.id.bknametextview);
            author_name=itemView.findViewById(R.id.authornametextview);
            issue_date=itemView.findViewById(R.id.issuedatetextview);
            due_date=itemView.findViewById(R.id.duedatetextview);
            return_date=itemView.findViewById(R.id.returndatetextview);
            status=itemView.findViewById(R.id.statustextview);
        }
    }
}
