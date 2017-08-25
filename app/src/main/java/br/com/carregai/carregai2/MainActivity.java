package br.com.carregai.carregai2;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import br.com.carregai.carregai2.activity.SettingsActivity;
import br.com.carregai.carregai2.adapter.SectionPageAdapter;
import br.com.carregai.carregai2.fragments.ServicesFragment;
import br.com.carregai.carregai2.model.User;
import br.com.carregai.carregai2.service.UpdatingService;
import br.com.carregai.carregai2.utils.DrawerUtils;
import br.com.carregai.carregai2.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener, AccountHeader.OnAccountHeaderProfileImageListener{

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

    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        firstTime = sharedPref.getBoolean("first_time", true);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

        if(user != null){
            DatabaseReference child = userRef.child(user.getUid());

            child.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User usermodel = dataSnapshot.getValue(User.class);

                    Log.i("USER: ", usermodel.toString());

                    Drawer drawer = new DrawerUtils()
                            .setUpCustomerDrawer(MainActivity.this,
                                    usermodel.getName(),
                                    usermodel.getEmail(),
                                    user.getPhotoUrl(),
                                    mToolbar,
                                    MainActivity.this).build();

                    drawer.setOnDrawerItemClickListener(MainActivity.this);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        mAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

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
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        DrawerUtils.onUserClickListener(position, this, this);
        return false;
    }

    public void trigger(){

        Intent myIntent = new Intent(this, UpdatingService.class);

        PendingIntent pendingIntent = pendingIntent = PendingIntent.getService(this, 0,
                myIntent, 0);

        Calendar calendar = (GregorianCalendar) Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 00);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    @Override
    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
    }

    @Override
    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
        return false;
    }
}
