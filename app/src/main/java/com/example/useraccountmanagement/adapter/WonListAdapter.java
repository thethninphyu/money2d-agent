package com.example.useraccountmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.WonListDate;
import com.example.useraccountmanagement.WonListDetailsActivity;
import com.example.useraccountmanagement.model.Wonlist;

import java.util.ArrayList;

public class WonListAdapter extends RecyclerView.Adapter<WonListAdapter.Placeholder> {

    Context con;
    ArrayList<Wonlist> pointlist;
    ArrayList<String> date,time;
    ArrayList<WonListDate> finalList;
    String token;

    public WonListAdapter(Context con, ArrayList<Wonlist> list, String token) {
        this.con = con;
        this.pointlist=list;
        this.token=token;

        ArrayList<String>duplicateTime=new ArrayList<>();
        ArrayList<String>duplicateDate=new ArrayList<>();
        for (int i=0;i<pointlist.size();i++){
            duplicateTime.add(pointlist.get(i).getEndTime());
            duplicateDate.add(pointlist.get(i).getDate());
        }
        date=new ArrayList<>();
        time=new ArrayList<>();

        for (int i=0;i<duplicateDate.size();i++){
            if (date.contains(duplicateDate.get(i)) && time.contains(duplicateTime.get(i))){
                continue;
            }else {
                date.add(duplicateDate.get(i));
                time.add(duplicateTime.get(i));
            }
        }

        finalList=new ArrayList<>();
        for (int i=0;i<date.size();i++){
            finalList.add(new WonListDate(date.get(i),time.get(i)));
        }
    }

    @NonNull
    @Override
    public Placeholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(con).inflate(R.layout.custom_won_list,viewGroup,false);
        Placeholder ph=new Placeholder(v);
        return ph;
    }

    @Override
    public void onBindViewHolder(@NonNull Placeholder placeholder, final int i) {
        int point=0;
        placeholder.tvWonEndTime.setText(finalList.get(i).getTime());
        placeholder.tvWonDate.setText(finalList.get(i).getDate());
        for (int j=0;j<pointlist.size();j++){
            if(finalList.get(i).getDate().equals(pointlist.get(j).getDate()) && finalList.get(i).getTime().equals(pointlist.get(j).getEndTime())){
               point+= pointlist.get(j).getPoint();
            }
        }
        placeholder.tvWonPoint.setText(String.valueOf(point));
       placeholder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(con, WonListDetailsActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("wonlisttime",finalList.get(i).getTime());
                intent.putExtra("wonlistdate",finalList.get(i).getDate());
                con.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return finalList.size();
    }

    public class Placeholder extends RecyclerView.ViewHolder {

        TextView tvWonEndTime,tvWonDate,tvWonPoint;
        LinearLayout linearLayout;

        public Placeholder(@NonNull View itemView) {
            super(itemView);
            tvWonEndTime=itemView.findViewById(R.id.won_endTime);
            tvWonDate=itemView.findViewById(R.id.won_date);
            tvWonPoint=itemView.findViewById(R.id.won_pont);
            linearLayout=itemView.findViewById(R.id.won_linear);
        }
    }
}
