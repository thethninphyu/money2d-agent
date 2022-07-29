package com.example.useraccountmanagement.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.adapter.BettingListAdapter;
import com.example.useraccountmanagement.model.AgentBetlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BetListsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    BettingListAdapter bettingAdapter;
    String token;
    List<AgentBetlist> list;
    SwipeRefreshLayout swipeRefreshLayout;
    ApiRequest apiRequest;

    public BetListsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bet, container, false);

        recyclerView = view.findViewById(R.id.bettingListRecycler);
        swipeRefreshLayout = view.findViewById(R.id.bet_list_swipe);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Betting Summary");

        token=getActivity().getIntent().getStringExtra("token");
        apiRequest = ApiUtil.getApiRequest();

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                int spanCount = 2;
                int spacing = 10;//spacing between views in grid

                if (position >= 0) {
                    int column = position % spanCount; // item column

                    outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                    outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                    if (position < spanCount) { // top edge
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing; // item bottom
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });
        recyclerView.setHasFixedSize(true);

        betListData();

        return view;
    }

    private void betListData() {
        apiRequest.getBetList(token).enqueue(new Callback<List<AgentBetlist>>() {
            @Override
            public void onResponse(Call<List<AgentBetlist>> call, Response<List<AgentBetlist>> response) {
                if (response.isSuccessful()) {
                    list = new ArrayList<>();
                    list.addAll(response.body());
                    Collections.sort(list, new Comparator<AgentBetlist>() {
                        @Override
                        public int compare(AgentBetlist o1, AgentBetlist o2) {
                            if(o1.getNumber().compareToIgnoreCase(o2.getNumber()) > 0) return 1;
                            if(o1.getNumber().compareToIgnoreCase(o2.getNumber()) < 0) return -1;
                            return 0;
                        }
                    });

                    if (list.size()!=0){
                        bettingAdapter = new BettingListAdapter(getContext(), list);
                        recyclerView.setAdapter(bettingAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }else
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<AgentBetlist>> call, Throwable t) {
                Toast.makeText(getContext(), "No Network", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onRefresh() {
        betListData();
        Toast.makeText(getContext(), "Update!", Toast.LENGTH_SHORT).show();
    }
}


