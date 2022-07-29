package com.example.useraccountmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.useraccountmanagement.model.Credit;
import com.example.useraccountmanagement.model.Take;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditListActivity extends AppCompatActivity {

    TextView agentname,paid,unpaid,amount,credit;
    Button btntake;
    ArrayList<Credit> arrayList;
    int give=0,take=0,creditamount=0,currentPoints=0;
    ApiRequest apiRequest;
    String name,email;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_list);
        agentname=findViewById(R.id.doAagent);
        paid=findViewById(R.id.doApaid);
        unpaid=findViewById(R.id.doAunpaid);
        amount=findViewById(R.id.doAamount);
        btntake=findViewById(R.id.doAtake);
        credit=findViewById(R.id.doAcredit);

        getSupportActionBar().setTitle("CreditList Details");
        token=getIntent().getExtras().getString("token");

        name=getIntent().getStringExtra("Agentname");
        email=getIntent().getStringExtra("Agentemail");
        currentPoints=getIntent().getIntExtra("AgentPoint",0);
        agentname.setText(name);

        apiRequest= ApiUtil.getApiRequest();
        creditList();


        btntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiRequest.takepoints(token,
                        email,give-take).enqueue(new Callback<Take>() {
                    @Override
                    public void onResponse(Call<Take> call, Response<Take> response) {
                        if(response.isSuccessful()) {
                            currentPoints=currentPoints-(give-take);
                            credit.setText("0");
                            amount.setText(String.valueOf(currentPoints));
                            btntake.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreditListActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(CreditListActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Take> call, Throwable t) {
                        Toast.makeText(CreditListActivity.this, "No Network", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void creditList() {
        apiRequest.creditlist(token,name).enqueue(new Callback<List<Credit>>() {
            @Override
            public void onResponse(Call<List<Credit>> call, Response<List<Credit>> response) {
                if(response.isSuccessful()) {
                    arrayList=new ArrayList<>();
                    arrayList.addAll(response.body());
                    for(int i=0;i<arrayList.size();i++){
                        if(arrayList.get(i).getStatus().equals("GIVE")){
                            give+=arrayList.get(i).getAmount();
                        }else if(arrayList.get(i).getStatus().equals("TAKE")){
                            take+=arrayList.get(i).getAmount();
                        }
                    }
                    creditamount=take-give;
                    paid.setText(String.valueOf(take));
                    unpaid.setText(String.valueOf(give));
                    credit.setText(String.valueOf(creditamount));
                    amount.setText(String.valueOf(currentPoints));

                    if (currentPoints>creditamount && give-take!=0){
                        btntake.setVisibility(View.VISIBLE);
                    }

                }
                else Toast.makeText(CreditListActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Credit>> call, Throwable t) {
                Toast.makeText(CreditListActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
