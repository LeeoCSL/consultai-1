package br.com.carregai.carregai2.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.adapter.RecargaRecyclerAdapter;
import br.com.carregai.carregai2.model.Recarga;
import butterknife.BindView;


public class OrdersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Recarga> mRecargas = new ArrayList<>();
    private RecargaRecyclerAdapter adapter;


    public OrdersFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.orders_layout, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_recarga);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);

        adapter = new RecargaRecyclerAdapter(mRecargas, getActivity());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users")
                .child(userID).child("recargas");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Recarga recarga = new Recarga();

                recarga.setDate(dataSnapshot.child("recarga_horario").getValue(String.class));
                recarga.setValue(dataSnapshot.child("recarga_valor").getValue(String.class));

                mRecargas.add(recarga);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
