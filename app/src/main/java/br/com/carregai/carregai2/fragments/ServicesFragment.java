package br.com.carregai.carregai2.fragments;


import br.com.carregai.carregai2.MainActivity;
import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.activity.LoginActivity;
import br.com.carregai.carregai2.adapter.DashboardGridViewAdapter;
import br.com.carregai.carregai2.model.DashboardItem;
import br.com.carregai.carregai2.service.UpdatingService;
import br.com.carregai.carregai2.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by renan.boni on 09/08/2017.
 */

public class ServicesFragment extends Fragment {

    private FirebaseAnalytics mFirebaseAnalytics;

    private GridView mGridView;
    private DashboardGridViewAdapter mAdapter;
    private List<DashboardItem> mItens;

    public static final int GASTO_DIARIO = 0;
    public static final int VALOR_RECARGA = 1;
    public static final int DIAS_DA_SEMANA = 2;
    private static final int COMO_USAR = 3;
    private static final int LIMPAR_CAMPOS = 4;
    private static final int VIAGEM_EXTRA = 5;

    private TextView mDisplay;


    public ServicesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getItensList();
        View view = inflater.inflate(R.layout.service_layout, container, false);
        mDisplay = (TextView) view.findViewById(R.id.tv_display);
        mGridView = (GridView) view.findViewById(R.id.dashboard_gridview);
        mGridView.setAdapter(new DashboardGridViewAdapter(view.getContext(), 1, mItens));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case GASTO_DIARIO:
                        valorDiario();
                        break;
                    case VALOR_RECARGA:
                        valorRecarga();
                        break;
                    case DIAS_DA_SEMANA:
                        selectDays();
                        break;
                    case COMO_USAR:
                        comoFunciona();
                        break;
                    case VIAGEM_EXTRA:
                        viagemExtra();
                        break;
                    case LIMPAR_CAMPOS:
                        limparCampos();
                        break;
                    case 6:
                        trigger();
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateViews();
    }

    private void getItensList() {
        mItens = new ArrayList<>();

        mItens.add(new DashboardItem(R.drawable.cifrao, "Gasto diário"));
        mItens.add(new DashboardItem(R.drawable.moeda, "Valor de recarga"));
        mItens.add(new DashboardItem(R.drawable.calendario, "Dias de uso"));
        mItens.add(new DashboardItem(R.drawable.interrogacao, "Como usar"));
        mItens.add(new DashboardItem(R.drawable.ic_vassoura, "Limpar dados"));
        mItens.add(new DashboardItem(R.drawable.bus, "Viagem extra"));
        mItens.add(new DashboardItem(R.drawable.bus, "teste"));
    }

    private void updateViews() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mDisplay.setText(Utility.formatValue(sp.getFloat("saldo_atual", 0)));
    }

    public void valorDiario() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Valor Diário");

        final CurrencyEditText input = new CurrencyEditText(getActivity(), null);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final float saldo = sp.getFloat("valor_diario", 0);

        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Digite o valor diário...");
        input.setText(Utility.formatValue(saldo));

        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                float db = Utility.stringToFloat(input.getText().toString());

                storeValue("valor_diario", db);

                Utility.makeText(getActivity(), "O seu gasto diário foi salvo.");

                Bundle bundle = new Bundle();
                bundle.putFloat("valor_diario", saldo );
                if (LoginActivity.emailParam != "") {
                    bundle.putString("email", LoginActivity.emailParam);
                }
                if (LoginActivity.emailGoogle != "") {
                    bundle.putString("email_google", LoginActivity.emailGoogle);
                }
                if (LoginActivity.linkFB != "") {
                    bundle.putString("link_fb", LoginActivity.linkFB);
                }
                if (LoginActivity.nomeFB != "") {
                    bundle.putString("nome", LoginActivity.nomeFB);
                }
                if (LoginActivity.idFacebook != "") {
                    bundle.putString("id", LoginActivity.idFacebook);
                }
                if (LoginActivity.emailFB != "") {
                    bundle.putString("email_facebook", LoginActivity.emailFB);
                }

                mFirebaseAnalytics.logEvent("valor_diario", bundle);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void valorRecarga() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Valor Recarga");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final float saldo = sp.getFloat("saldo_atual", 0);

        final CurrencyEditText input = new CurrencyEditText(getActivity(), null);
        input.setText(Utility.formatValue(sp.getFloat("valor_recarga", 0)));
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                float valorRecarga = Utility.stringToFloat(input.getText().toString());
                float saldoFinal = saldo + valorRecarga;

                mDisplay.setText(Utility.formatValue(saldoFinal));

                storeValue("saldo_atual", saldoFinal);
                storeValue("valor_recarga", valorRecarga);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
                Date data = new Date();

                Calendar cal = Calendar.getInstance();
                cal.setTime(data);
                Date dataAtual = cal.getTime();

                String dataCompleta = dateFormat.format(dataAtual);

                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("recargas");

                HashMap<String, String> values = new HashMap<String, String>();
                values.put("recarga_horario", dataCompleta);
                values.put("recarga_valor", String.valueOf(valorRecarga));

                ref.push().setValue(values);

                Utility.makeText(getActivity(), "Sua recarga foi atualizada.");

                Bundle bundle = new Bundle();
                bundle.putFloat("valor_recarga",valorRecarga );
                if (LoginActivity.emailParam != "") {
                    bundle.putString("email", LoginActivity.emailParam);
                }
                if (LoginActivity.emailGoogle != "") {
                    bundle.putString("email_google", LoginActivity.emailGoogle);
                }
                if (LoginActivity.linkFB != "") {
                    bundle.putString("link_fb", LoginActivity.linkFB);
                }
                if (LoginActivity.nomeFB != "") {
                    bundle.putString("nome", LoginActivity.nomeFB);
                }
                if (LoginActivity.idFacebook != "") {
                    bundle.putString("id", LoginActivity.idFacebook);
                }
                if (LoginActivity.emailFB != "") {
                    bundle.putString("email_facebook", LoginActivity.emailFB);
                }

                mFirebaseAnalytics.logEvent("valor_recarga", bundle);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void selectDays() {
        FragmentManager fragmentManager = getFragmentManager();
        DialogWeek dialogWeek = new DialogWeek();
        dialogWeek.show(fragmentManager, "dias_semana");
    }

    private void storeValue(String key, float value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putFloat(key, value);

        editor.commit();
    }

    private void comoFunciona() {
        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.tutorial);

        Button btn = (Button) dialog.findViewById(R.id.btn_entendi);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void viagemExtra() {
        Utility.makeText(getActivity(), "Seu saldo foi atualizado. [Viagem Extra: R$ " + " ]");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        float value = sharedPref.getFloat("viagem_extra", 0);

        if (value == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Configure o valor da viagem extra na barra de menu.")
                    .setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.create();
        } else {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            float saldoAtual = sp.getFloat("saldo_atual", 0);

            if (saldoAtual - value < 0) {
                Utility.makeText(getActivity(), "Atualize seu saldo antes de calcular a viagem extra.");

            } else if (saldoAtual - value >= 0) {
                saldoAtual -= value;
                SharedPreferences.Editor editor = sp.edit();
                editor.putFloat("saldo_atual", saldoAtual);
                editor.commit();
                mDisplay.setText("R$ " + String.format("%.2f", saldoAtual));
                Utility.makeText(getActivity(), "Seu saldo foi atualizado. [Viagem Extra: R$ " + value + " ]");

                Bundle bundle = new Bundle();
                if (LoginActivity.emailParam != "") {
                    bundle.putString("email", LoginActivity.emailParam);
                }
                if (LoginActivity.emailGoogle != "") {
                    bundle.putString("email_google", LoginActivity.emailGoogle);
                }
                if (LoginActivity.linkFB != "") {
                    bundle.putString("link_fb", LoginActivity.linkFB);
                }
                if (LoginActivity.nomeFB != "") {
                    bundle.putString("nome", LoginActivity.nomeFB);
                }
                if (LoginActivity.idFacebook != "") {
                    bundle.putString("id", LoginActivity.idFacebook);
                }
                if (LoginActivity.emailFB != "") {
                    bundle.putString("email_facebook", LoginActivity.emailFB);
                }
                mFirebaseAnalytics.logEvent("viagem_extra", bundle);

            }
        }
    }

    private void limparCampos() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        SharedPreferences.Editor editor = sharedPref.edit();

        String value = "R$ 00,00";

        editor.putFloat("valor_diario", 0);
        editor.putFloat("valor_recarga", 0);
        editor.putFloat("viagem_extra", 0);
        editor.putFloat("saldo_atual", 0);

