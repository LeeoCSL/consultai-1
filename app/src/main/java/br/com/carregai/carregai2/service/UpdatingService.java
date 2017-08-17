package br.com.carregai.carregai2.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.utils.NotificationUtils;

/**
 * Created by renan.boni on 16/08/2017.
 */

public class UpdatingService extends IntentService {

    public UpdatingService() {
        super("UpdatingService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Calendar calendar = Calendar.getInstance();
        String currentDay = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        float saldoAtual = sharedPref.getFloat("saldo_atual", 0);
        float valorDiario = sharedPref.getFloat("valor_diario", 0);

        if(sharedPref.getBoolean(currentDay, false)){
            if(saldoAtual - valorDiario > 0){
                saldoAtual -= valorDiario;
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("saldo_atual", saldoAtual);
                editor.commit();
            }else if(saldoAtual < 10){
                int hour = sharedPref.getInt("notification_hour", 22);
                int min = sharedPref.getInt("notification_minute", 0);

                Calendar cal = (GregorianCalendar) Calendar.getInstance();
                cal.add(Calendar.HOUR, hour);
                cal.add(Calendar.MINUTE, min);

                Intent myIntent = new Intent(this, NotificationService.class);

                PendingIntent pendingIntent = pendingIntent = PendingIntent.getService(this, 0,
                        myIntent, 0);

                Date date = cal.getTime();

                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            }
        }
    }
}
