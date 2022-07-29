package com.example.useraccountmanagement.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.Listallfinalnumber;

import java.util.ArrayList;

public class ListAllFinalNumberAdapter extends RecyclerView.Adapter<ListAllFinalNumberAdapter.PlaceHolder> {
    Context context;
    ArrayList<Listallfinalnumber> arrayList;
    public ListAllFinalNumberAdapter(Context context, ArrayList<Listallfinalnumber> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_list_all_final_number,viewGroup,false);
        PlaceHolder placeHolder=new PlaceHolder(view);
        return placeHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder placeHolder, int i) {
        placeHolder.number.setText(arrayList.get(i).getNumber());
        placeHolder.time.setText(arrayList.get(i).getEndTime());
        placeHolder.date.setText(arrayList.get(i).getDate());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {
        TextView number,time,date;
        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            number=itemView.findViewById(R.id.final_number);
            time=itemView.findViewById(R.id.final_end_time);
            date=itemView.findViewById(R.id.final_date);
        }
    }
}
