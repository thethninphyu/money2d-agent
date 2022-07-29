package com.example.useraccountmanagement;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.useraccountmanagement.model.AgentBetlist;
import com.example.useraccountmanagement.model.Credit;
import com.example.useraccountmanagement.model.User;
import com.example.useraccountmanagement.model.Userlist;
import com.example.useraccountmanagement.services.ApiRequest;
import com.example.useraccountmanagement.services.ApiUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    NavigationView navigationView;
    Toolbar toolbar;
    ArrayList<User> arrayList;
    ArrayList<AgentBetlist> betInfo;
    List<Credit> creditArrayList;
    ArrayList<String> liveuser;
    String token,email,name;
    TextView dashLive,tvAdminName,dashCreditPoints
            ,dashTotalAgent,dashAgentTotalPoints
            ,betUser,betPoints,liveBetUser,livePoints;
    ApiRequest apiRequest;
    DrawerLayout drawer;
    CardView bankCardId2,bankCardId3,bankCardId4,bankCardId5;
    int allAgentPoints,allUserPoints,totalGive=0
            ,totalTake=0,totalCredit=0,total=0,
            currentBetPoints=0,currentBetUser=0,livepoints;;
    private int hour,min;
    String date,time;
    Handler handler;
    private Document document;
    private String pageUrl="https://marketdata.set.or.th/mkt/marketsummary.do?language=en&country=US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        dashCreditPoints = findViewById(R.id.dashCreditPoint);

        dashTotalAgent = findViewById(R.id.dashTotalUser);
        dashAgentTotalPoints = findViewById(R.id.dashTotalAgentPoints);

        dashLive = findViewById(R.id.dashLive);
        betUser = findViewById(R.id.dashBetUser);
        betPoints = findViewById(R.id.dashBetPoints);
        liveBetUser = findViewById(R.id.dashLiveUser);
        livePoints = findViewById(R.id.dashLivePoints);


        bankCardId2=findViewById(R.id.bankcardId2);
        bankCardId3=findViewById(R.id.bankcardId3);

        bankCardId2.setOnClickListener(this);
        bankCardId3.setOnClickListener(this);





        setSupportActionBar(toolbar);

        apiRequest= ApiUtil.getApiRequest();
        token=getIntent().getExtras().getString("token");
        email=getIntent().getExtras().getString("email");
        name=getIntent().getExtras().getString("name");

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View v=navigationView.inflateHeaderView(R.layout.header);
        tvAdminName=v.findViewById(R.id.tv_AdminName);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        handler=new Handler();

        tvAdminName.setText(name);
        checkConnection();

        navigationView.setNavigationItemSelectedListener(this);

    }


    private void checkConnection() {

        if (isOnline()){
            handler.postDelayed(runn,650);
            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(MainActivity.this, "You are offline!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOnline() {
        if ((MainActivity.this!=null && !MainActivity.this.isFinishing())){
            ConnectivityManager cm= (ConnectivityManager) MainActivity.this. getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo=cm.getActiveNetworkInfo();
            if (netInfo!=null && netInfo.isConnected()){
                return true;
            }else {
                return false;
            }
        }
        return false;
    }

    public void getUserInfo(){
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
                        }
                    }
                    arrayList=new ArrayList<>();
                    arrayList.addAll(updateList);
                    dashTotalAgent.setText(String.valueOf(arrayList.size()));
                    for (int i=0;i<arrayList.size();i++){
                        allAgentPoints+=arrayList.get(i).getPoints();
                    }
                    dashAgentTotalPoints.setText(String.valueOf(allAgentPoints));
                    allAgentPoints=0;
                }else {
                    Toast.makeText(MainActivity.this,"Bad connection!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Userlist> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Offline!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id == R.id.home){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent i=new Intent(MainActivity.this, ShowHere.class);
        i.putExtra("ID",item.getItemId());
        i.putExtra("token",token);
        i.putExtra("email",email);
        startActivity(i);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllCredit();
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(MainActivity.this, BtnShowHere.class);
        intent.putExtra("ID",v.getId());
        intent.putExtra("token",token);
        startActivity(intent);
    }

    private class ParseLiveData extends AsyncTask<Void,Void,Void> {
        String set,value,updateDateTime;
        @Override
        protected Void doInBackground(Void... voids) {
            if (!isOnline()){
                handler.removeCallbacks(runn);
            }
            try {
                Connection.Response response = Jsoup.connect(pageUrl).method(Connection.Method.GET).execute();
                document=response.parse();
                Elements table=document.select("table[class=table-info]");

                Elements caption=table.select("caption");
                Element span=caption.select("span[class=set-color-gray set-color-graylight]").first();
                updateDateTime=span.nextElementSibling().nextSibling().outerHtml();


                Elements rows=table.select("tr");
                Element firstRow=rows.get(0);
                Element seconRow=rows.get(1);
                Elements titleColumn=firstRow.select("th");
                Elements setColumn=seconRow.select("td");

                if (titleColumn.get(1).text().equals("Last")&& titleColumn.get(7).html().toString().replaceAll("<br>","\n").equals("Value\n(M.Baht)")){
                    set=setColumn.get(1).text();
                    value=setColumn.get(7).text();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);
            if (null==set || null==value){
                Toast.makeText(MainActivity.this, "No more data!,you are offline", Toast.LENGTH_SHORT).show();
                return;
            }else {
                String top=set.substring(set.length()-1,set.length());
                String subValue,base;
                if (value.contains(".")){
                    subValue=value.substring(0,value.indexOf("."));
                    base=subValue.substring(subValue.length()-1,subValue.length());
                }else {
                    if (value.length()>1){
                        base=value.substring(value.length()-1,value.length());
                    }else {
                        base=value;
                    }
                }
                dashLive.setText(top+base);

                TimeZone tz=TimeZone.getTimeZone("GMT+6:30");
                Calendar c= Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                timeformat.setTimeZone(tz);
                String date = dateFormat.format(c.getTime());

                time = timeformat.format(c.getTime());
                hour=Integer.parseInt(time.substring(0,time.indexOf(":")));
                min=Integer.parseInt(time.substring(time.indexOf(":")+1,time.length()));

            }
        }
    }
    Runnable runn=new Runnable() {
        @Override
        public void run() {
            if (hour>=9 && (hour<=12)){
                timeEdit();
            }else {
               // blink();
            }
            if (hour>=13 && hour<=16){
                timeEdit();
            }else {
            }

            blink();
            getBetInfo();
            getUserInfo();
            new ParseLiveData().execute();
            handler.postDelayed(runn, 650);
        }
    };

    private void blink() {
        Runnable blinkRunn = new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (dashLive.getVisibility()== View.VISIBLE){
                            dashLive.setVisibility(View.INVISIBLE);
                        }else {
                            dashLive.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };
        new Thread(blinkRunn).start();
    }
    private void timeEdit() {
        if (hour<12){
        }else if (hour==12){
        }else if (hour==24){
            String otime=time;
            String eTime=otime.replace(String.valueOf(hour),String.valueOf(hour-12));
        }else {
        }
    }

    public int getCredit(String Agentname){
        apiRequest.creditlist(token,Agentname).enqueue(new Callback<List<Credit>>() {
            @ Override
            public void onResponse(Call<List<Credit>> call, Response<List<Credit>> response) {
                if(response.isSuccessful()) {
                    creditArrayList = new ArrayList<>();
                    creditArrayList.addAll(response.body());

                    for (int i = 0; i < creditArrayList.size(); i++) {
                        if (creditArrayList.get(i).getStatus().equals("GIVE")) {
                            totalGive += creditArrayList.get(i).getAmount();
                        }
                        if (creditArrayList.get(i).getStatus().equals("TAKE")) {
                            totalTake += creditArrayList.get(i).getAmount();
                        }
                        }
                        total = totalTake - totalGive;
                       dashCreditPoints.setText(String.valueOf(total));

                      }else{
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<List<Credit>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
            }

        });
        totalGive=0;
        totalTake=0;
        return total;
    }

   public void getAllCredit() {
       apiRequest.listOfUser(token).enqueue(new Callback<Userlist>() {
           @Override
           public void onResponse(Call<Userlist> call, Response<Userlist> response) {
               if (response.isSuccessful()) {
                   List<User> list = response.body().getUser();

                   for (int i = 0; i < list.size(); i++) {
                       String agentName = list.get(i).getName();
                       totalCredit += getCredit(agentName);
                   }
                   dashCreditPoints.setText(String.valueOf(totalCredit));
               } else
                   Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFailure(Call<Userlist> call, Throwable t) {
               Toast.makeText(MainActivity.this, "No Network", Toast.LENGTH_SHORT).show();
           }
       });
   }
    public void getBetInfo () {
        apiRequest.getBetList(token).enqueue(new Callback<List<AgentBetlist>>() {
            @Override
            public void onResponse(Call<List<AgentBetlist>> call, Response<List<AgentBetlist>> response) {
                if (response.isSuccessful()) {
                    betInfo = new ArrayList<>();
                    betInfo.addAll(response.body());

                    ArrayList<String> number = new ArrayList<>();
                    liveuser = new ArrayList<>();
                    for (int i = 0; i < betInfo.size(); i++) {
                        if (dashLive.getText().toString().equals(betInfo.get(i).getNumber())) {
                            liveuser.add(betInfo.get(i).getUser());
                            livepoints += betInfo.get(i).getPoints();
                        }
                        currentBetPoints += betInfo.get(i).getPoints();
                        if (number.contains(betInfo.get(i).getNumber())) {
                            continue;
                        } else {
                            number.add(betInfo.get(i).getNumber());
                        }
                    }
                    HashSet<String> set = new HashSet<>(liveuser);
                    ArrayList<String> newLiveUsers = new ArrayList<>(set);
                    liveBetUser.setText(String.valueOf(newLiveUsers.size()));
                    livePoints.setText(String.valueOf(livepoints));

                    betUser.setText(String.valueOf(number.size()));
                    betPoints.setText(String.valueOf(currentBetPoints));
                } else
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<AgentBetlist>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Offline!", Toast.LENGTH_SHORT).show();
            }
        });
        livepoints = 0;
        currentBetUser = 0;
        currentBetPoints = 0;

    }
}


