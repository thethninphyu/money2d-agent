package com.example.useraccountmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.useraccountmanagement.BetListDetailsActivity;
import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.AgentBetlist;

import java.util.List;

public class BettingListsDetailsAdapter extends RecyclerView.Adapter<BettingListsDetailsAdapter.MyViewHolder> {
    Context context;
    List<AgentBetlist> list;
    CardView cardView;
    String token;
    public BettingListsDetailsAdapter(Context context, List<AgentBetlist> list, String token) {
        this.context=context;
        this.list=list;
        this.token=token;
    }

    @NonNull
    @Override
    public BettingListsDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_betting_list_details,viewGroup,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BettingListsDetailsAdapter.MyViewHolder myViewHolder, final int i) {
           final String name=list.get(i).getUser();
           final String number=list.get(i).getNumber();
            myViewHolder.bettingListAgentName.setText(name);
            myViewHolder.bettingListNumber.setText(number);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, BetListDetailsActivity.class);
                    intent.putExtra("index",i);
                    intent.putExtra("token",token);
                    context.startActivity(intent);
                }
            });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bettingListAgentName,bettingListNumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardView);
            bettingListAgentName=itemView.findViewById(R.id.bettingListUserName);
            bettingListNumber=itemView.findViewById(R.id.bettingListNumber);
        }
    }
}
