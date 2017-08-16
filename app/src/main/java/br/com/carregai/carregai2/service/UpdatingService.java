package br.com.carregai.carregai2.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.utils.NotificationUtils;

/**
 * Created by renan.boni on 16/08/2017.
 */

public class UpdatingService extends WakefulIntentService {

    public UpdatingService() {
        super("UpdatingService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("Info", "oi");

        NotificationUtils.create(getApplicationContext(), 10, intent, R.mipmap.ic_launcher, "TETE", "TESTE");
    }
}
