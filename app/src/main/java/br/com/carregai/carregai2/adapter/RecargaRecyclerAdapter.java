package br.com.carregai.carregai2.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.model.Recarga;
import br.com.carregai.carregai2.utils.Utility;

/**
 * Created by renan.boni on 11/08/2017.
 */

public class RecargaRecyclerAdapter extends RecyclerView.Adapter<RecargaRecyclerAdapter.RecargaViewHolder>{

    private List<Recarga> recargas;
    private Activity activity;

    public RecargaRecyclerAdapter(List<Recarga> recargas, Activity activity){
        this.recargas = recargas;
        this.activity = activity;
    }

    @Override
    public RecargaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.recarga_list_item,
                parent, false);
        return new RecargaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecargaViewHolder holder, int position) {
        holder.bind(recargas.get(position));
    }

    @Override
    public int getItemCount() {
        return recargas != null ? recargas.size() : 0;
    }

    class RecargaViewHolder extends RecyclerView.ViewHolder{

        private TextView date;
        private TextView value;

        RecargaViewHolder(View view){
            super(view);

            date = (TextView) view.findViewById(R.id.tv_date);
            value = (TextView) view.findViewById(R.id.tv_value);
        }

        void bind(final Recarga recarga){
            String data = recarga.getDate();

            String formattedDate = data.substring(0, 10);
            String time = data.substring(11, 16);

            formattedDate = formattedDate.replace("-","/");
            time = time.replace(":","h");
            time = time.concat("min");

            date.setText(formattedDate + " " +time);
            value.setText(Utility.formatValue(Float.parseFloat(recarga.getValue())));
        }
    }
}
