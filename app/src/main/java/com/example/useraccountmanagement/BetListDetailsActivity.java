package com.example.useraccountmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.example.useraccountmanagement.model.AgentBetlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BetListDetailsActivity extends AppCompatActivity {
    TextView number,points,agentname,user,endTime,date;
    String token;
    ArrayList<AgentBetlist> allList;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        number=findViewById(R.id.doNumber);
        points=findViewById(R.id.doPoints);
        agentname=findViewById(R.id.doAgent);
        user=findViewById(R.id.doUser);
        endTime=findViewById(R.id.doEndTime);
        date=findViewById(R.id.doDate);

        getSupportActionBar().setTitle("Bet List Details");

        index=getIntent().getExtras().getInt("index");
        token=getIntent().getExtras().getString("token");

        ApiRequest apiRequest = ApiUtil.getApiRequest();
        apiRequest.getBetList(token).enqueue(new Callback<List<AgentBetlist>>() {
            @Override
            public void onResponse(Call<List<AgentBetlist>> call, Response<List<AgentBetlist>> response) {
                if (response.isSuccessful()){
                    allList=new ArrayList<>();
                    allList.addAll(response.body());
                    number.setText(allList.get(index).getNumber());
                    points.setText(allList.get(index).getPoints().toString());
                    user.setText(allList.get(index).getUser());
                    endTime.setText(allList.get(index).getEndTime());
                    date.setText(allList.get(index).getDate());
                }
            }

            @Override
            public void onFailure(Call<List<AgentBetlist>> call, Throwable t) {

            }
        });
    }
}

