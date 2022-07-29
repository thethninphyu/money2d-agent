package com.example.useraccountmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.useraccountmanagement.adapter.AgentUserListsAdapter;
import com.example.useraccountmanagement.model.AgentUser;
import com.example.useraccountmanagement.model.AgentUserLists;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentUserListsActivity extends AppCompatActivity {

    String email,token;
    RecyclerView recyclerView;
    ApiRequest apiRequest;
    TextView noti;
    ImageView imageView;
    AgentUserListsAdapter agentUserListsAdapter;
    ArrayList<AgentUser> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_user_lists);

        getSupportActionBar().setTitle("Agent User Lists");

        recyclerView=findViewById(R.id.agentuserrecycler);
        noti=findViewById(R.id.agent_user_noti);
        imageView=findViewById(R.id.agent_user_logo);

        token=getIntent().getStringExtra("token");
        email=getIntent().getStringExtra("Agentemail");
        apiRequest= ApiUtil.getApiRequest();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        apiRequest.agentuserlists(token,email).enqueue(new Callback<AgentUserLists>() {
            @Override
            public void onResponse(Call<AgentUserLists> call, Response<AgentUserLists> response) {
                if(response.isSuccessful()){
                    lists=new ArrayList<>();
                    lists.addAll(response.body().getUser());
                    if(lists.size()==0){
                        noti.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                    }else {
                        agentUserListsAdapter = new AgentUserListsAdapter(AgentUserListsActivity.this, lists);
                        recyclerView.setAdapter(agentUserListsAdapter);
                    }
                }else Toast.makeText(AgentUserListsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AgentUserLists> call, Throwable t) {
                Toast.makeText(AgentUserListsActivity.this, "No Network", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
