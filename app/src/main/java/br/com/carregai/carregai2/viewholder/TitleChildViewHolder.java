package br.com.carregai.carregai2.viewholder;


import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import br.com.carregai.carregai2.R;

public class TitleChildViewHolder extends ChildViewHolder{

    public TextView mTitle;

    public TitleChildViewHolder(View itemView) {
        super(itemView);

        mTitle = (TextView)itemView.findViewById(R.id.tv_title);
    }
}
