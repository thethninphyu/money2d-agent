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
import com.example.useraccountmanagement.adapter.ListAllFinalNumberAdapter;
import com.example.useraccountmanagement.model.Listallfinalnumber;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalNumberFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ApiRequest apiRequest;
    RecyclerView recyclerView;
    ListAllFinalNumberAdapter listAllFinalNumberAdapter;
    ArrayList<Listallfinalnumber> arrayList;
    SwipeRefreshLayout finalSwipe;
    View view;
    String token;
    public FinalNumberFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_final_number, container, false);
        recyclerView=view.findViewById(R.id.final_recycler);
        finalSwipe=view.findViewById(R.id.finalNumberSwipe);

        finalSwipe.setRefreshing(true);
        finalSwipe.setOnRefreshListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        token=getActivity().getIntent().getExtras().getString("token");

        finalNumberLoadData();

        return view;
    }

    private void finalNumberLoadData() {
        apiRequest= ApiUtil.getApiRequest();
        apiRequest.finalnumber(token).enqueue(new Callback<List<Listallfinalnumber>>() {
            @Override
            public void onResponse(Call<List<Listallfinalnumber>> call, Response<List<Listallfinalnumber>> response) {
                if(response.isSuccessful()){
                    arrayList=new ArrayList<>();
                    arrayList.addAll(response.body());
                    listAllFinalNumberAdapter=new ListAllFinalNumberAdapter(getContext(),arrayList);
                    recyclerView.setAdapter(listAllFinalNumberAdapter);
                    finalSwipe.setRefreshing(false);
                }else
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Listallfinalnumber>> call, Throwable t) {
                Toast.makeText(getContext(), "No Network", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Final Number");
    }

    @Override
    public void onRefresh() {
        finalNumberLoadData();
        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
    }
}
