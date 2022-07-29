package com.example.useraccountmanagement.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.adapter.WonListAdapter;
import com.example.useraccountmanagement.model.Wonlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class WonListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    WonListAdapter adapter;
    ApiRequest apiRequest;
    ArrayList<Wonlist> arrayList;
    View view;
    String token;
    SwipeRefreshLayout wonListSwipe;

    public WonListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_won_list, container, false);

        recyclerView=view.findViewById(R.id.wonRecyclerView);
        wonListSwipe=view.findViewById(R.id.wonList_Swipe);

        wonListSwipe.setRefreshing(true);
        wonListSwipe.setOnRefreshListener(this);

        apiRequest= ApiUtil.getApiRequest();
        token=getActivity().getIntent().getExtras().getString("token");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        parseData();
        return view;
    }

    private void parseData() {
        apiRequest.wonlist(token).enqueue(new Callback<List<Wonlist>>() {
            @Override
            public void onResponse(Call<List<Wonlist>> call, final Response<List<Wonlist>> response) {
                if(response.isSuccessful()){
                    arrayList=new ArrayList<>();
                    arrayList.addAll(response.body());
                    adapter=new WonListAdapter(getContext(),arrayList,token);
                    recyclerView.setAdapter(adapter);
                    wonListSwipe.setRefreshing(false);
                }else
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Wonlist>> call, Throwable t) {
                Toast.makeText(getContext(), "No network!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
            super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Won List");
    }


    @Override
    public void onRefresh() {
        parseData();
        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
    }
}
