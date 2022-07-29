package com.example.useraccountmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;

import com.example.useraccountmanagement.fragments.CreateAgentFragment;
import com.example.useraccountmanagement.fragments.DebtOfUserFragment;


public class BtnShowHere extends AppCompatActivity {
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        assert getSupportActionBar() == null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getExtras().getInt("ID", 0);
        token = getIntent().getExtras().getString("token");
        switch (id) {
            case R.id.bankcardId2:
                replaceFragment(new CreateAgentFragment());
                break;

            case R.id.bankcardId3:
                replaceFragment(new DebtOfUserFragment());
                break;

            }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ScrollView dash = findViewById(R.id.dashScrollView);
        ScrollView frame = findViewById(R.id.frameScrollView);
        if (frame.getVisibility() == View.GONE) {
            dash.setVisibility(View.GONE);
            frame.setVisibility(View.VISIBLE);
            ft.replace(R.id.rel, fragment);
        }
        ft.commit();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}