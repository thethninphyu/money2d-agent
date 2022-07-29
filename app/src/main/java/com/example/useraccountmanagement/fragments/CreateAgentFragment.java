package com.example.useraccountmanagement.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.RegisterActivity;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAgentFragment extends Fragment implements TextWatcher, View.OnClickListener {
    EditText edname,edpass,edpoints,edphone;
    CheckBox checkPaid;
    Button btnCreateAgent;
    String token;
    ApiRequest apiRequest;
    RegisterActivity registerActivity;
    int total;
    View view;



    public CreateAgentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_create_agent, container, false);
        edname=view.findViewById(R.id.ed_agentname);
        edpass=view.findViewById(R.id.ed_agentPass);
        edpoints=view.findViewById(R.id.ed_agentPoint);
        edphone=view.findViewById(R.id.ed_agentPhone);
        checkPaid=view.findViewById(R.id.agent_paidCheck);
        btnCreateAgent=view.findViewById(R.id.btn_agentCreate);

        token=getActivity().getIntent().getExtras().getString("token");
        apiRequest= ApiUtil.getApiRequest();

        edname.addTextChangedListener(this);
        edpass.addTextChangedListener(this);

        btnCreateAgent.setOnClickListener(this);


/*
        apiRequest.gettotalpoints(token).enqueue(new Callback<Ownerpoints>() {
            @Override
            public void onResponse(Call<Ownerpoints> call, Response<Ownerpoints> response) {
                if (response.isSuccessful()){
                    total=response.body().getTotal();
                }else
                    Toast.makeText(getContext(),response.message(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Ownerpoints> call, Throwable t) {
                Toast.makeText(getContext(),"No Network",Toast.LENGTH_LONG).show();

            }
        });
*/
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create User");
    }

    @Override
    public void onClick(View v) {
        if (edname.length()==0 || edpass.length()==0 || edphone.length()==0 ||
                edpoints.length()==0){
            Toast.makeText(getContext(),"Empty data!",Toast.LENGTH_LONG).show();
            return;
        }

        String name=edname.getText().toString();
        String pass=edpass.getText().toString();
        int points= Integer.parseInt(edpoints.getText().toString());

        String phone=edphone.getText().toString();
        Boolean paid;

        String email=name+"@real2d.com";

        if (checkPaid.isChecked()){
            paid=true;
        }else {
            paid=false;
        }

        token=getActivity().getIntent().getExtras().getString("token");

        registerActivity=new RegisterActivity(name,email,pass,phone,"NEED",points,paid);


        apiRequest.agentRegister(registerActivity,token).enqueue(new Callback<RegisterActivity>() {
            @Override
            public void onResponse(Call<RegisterActivity> call, Response<RegisterActivity> response) {
                if (response.isSuccessful()){
                    Toast.makeText(getContext(),response.body().getMessage(),Toast.LENGTH_LONG).show();

                }else {

                    Toast.makeText(getContext(),"Something wrong!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterActivity> call, Throwable t) {
                Toast.makeText(getContext(),"Bad connection!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str= String.valueOf(s);
        if (edname.length()>0 && Character.isWhitespace(s.charAt(start))){
            if (str.equals(edname.getText().toString())){
                edname.setText(edname.getText().toString().replaceAll(" ",""));
                edname.setSelection(edname.getText().length());
            }else if (edpass.length()>0 && str.equals(edpass.getText().toString())){
                edpass.setText(edpass.getText().toString().replaceAll(" ",""));
                edpass.setSelection(edpass.getText().length());
            }
            Toast.makeText(getContext(), "Space is not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}

