package br.com.carregai.carregai2.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.adapter.PaymentPageAdapter;
import br.com.carregai.carregai2.adapter.SectionPageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import ivb.com.materialstepper.progressMobileStepper;

public class PaymentActivity extends progressMobileStepper {

    public static final String SHIPPING_FRAGMENT = "Carrinho";
    public static final String PAYMENT_FRAGMENT = "Pagamento";
    public static final String REVIEW_FRAGMENT = "Revisão";

    public static final int TOTAL_FRAGMENTS = 3;

    private PaymentPageAdapter mAdapter;

    @BindView(R.id.payment_view_pager)
    ViewPager mViewPager;

    @BindView(R.id.payment_tab)
    TabLayout mTabLayout;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ButterKnife.bind(this);

        mToolbar = (Toolbar)findViewById(R.id.payment_toolbar);

        setSupportActionBar(mToolbar);

        mAdapter = new PaymentPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onStepperCompleted() {

    }

    @Override
    public List<Class> init() {
        return null;
    }
}
