package br.com.carregai.carregai2.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by renan.boni on 16/08/2017.
 */

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        WakefulIntentService.acquireStaticLock(context); //acquire a partial WakeLock
        context.startService(new Intent(context, TaskButlerService.class)); //start TaskButlerService
    }
}