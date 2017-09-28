package br.com.carregai.carregai2.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.model.DashboardItem;
import io.reactivex.annotations.NonNull;

public class DashboardListViewAdapter extends ArrayAdapter<DashboardItem> {

    public DashboardListViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DashboardItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dashboard_item_list, null);
        }

        DashboardItem item = getItem(position);

        ImageView imageView = (ImageView)view.findViewById(R.id.list_item_image);
        TextView textView = (TextView)view.findViewById(R.id.list_item_title);

        imageView.setImageResource(item.getImageID());
        textView.setText(item.getTitle());

        return view;
    }
}