package br.com.carregai.carregai2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.carregai.carregai2.R;

public class ShippingFragment extends Fragment {

    private View view;

    private ListView mValues;

    private Button mContinue;
    private Button mOtherValue;

    private LinearLayout mBilheteUnico;

    private String[] values = {"R$ 10,00", "R$ 20,00", "R$ 50,00"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bilhete_unico_layout, container, false);

        mValues = (ListView) view.findViewById(R.id.lv_values);
        mContinue = (Button) view.findViewById(R.id.btn_continue);
        mOtherValue = (Button) view.findViewById(R.id.btn_other_value);
        mBilheteUnico = (LinearLayout) view.findViewById(R.id.bilhete_unico_ll);

        ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), R.layout.value_item_list, R.id.tv_value, values);
        mValues.setAdapter(ad);

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBilheteUnico.setVisibility(View.GONE);
                mContinue.setVisibility(View.GONE);
                mOtherValue.setVisibility(View.VISIBLE);
                mValues.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }


}
