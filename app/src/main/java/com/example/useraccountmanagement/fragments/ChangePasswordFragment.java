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
import android.widget.EditText;
import android.widget.Toast;


import com.example.useraccountmanagement.R;
import com.example.useraccountmanagement.model.ChangePassword;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment implements View.OnClickListener, TextWatcher {
    EditText edtOldPass,edtNewPass,edtConfirm;
    Button btnchangePass;
    String token;
    String youremail;
    View view;



    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Change Password");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_change_password, container, false);
        edtOldPass=view.findViewById(R.id.ed_oldPass);
        edtNewPass=view.findViewById(R.id.edt_newPass);
        edtConfirm=view.findViewById(R.id.edt_confirmPass);
        btnchangePass=view.findViewById(R.id.btn_changePass);

        token =getActivity().getIntent().getExtras().getString("token");
        youremail=getActivity().getIntent().getExtras().getString("email");

        //  Log.d("email::::",youremail);

        edtOldPass.addTextChangedListener(this);
        edtNewPass.addTextChangedListener(this);
        edtConfirm.addTextChangedListener(this);

        btnchangePass.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

        String oldPassword=edtOldPass.getText().toString();
        String newPassword=edtNewPass.getText().toString();
        String confirm=edtConfirm.getText().toString();

        if (confirm.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()){
            Toast.makeText(getContext(),"Your old or password is empty!",Toast.LENGTH_LONG).show();
            return;
        }else if(newPassword.equals(confirm)) {
            ApiRequest apiRequest = ApiUtil.getApiRequest();
            apiRequest.changePassword(youremail, oldPassword, newPassword, confirm, token).enqueue(new Callback<ChangePassword>() {
                @Override
                public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                    if (!response.body().getError()) {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ChangePassword> call, Throwable t) {
                    Toast.makeText(getContext(), "Bad connection!", Toast.LENGTH_LONG).show();
                }
            });
        }else
            Toast.makeText(getContext(), "Passwor doest not match!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str=String.valueOf(s);
        if (str.length()>=0 && str.contains(" ")){
            if (str.equals(edtOldPass.getText().toString())){
                edtOldPass.setText(edtOldPass.getText().toString().replaceAll(" ",""));
                edtOldPass.setSelection(edtOldPass.getText().length());
            }else if (str.equals(edtNewPass.getText().toString())){
                edtNewPass.setText(edtNewPass.getText().toString().replaceAll(" ",""));
                edtNewPass.setSelection(edtNewPass.getText().length());
            }else if (str.equals(edtConfirm.getText().toString())){
                edtConfirm.setText(edtConfirm.getText().toString().replaceAll(" ",""));
                edtConfirm.setSelection(edtConfirm.getText().length());
            }
            Toast.makeText(getContext(), "Space is not allowed!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}


