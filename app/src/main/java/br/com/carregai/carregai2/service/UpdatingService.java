package br.com.carregai.carregai2.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;
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
            }
        }

        NotificationUtils.create(getApplicationContext(), 10, intent, R.mipmap.ic_launcher, "Atenção!", "Seu saldo é de apenas R$" +String.format("%.2f", saldoAtual));
    }
}
