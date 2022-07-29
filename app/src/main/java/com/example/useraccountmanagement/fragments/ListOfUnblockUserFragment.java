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
import android.widget.TextView;
import android.widget.Toast;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.adapter.BlockUserListAdapter;
import com.example.useraccountmanagement.model.Unblock;
import com.example.useraccountmanagement.model.User;
import com.example.useraccountmanagement.model.Userlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOfUnblockUserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    TextView tvNoti;
    ArrayList<User> arrayList;
    BlockUserListAdapter adapter;
    String token;
    ApiRequest apiRequest;
    View view;


    public ListOfUnblockUserFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_list_of_unblock_agent, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerview);
        tvNoti=view.findViewById(R.id.notiTV);

        token = getActivity().getIntent().getExtras().getString("token");

        apiRequest= ApiUtil.getApiRequest();

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new BlockUserListAdapter(getContext(), arrayList,
                token);
        recyclerView.setHasFixedSize(true);
        parseData();

        adapter.setAdapterIteamClickListener(new BlockUserListAdapter.CVItemClick() {
            @Override
            public void onItemClick(String email, int i) {
                apiRequest.unblockAgent(token,email).enqueue(new Callback<Unblock>() {
                    @Override
                    public void onResponse(Call<Unblock> call, Response<Unblock> response) {
                        if (!response.body().getError()){
                            parseData();
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Unblock> call, Throwable t) {
                        Toast.makeText(getContext(), "Bad connection!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return view;

    }

    private void parseData() {
        apiRequest.listOfUser(token).enqueue(new Callback<Userlist>() {
            @Override
            public void onResponse(Call<Userlist> call, Response<Userlist> response) {
                if (response.isSuccessful()){
                    ArrayList<User> bAgentList=new ArrayList<>();
                    for (int i=0;i<response.body().getUser().size();i++){
                        if (response.body().getUser().get(i).getStatus().equals("BLOCK")){
                            bAgentList.add(response.body().getUser().get(i));
                            Collections.sort(bAgentList, new Comparator<User>() {
                                @Override
                                public int compare(User o1, User o2) {
                                    if(o1.getName().compareToIgnoreCase(o2.getName()) > 0) return 1;
                                    if(o1.getName().compareToIgnoreCase(o2.getName()) < 0) return -1;
                                    return 0;
                                }
                            });
                        }else {
                            continue;
                        }
                    }
                    arrayList=new ArrayList<>();
                    arrayList.addAll(bAgentList);
                    if (arrayList.size()==0){
                        tvNoti.setVisibility(View.VISIBLE);
                        tvNoti.setText("No one is blocked!");
                    }else {
                        tvNoti.setVisibility(View.INVISIBLE);
                    }
                    adapter.setUpdateList(arrayList);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    Toast.makeText(getContext(), "Something wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Userlist> call, Throwable t) {
                Toast.makeText(getContext(), "no network!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        parseData();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("List Of Unblock User");
    }
}
