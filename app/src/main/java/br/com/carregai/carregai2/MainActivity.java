package br.com.carregai.carregai2;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import br.com.carregai.carregai2.adapter.SectionPageAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String SERVICES_FRAGMENT = "Serviços";
    public static final String ORDERS_FRAGMENT = "Recargas";
    public static final int TOTAL_FRAGMENTS = 2;

    @BindView(R.id.main_page_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.main_view_pager)
    ViewPager mViewPager;

    @BindView(R.id.main_tab)
    TabLayout mTabLayout;

    private SectionPageAdapter mAdapter;

    private DatabaseReference mUsersDatabase;

    private Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        mAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }
}
