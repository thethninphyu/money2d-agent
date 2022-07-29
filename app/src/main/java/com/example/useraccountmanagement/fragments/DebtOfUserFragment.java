package com.example.useraccountmanagement.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.adapter.DebtOfUserAdapter;
import com.example.useraccountmanagement.model.User;
import com.example.useraccountmanagement.model.Userlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DebtOfUserFragment extends Fragment {
    RecyclerView recyclerView;
    DebtOfUserAdapter debtOfAgentAdapter;
    String token;
    View view;
    ApiRequest apiRequest;

    public DebtOfUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_debt_of_agent, container, false);
        setHasOptionsMenu(true);
        token=getActivity().getIntent().getStringExtra("token");

        recyclerView=view.findViewById(R.id.doARecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        apiRequest= ApiUtil.getApiRequest();
        getAgentList();
        return view;
    }

    private void getAgentList() {
        apiRequest.listOfUser(token).enqueue(new Callback<Userlist>() {
            @Override
            public void onResponse(Call<Userlist> call, Response<Userlist> response) {
                if(response.isSuccessful()){
                    List<User> list=response.body().getUser();
                    debtOfAgentAdapter =new DebtOfUserAdapter(getContext(),list,token);
                    recyclerView.setAdapter(debtOfAgentAdapter);
                }else
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Userlist> call, Throwable t) {
                Toast.makeText(getContext(), "No Network", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater=getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.doa_search,menu);
        MenuItem searchItem=menu.findItem(R.id.search_item);
        SearchView searchView= (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                debtOfAgentAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Debt Of User");
    }

    @Override
    public void onResume() {
        super.onResume();
        getAgentList();
    }
}
