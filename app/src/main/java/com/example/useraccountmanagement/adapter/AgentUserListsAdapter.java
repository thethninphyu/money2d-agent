package com.example.useraccountmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.AgentUser;

import java.util.ArrayList;
import java.util.List;

public class AgentUserListsAdapter extends RecyclerView.Adapter<AgentUserListsAdapter.PlaceHolder> {

    Context context;
    List<AgentUser> list;

    public AgentUserListsAdapter(Context context, ArrayList<AgentUser> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_agent_user_lists,viewGroup,false);
        PlaceHolder placeHolder=new PlaceHolder(view);
        return placeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder placeHolder, int i) {
        placeHolder.name.setText(list.get(i).getName());
        placeHolder.point.setText(list.get(i).getPoints().toString());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {
        TextView name,point;
        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.agent_user_name);
            point=itemView.findViewById(R.id.agent_user_points);
        }
    }
}
