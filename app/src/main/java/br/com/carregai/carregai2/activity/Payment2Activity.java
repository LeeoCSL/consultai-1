package br.com.carregai.carregai2.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.fragments.PaymentFragment;
import br.com.carregai.carregai2.fragments.ReviewFragment;
import br.com.carregai.carregai2.fragments.ShippingFragment;

public class Payment2Activity extends AppCompatActivity {

    private FrameLayout mFrame1;

    private Fragment frag2, frag3;
    private ShippingFragment frag1;
    private Button mBtnCarrinho, mBtnPagamento, mBtnRevisao;

    private ImageView mT1, mT2, mT3;

    private Toolbar mToolbar;
    public static TextView mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment2);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mValue = (TextView)mToolbar.findViewById(R.id.payment_value);

        frag1 = new ShippingFragment();
        frag2 = new PaymentFragment();
        frag3 = new ReviewFragment();

        mBtnCarrinho = (Button)findViewById(R.id.btn_carrinho);
        mBtnPagamento = (Button)findViewById(R.id.btn_pagamento);
        mBtnRevisao = (Button)findViewById(R.id.btn_revisao);

        mT1 = (ImageView)findViewById(R.id.t1);
        mT2 = (ImageView)findViewById(R.id.t2);
        mT3 = (ImageView)findViewById(R.id.t3);

        disableButton(mBtnPagamento);
        disableButton(mBtnRevisao);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.place_holder1, frag1)
                .commit();
    }

    private void disableButton(Button button){
        button.setEnabled(false);
        button.setTextColor(Color.parseColor("#9D9FA2"));
    }

    private void enableButton(Button button){
        button.setEnabled(true);
        button.setTextColor(Color.WHITE);
    }

    public void handlerToPayment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.place_holder1, frag2)
                .commit();

        disableButton(mBtnCarrinho);
        enableButton(mBtnPagamento);

        mT1.setVisibility(View.GONE);
        mT2.setVisibility(View.VISIBLE);
        mT3.setVisibility(View.GONE);
    }
}
