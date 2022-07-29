package com.example.useraccountmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.useraccountmanagement.BetSummary;
import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.AgentBetlist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BettingListAdapter extends RecyclerView.Adapter<BettingListAdapter.MyViewHolder> {
    Context context;
    List<AgentBetlist> list;
    ArrayList<String> duplicateUser,myNumber;
    ArrayList<BetSummary> finalList;

    String betnum;
    public BettingListAdapter(Context context, List<AgentBetlist> list) {
        this.context=context;
        this.list=list;

        ArrayList<String> duplicateNumber=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            duplicateNumber.add(list.get(i).getNumber());
        }

        myNumber=new ArrayList<>();
        for (int i=0;i<duplicateNumber.size();i++) {
            if (myNumber.contains(duplicateNumber.get(i))){
                continue;
            }else {
                myNumber.add(duplicateNumber.get(i));
            }
        }

        finalList=new ArrayList<>();
        duplicateUser=new ArrayList<>();
        BetSummary summary=new BetSummary();

        for (int z=0;z<myNumber.size();z++){
            int points=0;
            for (int j=0;j<list.size();j++) {
                if (list.get(j).getNumber().equals(myNumber.get(z))) {
                    points+=list.get(j).getPoints();
                    duplicateUser.add(list.get(j).getUser());
                }else {
                    continue;
                }
            }
            HashSet<String > set=new HashSet<>(duplicateUser);
            ArrayList<String > users=new ArrayList(set);
           finalList.add(new BetSummary(points,users.size(),myNumber.get(z)));
        }
    }

    @NonNull
    @Override
    public BettingListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_betting_list,viewGroup,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BettingListAdapter.MyViewHolder myViewHolder, final int i) {
        myViewHolder.number.setText(finalList.get(i).getNumber());
        myViewHolder.point.setText(String.valueOf(finalList.get(i).getPoints()));
        myViewHolder.user.setText(String.valueOf(finalList.get(i).getUser()));

    }

    @Override
    public int getItemCount() {
        return myNumber.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number,point,user;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.bet_list_number);
            user=itemView.findViewById(R.id.bet_list_user);
            point=itemView.findViewById(R.id.bet_list_point);
        }
    }
}
