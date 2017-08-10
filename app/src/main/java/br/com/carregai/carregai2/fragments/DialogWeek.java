package br.com.carregai.carregai2.fragments;

/**
 * Created by renan.boni on 10/08/2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TextView;


import java.util.Calendar;
import java.util.Locale;



public class DialogWeek extends DialogFragment {

    private TextView[] mDiasSemana;
    private boolean mCheckeds[];

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = {"Segunda-feira", "Terça-feira","Quarta-feira","Quinta-feira",
                "Sexta-feira","Sábado","Domingo"};

        mDiasSemana = new TextView[items.length];

        mCheckeds = new boolean[items.length];

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        for(int i = 0; i < items.length; i++){
            String key = items[i].toLowerCase();
            boolean value = sharedPref.getBoolean(key, false);

            mCheckeds[i] = value;
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());

        builder.setTitle("Dias de uso")
                .setMultiChoiceItems(items, mCheckeds,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int item, boolean isChecked) {
                                mCheckeds[item] = isChecked;
                            }
                        });

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();

                for(int i = 0; i < items.length; i++){
                    editor.putBoolean(items[i].toLowerCase(), mCheckeds[i]);

                    editor.commit();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
