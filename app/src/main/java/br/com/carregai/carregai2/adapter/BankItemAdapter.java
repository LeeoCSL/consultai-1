package br.com.carregai.carregai2.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.interfaces.OnItemBankClickListener;
import br.com.carregai.carregai2.model.Bank;

public class BankItemAdapter extends RecyclerView.Adapter<BankItemAdapter.BankItemAdapterViewHolder>{

    private List<Bank> banks;
    private Activity activity;
    private OnItemBankClickListener listener;

    public BankItemAdapter(List<Bank> banks, Activity activity, OnItemBankClickListener listener){
        this.banks = banks;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public BankItemAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.bank_item_list,
                parent, false);
        return new BankItemAdapter.BankItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BankItemAdapterViewHolder holder, int position) {
        holder.bind(banks.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return banks.size() > 0 ? banks.size() : 0;
    }

    class BankItemAdapterViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView mLogo;
        private TextView mBankName;
        private LinearLayout mBankLL;

        public BankItemAdapterViewHolder(View itemView) {
            super(itemView);

            mLogo = (ImageView) itemView.findViewById(R.id.ic_bank);
            mBankName = (TextView) itemView.findViewById(R.id.bank_title);
            mBankLL = (LinearLayout) itemView.findViewById(R.id.bank_ll);
        }

        void bind(final Bank bank, final OnItemBankClickListener listener){
            mBankName.setText(bank.getName());

            Picasso.with(activity.getApplicationContext())
                    .load(bank.getDrawable())
                    .into(mLogo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemBankClick(bank);
                }
            });

            if(bank.getName().equals("Caixa Econômica")){
                mBankLL.setBackgroundResource(R.drawable.selector_card_background);
            }else{
                mBankLL.setBackgroundColor(Color.parseColor(bank.getColor()));
            }

            mBankName.setTextColor(Color.parseColor(bank.getTextColor()));
        }
    }
}
