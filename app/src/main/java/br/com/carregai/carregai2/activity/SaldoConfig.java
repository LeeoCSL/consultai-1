package br.com.carregai.carregai2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import br.com.carregai.carregai2.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SaldoConfig extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo_config);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Configurações do saldo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
