package br.com.carregai.carregai2.activity;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.adapter.ExpandableAdapter;
import br.com.carregai.carregai2.model.TitleChild;
import br.com.carregai.carregai2.model.TitleCreator;
import br.com.carregai.carregai2.model.TitleParent;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MeuSaldo extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_view)
    RecyclerView mSaldoConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meu_saldo);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mSaldoConfig.setLayoutManager(new LinearLayoutManager(this));

        ExpandableAdapter adapter = new ExpandableAdapter(this, initData());
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);

        mSaldoConfig.setAdapter(adapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState);
        ((ExpandableAdapter)mSaldoConfig.getAdapter()).onSaveInstanceState(outState);
    }

    private List<ParentObject> initData() {
        TitleCreator titleCreator = TitleCreator.get(this);
        List<TitleParent> titles = titleCreator.getAll();
        List<ParentObject> parentObject = new ArrayList<>();

        for(TitleParent title: titles){
            List<Object> childList = new ArrayList<>();
            childList.add(new TitleChild("Add to contats"));
            title.setChildObjectList(childList);
            parentObject.add(title);
        }
        return parentObject;
    }
}
