package com.example.useraccountmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.Wonlist;

import java.util.ArrayList;

public class WonListDetailsAdapter extends RecyclerView.Adapter<WonListDetailsAdapter.MPlaceholder> {

    Context con;
    ArrayList<Wonlist> list;

    public WonListDetailsAdapter(Context con, ArrayList<Wonlist> list) {
        this.con=con;
        this.list = list;
    }

    @NonNull
    @Override
    public MPlaceholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(con).inflate(R.layout.custom_won_list_details,viewGroup,false);
        MPlaceholder ph=new MPlaceholder(v);
        return ph;
    }

    @Override
    public void onBindViewHolder(@NonNull MPlaceholder mPlaceholder, int i) {
        mPlaceholder.tvWonNumber.setText(list.get(i).getNumber());
        mPlaceholder.tvWonPoint.setText(String.valueOf(list.get(i).getPoint()));
        mPlaceholder.tvWon.setText(String.valueOf(list.get(i).getWon()));
        mPlaceholder.tvWonUser.setText(list.get(i).getUser());
        mPlaceholder.tvWonAgent.setText(list.get(i).getAgent());
        mPlaceholder.tvWonEndTime.setText(list.get(i).getEndTime());
        mPlaceholder.tvWonDate.setText(list.get(i).getDate());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MPlaceholder extends RecyclerView.ViewHolder {

        TextView tvWonNumber,tvWonPoint,tvWon,tvWonUser,tvWonAgent,tvWonEndTime,tvWonDate;

        public MPlaceholder(@NonNull View itemView) {
            super(itemView);

            tvWonNumber=itemView.findViewById(R.id.tv_wonNumber);
            tvWonPoint=itemView.findViewById(R.id.tv_wonPoint);
            tvWon=itemView.findViewById(R.id.tv_won);
            tvWonUser=itemView.findViewById(R.id.tv_won_user);
            tvWonAgent=itemView.findViewById(R.id.tv_won_agent);
            tvWonEndTime=itemView.findViewById(R.id.tv_won_endTime);
            tvWonDate=itemView.findViewById(R.id.tv_won_date);
        }
    }
}
