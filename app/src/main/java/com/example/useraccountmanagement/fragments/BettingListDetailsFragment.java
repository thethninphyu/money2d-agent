package com.example.useraccountmanagement.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.adapter.BettingListsDetailsAdapter;
import com.example.useraccountmanagement.model.AgentBetlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BettingListDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    BettingListsDetailsAdapter bettingAdapter;
    String token;
    List<AgentBetlist> list;
    SwipeRefreshLayout swipeRefreshLayout;
    ApiRequest apiRequest;

    public BettingListDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bet_list_details, container, false);

        recyclerView = view.findViewById(R.id.bettingListRecycler);
        swipeRefreshLayout = view.findViewById(R.id.bet_list_details_swipe);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        apiRequest = ApiUtil.getApiRequest();
        token=getActivity().getIntent().getExtras().getString("token");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Betting Details");

        betListDetailsData();
        return view;
    }

    private void betListDetailsData() {
        apiRequest.getBetList(token).enqueue(new Callback<List<AgentBetlist>>() {
            @Override
            public void onResponse(Call<List<AgentBetlist>> call, Response<List<AgentBetlist>> response) {
                if (response.isSuccessful()) {
                    list = new ArrayList<>();
                    list.addAll(response.body());
                    bettingAdapter = new BettingListsDetailsAdapter(getContext(), list,token);
                    recyclerView.setAdapter(bettingAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }else Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<AgentBetlist>> call, Throwable t) {
                Toast.makeText(getContext(), "No Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        betListDetailsData();
        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
    }
}


