package br.com.carregai.carregai2.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.utils.NotificationUtils;

/**
 * Created by renan.boni on 17/08/2017.
 */

public class NotificationService extends IntentService {

    public NotificationService() {
        super("notification_service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        float saldoAtual = sharedPref.getFloat("saldo_atual", 0);
        NotificationUtils.create(getApplicationContext(), 10, intent, R.mipmap.ic_launcher, "Atenção!", "Seu saldo é de apenas R$" +String.format("%.2f", saldoAtual));
    }
}
