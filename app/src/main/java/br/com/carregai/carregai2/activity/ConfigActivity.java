package br.com.carregai.carregai2.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfigActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView mListConfig;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private String[] values = new String[]{"Perfil", "Saldo"};

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Configurações");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = EventBus.getDefault().removeStickyEvent(User.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        mListConfig.setAdapter(adapter);

        mListConfig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    EventBus.getDefault().postSticky(user);
                    startActivity(new Intent(ConfigActivity.this, SettingsActivity.class));
                }else if(position == 1){

                }
            }
        });
    }
}
