package com.chetana.paginationassignment.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chetana.paginationassignment.Class.User;
import com.chetana.paginationassignment.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.RecyclerviewAdapterViewHolder>{

    Context parent;
    ArrayList<User.Data> userArrayList;
    private String fName="", lName="", eMail="", avtar="";
    RecyclerView recyclerView;

    public RecyclerviewAdapter(Context parent, ArrayList<User.Data> userArrayList) {
        this.parent = parent;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public RecyclerviewAdapter.RecyclerviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_recycleview, parent, false);

        return new RecyclerviewAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewAdapter.RecyclerviewAdapterViewHolder holder, int position) {
        fName = userArrayList.get(position).getFirst_name();
        lName = userArrayList.get(position).getLast_name();
        eMail = userArrayList.get(position).getEmail();
        avtar = userArrayList.get(position).getAvatar();

        holder.txtname.setText(fName + " "+lName);
        holder.txtemail.setText(eMail);

        try{
            Picasso.with(parent)
                    .load(avtar)
                    //.placeholder(Integer.parseInt(avtar)).error(R.mipmap.ic_launcher)      // optional
                    //.resize(60,60)                        // optional
                    .into(holder.imgprof);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class RecyclerviewAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txtname)
        TextView txtname;
        @BindView(R.id.txtemail)
        TextView txtemail;
        @BindView(R.id.imgprof)
        ImageView imgprof;

        public RecyclerviewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
           ButterKnife.bind(this,itemView);

        }
    }
}
