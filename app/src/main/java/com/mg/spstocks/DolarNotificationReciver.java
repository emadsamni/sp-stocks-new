package com.mg.spstocks;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mg.spstocks.api.classes.Api;
import com.mg.spstocks.api.classes.ApiClient;
import com.mg.spstocks.api.classes.ApiResponse;
import com.mg.spstocks.fragments.CoinFragment;
import com.mg.spstocks.models.Coin;
import com.mg.spstocks.models.gold;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DolarNotificationReciver  extends BroadcastReceiver {
    public Coin coin;
    SharedPreferences mPrefs;
    public ArrayList<Coin> coinList;
    @Override
    public void onReceive(final Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            setNotification(context);
            setDolarNotification(context);
        }

        mPrefs = context.getSharedPreferences("myPrefs",context.MODE_PRIVATE);
        Api apiService = ApiClient.getClient().create(Api.class);
        Call<ApiResponse<List<Coin>>> call = apiService.getCoins(Constants.API_KEY);
        call.enqueue(new Callback<ApiResponse<List<Coin>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Coin>>> call, Response<ApiResponse<List<Coin>>> response) {
                if (response.isSuccessful()) {
                    List<Coin> temp = response.body().getData();
                    coin = temp.get(0);
                    if (mPrefs.contains("coinList")) {
                        Gson gson = new Gson();
                        String json = mPrefs.getString("coinList", "");
                        Type type = new TypeToken<List<Coin>>() {
                        }.getType();
                        coinList = gson.fromJson(json, type);

                        if (Math.abs(coinList.get(0).getLog().get(0).getBuy() - coin.getLog().get(0).getBuy()) >= Constants.dolarChange) {

                            coinList.clear();
                            for (int i = 0; i < temp.size(); i++) {
                                coinList.add(temp.get(i));
                            }
                            showNot(context);
                            SharedPreferences.Editor prefsEditor = mPrefs.edit();
                            Gson gson2 = new Gson();
                            String json2 = gson2.toJson(coinList);
                            prefsEditor.putString("coinList", json2);
                            prefsEditor.commit();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Coin>>> call, Throwable t) {



            }
        });
    }
    public void showNot(Context context)
    {
        NotificationManager notificationManager = ( NotificationManager)  context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent(context,MainActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(coin.getCoin_name()+"  "+context.getResources().getString(R.string.Buy)+" :   " +coin.getLog().get(0).getBuy()+"  " + context.getResources().getString(R.string.Sell)+" :   " +coin.getLog().get(0).getSell())
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void setNotification(Context context)
    {
        Calendar firingCal = Calendar.getInstance() ;
        Calendar calendar = Calendar.getInstance();

        firingCal.set(Calendar.HOUR_OF_DAY, 9);
        firingCal.set(Calendar.MINUTE, 1);
        firingCal.set(Calendar.SECOND, 1);
        long intendedTime= firingCal.getTimeInMillis();
        long currentTime= calendar.getTimeInMillis();

        Intent intent = new Intent(context, Notification_reciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

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

    public  void setDolarNotification(Context context)
    {
        Calendar firingCal = Calendar.getInstance() ;
        Calendar calendar = Calendar.getInstance();
        long intendedTime= firingCal.getTimeInMillis();
        long currentTime= calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY,8);
        Intent intent2 = new Intent(context, DolarNotificationReciver.class);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 100, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager2 = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager2.setRepeating(AlarmManager.RTC,
                calendar.getTimeInMillis(),
                1000*60*60*8,
                pendingIntent2);
    }

}
