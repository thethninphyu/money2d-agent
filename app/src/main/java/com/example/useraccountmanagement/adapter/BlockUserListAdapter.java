package com.example.useraccountmanagement.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.User;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;


import java.util.List;

public class BlockUserListAdapter extends RecyclerView.Adapter<BlockUserListAdapter.PlaceHolder> {
    Context con;
    String token;
    List<User> list;
    CVItemClick cvItemClick;
    Dialog dialog;

    public BlockUserListAdapter(Context con, List<User> list, String token) {
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
        View v= LayoutInflater.from(con).inflate(R.layout.custom_block_agent_list,viewGroup,false);
        PlaceHolder p=new PlaceHolder(v);
        return p;
    }

    @Override
    public void onBindViewHolder(@NonNull final PlaceHolder placeHolder, final int i) {
        final ApiRequest apiRequest= ApiUtil.getApiRequest();
        placeHolder.tvBAgentname.setText(list.get(i).getName());

        placeHolder.btnUnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertdialog=new AlertDialog.Builder(con);
                alertdialog.setTitle("Unblock");
                alertdialog.setMessage("Are you want to unblock!");
                alertdialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cvItemClick.onItemClick(list.get(i).getEmail(),placeHolder.getAdapterPosition());
                    }
                });
                alertdialog.setNeutralButton("Cancle",null);
                alertdialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PlaceHolder extends RecyclerView.ViewHolder {

        TextView tvBAgentname;
        Button btnUnBlock;

        public PlaceHolder(@NonNull View itemView) {
            super(itemView);
            tvBAgentname=itemView.findViewById(R.id.block_agentname);
            btnUnBlock=itemView.findViewById(R.id.btnUnBlock);

        }
    }

    public interface CVItemClick{
        public void onItemClick(String email, int i);
    }
    public void setAdapterIteamClickListener(CVItemClick cvItemClick){
        this.cvItemClick=cvItemClick;
    }
}
