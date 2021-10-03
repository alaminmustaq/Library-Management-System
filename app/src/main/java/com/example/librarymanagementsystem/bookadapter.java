package com.example.librarymanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



public class bookadapter extends FirebaseRecyclerAdapter<book,bookadapter.myviewholder> {
    public bookadapter(@NonNull FirebaseRecyclerOptions<book> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull book model) {
        holder.name.setText("Name: "+model.getName());
        holder.author_name.setText("Author Name: "+model.getAuthor_name());
        holder.pub.setText("Publisher: "+model.getPublisher());
        holder.no.setText("No of books: "+model.getNo());
        holder.loc.setText("Location: "+model.getLoc());
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_row,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        TextView name,author_name,pub,no,loc;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.bknametextview);
            author_name=itemView.findViewById(R.id.bkanametextview);
            pub=itemView.findViewById(R.id.bkpublishertextview);
            no=itemView.findViewById(R.id.bknotextview);
            loc=itemView.findViewById(R.id.bkloctextview);
        }
    }
}
