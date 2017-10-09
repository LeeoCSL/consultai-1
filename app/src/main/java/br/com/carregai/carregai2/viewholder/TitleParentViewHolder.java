package br.com.carregai.carregai2.viewholder;


import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import br.com.carregai.carregai2.R;

public class TitleParentViewHolder extends ParentViewHolder {

    public TextView mSaldo;

    public TitleParentViewHolder(View itemView) {
        super(itemView);

        mSaldo = (TextView)itemView.findViewById(R.id.saldo);
    }
}
