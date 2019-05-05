package com.mg.spstocks;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mg.spstocks.fragments.CoinFragment;
import com.mg.spstocks.fragments.GoldFragment;
import com.mg.spstocks.fragments.TransferFragment;
import com.mg.spstocks.adapters.Coinadapter;
import com.mg.spstocks.adapters.Goldadapter;
import com.mg.spstocks.api.classes.Api;
import com.mg.spstocks.api.classes.ApiClient;
import com.mg.spstocks.api.classes.ApiResponse;
import com.mg.spstocks.models.Coin;


import com.mg.spstocks.models.gold;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView mTextMessage;
    private  RecyclerView myRecyclerView;
    private Coinadapter mAdapter;
    private Goldadapter mAdapter2;
    public ArrayList<Coin> coinList;
    public   ArrayList<gold> goldList;
    ProgressDialog progressDialog;
    TextView textView;
    Toolbar toolbar ;
    SharedPreferences  mPrefs;
    Dialog reConnectDialog;
    Button reConnect;
    InterstitialAd mInterstitialAd;
    private InterstitialAd interstitial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressDialog =ProgressDialog.getInstance();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        coinList= new ArrayList<Coin>();
        goldList=new ArrayList<gold>();
        mPrefs = getSharedPreferences("myPrefs",MODE_PRIVATE);
        progressDialog.show(this);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(MainActivity.this);
       // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        interstitial.loadAd(adRequest);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
           // Call displayInterstitial() function
                displayInterstitial();
            }
        });
        getData();
        setNotification();
        setDolarNotification();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest2);





    }
    public void getData() {
        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ApiResponse<List<Coin>>> call = apiService.getCoins(Constants.API_KEY);
        call.enqueue(new Callback<ApiResponse<List<Coin>>>() {
         @Override
         public void onResponse(Call<ApiResponse<List<Coin>>> call, Response<ApiResponse<List<Coin>>> response) {
             if (response.isSuccessful()) {
                 List<Coin> temp = response.body().getData();
                 for (int i = 0; i < temp.size(); i++) {
                     coinList.add(temp.get(i));
                 }
                 SharedPreferences.Editor prefsEditor = mPrefs.edit();
                 Gson gson = new Gson();
                 String json = gson.toJson(coinList);
                 prefsEditor.putString("coinList", json);
                 prefsEditor.commit();
                 getGold();
             }
             else
             {
                 importFromPref();
             }

         }
         @Override
         public void onFailure(Call<ApiResponse<List<Coin>>> call, Throwable t) {
             importFromPref();
             Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
         }
     });

    }
    public void getGold()
    {
        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ApiResponse<List<gold>>> call = apiService.getGold(Constants.API_KEY);

        call.enqueue(new Callback<ApiResponse<List<gold>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<gold>>> call, Response<ApiResponse<List<gold>>> response) {
               if (response.isSuccessful()) {
                   List<gold> temp = response.body().getData();
                   for (int i = 0; i < temp.size(); i++) {
                       goldList.add(temp.get(i));
                   }
                   SharedPreferences.Editor prefsEditor = mPrefs.edit();
                   Gson gson = new Gson();
                   String json = gson.toJson(goldList);
                   prefsEditor.putString("goldList", json);
                   prefsEditor.commit();
                   if (reConnectDialog != null)
                       if (reConnectDialog.isShowing())
                           reConnectDialog.cancel();
                   progressDialog.cancel();
                   CoinFragment fragment = new CoinFragment();
                   loadFragment(fragment);
               }
               else
               {
                   importFromPref();
               }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<gold>>> call, Throwable t) {

                Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<gold> getGoldList() {  return  goldList;
    }

    public List<Coin> getCoinList() { return  coinList;
    }
    private  boolean loadFragment(Fragment fragment)
    {
        if (fragment  != null )
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fargment_container,fragment).commitNowAllowingStateLoss();
                 return true;
        }
        return false;
    }
    public  void setNotification()
    {
                Calendar firingCal = Calendar.getInstance() ;
                Calendar calendar = Calendar.getInstance();
                firingCal.set(Calendar.HOUR_OF_DAY, 9);
                firingCal.set(Calendar.MINUTE, 1);
                firingCal.set(Calendar.SECOND, 1);
                long intendedTime= firingCal.getTimeInMillis();
                long currentTime= calendar.getTimeInMillis();
                Intent intent = new Intent(getApplicationContext(), Notification_reciver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                if (intendedTime >= currentTime) {
                    alarmManager.setRepeating(AlarmManager.RTC,
                            intendedTime,
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent);

                }
                else
                {
                    firingCal.add(Calendar.DAY_OF_MONTH, 1);
                    intendedTime = firingCal.getTimeInMillis();
                    alarmManager.setRepeating(AlarmManager.RTC,
                            intendedTime,
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent);
                }
    }

    public  void setDolarNotification()
    {

        Calendar firingCal = Calendar.getInstance() ;
        Calendar calendar = Calendar.getInstance();
        long intendedTime= firingCal.getTimeInMillis();
        long currentTime= calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY,8);
        Intent intent2 = new Intent(getApplicationContext(), DolarNotificationReciver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 100, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager2.setRepeating(AlarmManager.RTC,
                    calendar.getTimeInMillis(),
                    1000*60*60*8,
                    pendingIntent2);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment= null;
        switch (menuItem.getItemId())
        {
            case R.id.navigation_coins:
                fragment = new CoinFragment();

                break;
            case R.id.navigation_gold:
                fragment = new GoldFragment();

                break;
            case R.id.navigation_tranfer:
                fragment = new TransferFragment();
                break;
        }
        return loadFragment(fragment);
    }


    public void  importFromPref()
    {
        if (mPrefs.contains("coinList") && mPrefs.contains("goldList") ) {
            Gson gson = new Gson();
            String json = mPrefs.getString("coinList", "");
            Type type = new TypeToken<List<Coin>>() {
            }.getType();
            coinList = gson.fromJson(json, type);
            gson = new Gson();
            json = mPrefs.getString("goldList", "");
            type = new TypeToken<List<gold>>() {
            }.getType();
            goldList = gson.fromJson(json, type);
            progressDialog.cancel();
            CoinFragment fragment = new CoinFragment();
            loadFragment(fragment);
        }
        else
        {
            progressDialog.cancel();
            if (reConnectDialog == null) {
                reConnectDialog = new Dialog(MainActivity.this, R.style.myDialog);
                reConnectDialog.setContentView(R.layout.dialog_connect);
                reConnectDialog.setCancelable(false);
                reConnectDialog.setCanceledOnTouchOutside(false);
                reConnectDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                reConnectDialog.show();
                reConnect = (Button) reConnectDialog.findViewById(R.id.buttonReconnect);
                reConnect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reConnectDialog.cancel();
                        progressDialog.show(MainActivity.this);
                        getData();
                    }
                });
            }
            else
            {
                if(!reConnectDialog.isShowing())
                {
                    reConnectDialog.show();
                }
            }


        }
    }
    public void displayInterstitial() {
// If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
