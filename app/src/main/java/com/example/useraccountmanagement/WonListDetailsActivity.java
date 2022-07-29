package com.example.useraccountmanagement;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.example.useraccountmanagement.adapter.WonListDetailsAdapter;
import com.example.useraccountmanagement.model.Wonlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WonListDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView detailrecycler;
    WonListDetailsAdapter wonListDetailsAdapter;
    ApiRequest apiRequest;
    ArrayList<Wonlist> arrayList,list;
    String token,time,date;
    SwipeRefreshLayout wonListSwipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won_list_details);

        detailrecycler=findViewById(R.id.won_detail_recycler);
        wonListSwipe=findViewById(R.id.won_list_details_swipe);

        wonListSwipe.setRefreshing(true);
        wonListSwipe.setOnRefreshListener(this);

        token=getIntent().getExtras().getString("token");
        time=getIntent().getExtras().getString("wonlisttime");
        date=getIntent().getExtras().getString("wonlistdate");
        getSupportActionBar().setTitle("Won List Details");

        apiRequest= ApiUtil.getApiRequest();

        detailrecycler.setLayoutManager(new LinearLayoutManager(this));
        detailrecycler.setHasFixedSize(true);
        parseData();
    }

    private void parseData() {
        apiRequest.wonlist(token).enqueue(new Callback<List<Wonlist>>() {
            @Override
            public void onResponse(Call<List<Wonlist>> call, final Response<List<Wonlist>> response) {
                if(response.isSuccessful()){
                    arrayList=new ArrayList<>();
                    arrayList.addAll(response.body());
                    list=new ArrayList<>();
                    for (int i=0;i<arrayList.size();i++){
                        if (arrayList.get(i).getEndTime().equals(time) && arrayList.get(i).getDate().equals(date)){
                            list.add(arrayList.get(i));
                        }else {
                            continue;
                        }
                    }
                    wonListDetailsAdapter=new WonListDetailsAdapter(WonListDetailsActivity.this,list);
                    detailrecycler.setAdapter(wonListDetailsAdapter);
                    wonListSwipe.setRefreshing(false);
                }else Toast.makeText(WonListDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Wonlist>> call, Throwable t) {
                Toast.makeText(WonListDetailsActivity.this, "No network!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        parseData();
        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
    }
}
