package com.example.useraccountmanagement.fragments;


import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useraccountmanagement.R;
import com.kaopiz.kprogresshud.KProgressHUD;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LivePageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    Handler handler;
    private Document document;
    private String pageUrl="https://marketdata.set.or.th/mkt/marketsummary.do?language=en&country=US";
    TextView currentResult,liveUpdateDate,tvDate,tvTime,tvDetailSet,tvDetailValue,tvFirstResult;
    SwipeRefreshLayout swipeRefresh;
    private KProgressHUD hud;
    private int hour,min;
    String date,time;


    public LivePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_live_page, container, false);
        currentResult=view.findViewById(R.id.currentResult);
        liveUpdateDate=view.findViewById(R.id.liveUpdateDate);
        tvDate=view.findViewById(R.id.Date);
        tvTime=view.findViewById(R.id.time);
        tvDetailSet=view.findViewById(R.id.DetailSet);
        tvDetailValue=view.findViewById(R.id.DetailValue);
        tvFirstResult=view.findViewById(R.id.Result);
        swipeRefresh=view.findViewById(R.id.liveSwip);
        tvDate=view.findViewById(R.id.Date);
        tvTime=view.findViewById(R.id.Time);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Live");

        handler=new Handler();
        swipeRefresh.setRefreshing(true);
        swipeRefresh.setOnRefreshListener(this);

        hud = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(new DialogInterface.OnCancelListener()
                {
                    @Override public void onCancel(DialogInterface
                                                           dialogInterface)
                    {
                        Toast.makeText(getContext(), "You " +
                                "cancelled manually!", Toast
                                .LENGTH_SHORT).show();
                    }
                });
        hud.show();
        checkConnection();
        // Inflate the layout for this fragment
        return view;
    }

    private void checkConnection() {

        if (isOnline()){
            handler.postDelayed(runn,650);
            hud.dismiss();
            swipeRefresh.setRefreshing(false);
            Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();
        }else {
            swipeRefresh.setRefreshing(false);
            Toast.makeText(getContext(), "You are offline!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOnline() {
        if ((getActivity()!=null && !getActivity().isFinishing())){
        ConnectivityManager cm= (ConnectivityManager)getActivity(). getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo=cm.getActiveNetworkInfo();
        if (netInfo!=null && netInfo.isConnected()){
            return true;
        }else {
            return false;
        }
        }
        return false;
    }
    public void onRefresh() {
        checkConnection();
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
                Toast.makeText(getContext(), "No more data!,you are offline", Toast.LENGTH_SHORT).show();
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
                currentResult.setText(top+base);

                TimeZone tz=TimeZone.getTimeZone("GMT+6:30");
                Calendar c= Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                timeformat.setTimeZone(tz);
                String date = dateFormat.format(c.getTime());
                tvDate.setText(date);

                time = timeformat.format(c.getTime());
                hour=Integer.parseInt(time.substring(0,time.indexOf(":")));
                min=Integer.parseInt(time.substring(time.indexOf(":")+1,time.length()));

                tvDetailSet.setText(set);
                tvDetailValue.setText(value);
                tvFirstResult.setText(top+base);
                liveUpdateDate.setText(updateDateTime.replace("*",""));
            }
        }
    }
    Runnable runn=new Runnable() {
        @Override
        public void run() {
            new ParseLiveData().execute();
            if (hour>=9 && (hour<=12 & min==1)){
                timeEdit();
                //blink();
                tvTime.setText("12:00 PM");
            }else {
                tvTime.setText("12:00 PM");
            }
            if (hour>=13 && (hour<=16 && min<=31)){
                timeEdit();
                tvTime.setText("4:30 PM");
            }else {
                tvTime.setText("4:30 PM");
            }

                blink();
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
                        if (currentResult.getVisibility()== View.VISIBLE){
                            currentResult.setVisibility(View.INVISIBLE);
                        }else {
                            currentResult.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        };
        new Thread(blinkRunn).start();
    }

    private void timeEdit() {
        if (hour<12){
            tvTime.setText(time+" AM");
        }else if (hour==12){
            tvTime.setText(time+" PM");
        }else if (hour==24){
            String otime=time;
            String eTime=otime.replace(String.valueOf(hour),String.valueOf(hour-12));
            tvTime.setText(eTime+" AM");
        }else {
            tvTime.setText(time.replace(String.valueOf(hour),String.valueOf(hour-12))+" PM");
        }
    }


}