//        mValorRecarga.setText(value);
//        mValorDiario.setText(value);
        mDisplay.setText(value);


        final String[] items = {"Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira",
                "Sexta-feira", "Sábado", "Domingo"};


        for (int i = 0; i < items.length; i++) {
            String key = items[i].toLowerCase();
            editor.putBoolean(key, false);
        }

        editor.commit();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();


        if (LoginActivity.emailParam != "") {
            bundle.putString("email", LoginActivity.emailParam);
        }
        if (LoginActivity.emailGoogle != "") {
            bundle.putString("email_google", LoginActivity.emailGoogle);
        }
        if (LoginActivity.linkFB != "") {
            bundle.putString("link_fb", LoginActivity.linkFB);
        }
        if (LoginActivity.nomeFB != "") {
            bundle.putString("nome", LoginActivity.nomeFB);
        }
        if (LoginActivity.idFacebook != "") {
            bundle.putString("id", LoginActivity.idFacebook);
        }
        if (LoginActivity.emailFB != "") {
            bundle.putString("email_facebook", LoginActivity.emailFB);
        }
        mFirebaseAnalytics.logEvent("limpar_campos", bundle);
    }
    public void trigger(){

        Utility.makeText(getActivity(), "servico");

        Calendar calendar = (GregorianCalendar) Calendar.getInstance();

        Intent myIntent = new Intent(getActivity(), UpdatingService.class);

        PendingIntent pendingIntent = pendingIntent = PendingIntent.getService(getActivity(), 0,
                myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                30 * 1000, pendingIntent);
    }
}
