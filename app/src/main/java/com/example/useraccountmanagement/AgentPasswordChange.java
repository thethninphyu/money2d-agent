package com.example.useraccountmanagement;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgentPasswordChange extends AppCompatActivity implements View.OnClickListener {
    EditText agentNewPass,agentConfirmPass;
    Button btnAgentChangePass;
    String token,email;
    ApiRequest apiRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_password_change);

        token=getIntent().getStringExtra("token");
        email=getIntent().getStringExtra("Agentemail");
        getSupportActionBar().setTitle("Agent Password Change");

        apiRequest= ApiUtil.getApiRequest();

        agentNewPass=findViewById(R.id.agentNewPass);
        agentConfirmPass=findViewById(R.id.agentConfirmPass);
        btnAgentChangePass=findViewById(R.id.btnAgentChangePass);

        btnAgentChangePass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (agentNewPass.length()==0 || agentConfirmPass.length()==0){
            Toast.makeText(this, "Empty data!", Toast.LENGTH_SHORT).show();
            return;
        }else if(agentNewPass.getText().toString().equals(agentConfirmPass.getText().toString())){
             apiRequest.changePassword(token,email,agentNewPass.getText().toString()).enqueue(new Callback<AgentPasswordChange>() {
                 @Override
                 public void onResponse(Call<AgentPasswordChange> call, Response<AgentPasswordChange> response) {
                     if(response.isSuccessful()) {
                         Toast.makeText(AgentPasswordChange.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                     }else
                         Toast.makeText(AgentPasswordChange.this, response.message(), Toast.LENGTH_SHORT).show();
                 }

                 @Override
                 public void onFailure(Call<AgentPasswordChange> call, Throwable t) {
                     Toast.makeText(AgentPasswordChange.this, "No Network", Toast.LENGTH_SHORT).show();
                 }
             });

        }
    }

    private int getMessage() {
        return 0;
    }
}
