package com.example.useraccountmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.example.useraccountmanagement.CreditListActivity;
import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.User;

import java.util.ArrayList;
import java.util.List;

public class DebtOfUserAdapter extends RecyclerView.Adapter<DebtOfUserAdapter.Placeholder> implements Filterable {
    Context context;
    List<User> list;
    List<User> originalList;
    String token;

    public DebtOfUserAdapter(Context context, List<User> list, String token) {
        this.context=context;
        this.list=list;
        this.originalList=new ArrayList<>(list);
        this.token=token;
    }

    @NonNull
    @Override
    public Placeholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_debt_of_agent,viewGroup,false);
        return new Placeholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Placeholder placeholder, int i) {
        final String name=list.get(i).getName();
        final String email=list.get(i).getEmail();
        final int points= (int) list.get(i).getPoints();
        placeholder.othagentname.setText(name);
        placeholder.othagentname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CreditListActivity.class);
                intent.putExtra("Agentname",name);
                intent.putExtra("Agentemail",email);
                intent.putExtra("AgentPoint",points);
                intent.putExtra("token",token);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Placeholder extends RecyclerView.ViewHolder {
        TextView othagentname;
        public Placeholder(@NonNull View itemView) {
            super(itemView);
            othagentname=itemView.findViewById(R.id.doAagentname);

        }
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }
    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<User> filteredProduct = new ArrayList<>();
            if (charSequence == null && charSequence.length() == 0){
                filteredProduct.addAll(originalList);
            }else {
                String pattern = charSequence.toString().toLowerCase().trim();
                for (User user : originalList){
                    if (user.getName().toLowerCase().trim().contains(pattern)){
                        filteredProduct.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredProduct;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
