package com.example.useraccountmanagement.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.PlaceHolder> {
    Context con;
    String token;
    List<User> list;
    CVItemClick cvItemClick;
    Dialog dialog;

    public UserListAdapter() {
    }

    public UserListAdapter(Context con, List<User> list, String token) {
        this.con = con;
        this.list = list;
        this.token=token;
    }

    public void setUpdateList(List<User> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(con).inflate(R.layout.custom_agent_list,viewGroup,false);
        PlaceHolder p=new PlaceHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaceHolder placeHolder, final int i) {
        placeHolder.tvname.setText(list.get(i).getName());
        placeHolder.tvpoints.setText(String.valueOf(list.get(i).getPoints()));
        placeHolder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvItemClick.agentEdit(list.get(i).getEmail(), (int) list.get(i).getPoints(),placeHolder.EditRecyclerView,placeHolder.arrow);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {

        TextView tvname,tvpoints;
        RelativeLayout relative;
        View arrow;
        RecyclerView EditRecyclerView;

        public PlaceHolder(@NonNull final View itemView) {
            super(itemView);
            tvname=itemView.findViewById(R.id.agentlist_name);
            tvpoints=itemView.findViewById(R.id.agentlist_points);
            relative=itemView.findViewById(R.id.relative);
            arrow=itemView.findViewById(R.id.arrowview);
            EditRecyclerView=itemView.findViewById(R.id.editRecyclerView);
        }
    }
    public interface CVItemClick{
        public void agentEdit(String email, int points, RecyclerView edRe, View arrow);
    }
    public void setAdapterIteamClickListener(CVItemClick cvItemClick){
        this.cvItemClick=cvItemClick;
    }
}
