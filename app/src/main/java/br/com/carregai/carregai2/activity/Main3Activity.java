package br.com.carregai.carregai2.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import br.com.carregai.carregai2.MainActivity;
import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.adapter.DashboardListViewAdapter;
import br.com.carregai.carregai2.adapter.SectionPageAdapter;
import br.com.carregai.carregai2.fragments.ServicesFragment;
import br.com.carregai.carregai2.model.DashboardItem;
import br.com.carregai.carregai2.model.User;
import br.com.carregai.carregai2.service.UpdatingService;
import br.com.carregai.carregai2.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Main3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SERVICES_FRAGMENT = "Serviços";
    public static final String ORDERS_FRAGMENT = "Recargas";
    public static final int TOTAL_FRAGMENTS = 2;

    private TextView mUsername;
    private TextView mUserEmail;
    private CircleImageView mUserImage;

    @BindView(R.id.main_page_toolbar)
    Toolbar mToolbar;

    ViewPager mViewPager;

    TabLayout mTabLayout;

    private SectionPageAdapter mAdapter;

    private boolean firstTime = true;

    private DatabaseReference mUserDatabase;

    private User user;

    private List<DashboardItem> mItens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        ButterKnife.bind(this);

        mViewPager = (ViewPager)findViewById(R.id.main_view_pager);
        mTabLayout = (TabLayout)findViewById(R.id.main_tab);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userID);

        mAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        firstTime = sharedPref.getBoolean("first_time", true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // navigation user info (foto, nome e email)
        View header = navigationView.getHeaderView(0);
        mUsername = (TextView)header.findViewById(R.id.nav_header_user_name);
        mUserEmail = (TextView)header.findViewById(R.id.nav_header_user_email);
        mUserImage = (CircleImageView) header.findViewById(R.id.nav_header_user_image);

        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                Picasso.with(getApplicationContext()).
                        load(user.getImage()).
                        into(mUserImage);

                mUsername.setText(user.getName());
                mUserEmail.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(firstTime){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Configurações iniciais");
            builder.setMessage("Antes de começar, configure o valor do seu saldo agora, nas" +
                    " próximas vezes você pode clicar no botão de recarga no menu principal.");

            final CurrencyEditText currencyEditText = new CurrencyEditText(this, null);
            builder.setView(currencyEditText);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putFloat("saldo_atual", Utility.stringToFloat(currencyEditText.getText().toString()));
                    editor.putBoolean("first_time", false);

                    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                            .child(userID).child("recargas");

                    editor.commit();
                    ServicesFragment.mDisplay.setText("R$ " + String.format("%.2f", Utility.stringToFloat(currencyEditText.getText().toString())));

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date dataAtual = cal.getTime();
                    String dataCompleta = dateFormat.format(dataAtual);

                    HashMap<String, String> values = new HashMap<String, String>();
                    values.put("recarga_horario", dataCompleta);
                    values.put("recarga_valor", String.valueOf(Utility.stringToFloat(currencyEditText.getText().toString())));

                    ref.push().setValue(values);
                }
            });

            builder.setNegativeButton("Faço isso depois.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("first_time", false);
                    editor.commit();
                    dialog.dismiss();
                }
            });
            builder.show();

            trigger();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(this, MeuSaldo.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            EventBus.getDefault().postSticky(user);
            startActivity(new Intent(this, ConfigActivity.class));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /*
            função disparada na primeira vez que o app é instalado,
        depois um service roda a função todos os dias
    */
    public void trigger(){

        Intent myIntent = new Intent(this, UpdatingService.class);

        PendingIntent pendingIntent = pendingIntent = PendingIntent.getService(this, 0,
                myIntent, 0);

        Calendar calendar = (GregorianCalendar) Calendar.getInstance();
/*        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 00);*/

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                1000 * 60,
                pendingIntent);
    }
}
