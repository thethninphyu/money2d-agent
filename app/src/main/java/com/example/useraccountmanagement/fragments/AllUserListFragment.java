package com.example.useraccountmanagement.fragments;

import android.content.Intent;
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


import com.example.useraccountmanagement.AgentPasswordChange;
import com.example.useraccountmanagement.AgentUserListsActivity;
import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.adapter.AgentEditAdapter;
import com.example.useraccountmanagement.adapter.UserListAdapter;
import com.example.useraccountmanagement.model.Block;
import com.example.useraccountmanagement.model.Give;
import com.example.useraccountmanagement.model.Take;
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

public class AllUserListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ArrayList<User> arrayList;
    UserListAdapter agentListAdapter;
    String token,email;
    ApiRequest apiRequest;
    View view;
    public RecyclerView.RecycledViewPool recycledViewPool=new RecyclerView.RecycledViewPool();

    public AllUserListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_all_agent_list, container, false);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        recyclerView=view.findViewById(R.id.recyclerview);

        token=getActivity().getIntent().getExtras().getString("token");
        email=getActivity().getIntent().getExtras().getString("email");

        apiRequest= ApiUtil.getApiRequest();

        swipeRefreshLayout.setRefreshing(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        agentListAdapter=new UserListAdapter(getContext(),arrayList,
                token);
        recyclerView.setHasFixedSize(true);
        parseData();
        swipeRefreshLayout.setOnRefreshListener(this);
        agentListAdapter.setAdapterIteamClickListener(new UserListAdapter.CVItemClick() {
            @Override
            public void agentEdit(String email,int points, RecyclerView editRecyclerView, View arrow) {
                if(editRecyclerView.getVisibility()==View.GONE){
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(editRecyclerView.getContext(),LinearLayoutManager.VERTICAL,false);
                    linearLayoutManager.setInitialPrefetchItemCount(1);
                    AgentEditAdapter editAdapter=new AgentEditAdapter(view.getContext(),email,points);
                    editRecyclerView.setLayoutManager(linearLayoutManager);
                    editRecyclerView.setAdapter(editAdapter);
                    editRecyclerView.setHasFixedSize(true);
                    editRecyclerView.setRecycledViewPool(recycledViewPool);
                    editRecyclerView.setVisibility(View.VISIBLE);
                    arrow.setVisibility(View.VISIBLE);

                    editAdapter.setAdapterIteamClickListener(new AgentEditAdapter.CVItemClick() {
                        @Override
                        public void onItemClick(String email, int points, Boolean check, String status) {
                            switch (status) {
                                case "Take":
                                    apiRequest.takepoints(token,
                                            email, points).enqueue(new Callback<Take>() {
                                        @Override
                                        public void onResponse(Call<Take> call, Response<Take> response) {
                                            parseData();
                                        }
                                        @Override
                                        public void onFailure(Call<Take> call, Throwable t) {

                                        }
                                    });
                                    break;
                                case "Give":
                                    apiRequest.givepoints(token,
                                            email, points,check).enqueue(new Callback<Give>() {
                                        @Override
                                        public void onResponse(Call<Give> call, Response<Give> response) {
                                            parseData();
                                        }
                                        @Override
                                        public void onFailure(Call<Give> call, Throwable t) {

                                        }
                                    });
                                    break;
                            }
                        }

                        @Override
                        public void onBlockList(String email, int i) {
                            apiRequest.blockAgent(token,email).enqueue(new Callback<Block>() {
                                @Override
                                public void onResponse(Call<Block> call, Response<Block> response) {
                                    if (!response.body().getError()){
                                        parseData();
                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getContext(), "Something Wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Block> call, Throwable t) {
                                    Toast.makeText(getContext(), "Bad connection!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onChangeClick(String email) {
                            Intent intent=new Intent(getContext(), AgentPasswordChange.class);
                            intent.putExtra("Agentemail",email);
                            intent.putExtra("token",token);
                            getActivity().startActivity(intent);

                        }

                        @Override
                        public void onUserList(String email) {
                            Intent intent=new Intent(getContext(), AgentUserListsActivity.class);
                            intent.putExtra("Agentemail",email);
                            intent.putExtra("token",token);
                            getActivity().startActivity(intent);

                        }
                    });
                }else {
                    editRecyclerView.setVisibility(View.GONE);
                    arrow.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("All User List");
    }

    private void parseData() {
        apiRequest.listOfUser(token).enqueue(new Callback<Userlist>() {
            @Override
            public void onResponse(Call<Userlist> call, Response<Userlist> response) {
                if (response.isSuccessful()){
                    ArrayList<User> updateList=new ArrayList<>();
                    for (int i=0;i<response.body().getUser().size();i++){
                        if (response.body().getUser().get(i).getStatus().equals("BLOCK")){
                            continue;
                        }else {
                            updateList.add(response.body().getUser().get(i));
                            Collections.sort(updateList, new Comparator<User>() {
                                @Override
                                public int compare(User o1, User o2) {
                                    if(o1.getName().compareToIgnoreCase(o2.getName()) > 0) return 1;
                                    if(o1.getName().compareToIgnoreCase(o2.getName()) < 0) return -1;
                                    return 0;
                                }
                            });
                        }
                    }
                    arrayList=new ArrayList<>();
                    arrayList.addAll(updateList);
                    agentListAdapter.setUpdateList(arrayList);
                    recyclerView.setAdapter(agentListAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    Toast.makeText(getContext(),"Something wrong!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Userlist> call, Throwable t) {
                Toast.makeText(getContext(), "No network!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        parseData();
        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
    }
}
