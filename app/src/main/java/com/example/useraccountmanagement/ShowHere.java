package com.example.useraccountmanagement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;

import com.example.useraccountmanagement.fragments.AllUserListFragment;
import com.example.useraccountmanagement.fragments.BetListsFragment;
import com.example.useraccountmanagement.fragments.BettingListDetailsFragment;
import com.example.useraccountmanagement.fragments.ChangePasswordFragment;
import com.example.useraccountmanagement.fragments.CreateAgentFragment;
import com.example.useraccountmanagement.fragments.DebtOfUserFragment;
import com.example.useraccountmanagement.fragments.FinalNumberFragment;
import com.example.useraccountmanagement.fragments.ListOfUnblockUserFragment;
import com.example.useraccountmanagement.fragments.LivePageFragment;
import com.example.useraccountmanagement.fragments.WonListFragment;



public class ShowHere extends AppCompatActivity {
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        assert getSupportActionBar() == null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id=getIntent().getExtras().getInt("ID",0);
        token=getIntent().getExtras().getString("token");
        switch (id){
            case R.id.liveStream:
                replaceFragment(new LivePageFragment());
                break;
            case R.id.betlist:
                replaceFragment(new BetListsFragment());
                break;
            case R.id.betting_lists_details:
                replaceFragment(new BettingListDetailsFragment());
                break;

            case R.id.change_password:
                replaceFragment(new ChangePasswordFragment());
                break;

            case R.id.create_user:
                replaceFragment(new CreateAgentFragment());
                break;
            case R.id.all_user_list:
                replaceFragment(new AllUserListFragment());
                break;
            case R.id.unblock_user:
                replaceFragment(new ListOfUnblockUserFragment());
                break;

            case R.id.credit_list:
                replaceFragment(new DebtOfUserFragment());
                break;

            case R.id.won_list:
                replaceFragment(new WonListFragment());
                break;
            case R.id.final_no:
                replaceFragment(new FinalNumberFragment());
                break;



        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ScrollView dash=findViewById(R.id.dashScrollView);
        ScrollView frame=findViewById(R.id.frameScrollView);
        if (frame.getVisibility()== View.GONE){
            dash.setVisibility(View.GONE);
            frame.setVisibility(View.VISIBLE);
            ft.replace(R.id.rel,fragment);
        }
        ft.commit();
    }
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}