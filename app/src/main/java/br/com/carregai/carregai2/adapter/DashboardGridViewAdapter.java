package br.com.carregai.carregai2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.model.DashboardItem;

/**
 * Created by renan.boni on 09/08/2017.
 */

public class DashboardGridViewAdapter extends ArrayAdapter<DashboardItem> {

    public DashboardGridViewAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DashboardItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dashboard_grid_item, null);
        }

        DashboardItem item = getItem(position);

        LinearLayout layout = (LinearLayout)view.findViewById(R.id.layout);
        ImageView imageView = (ImageView)view.findViewById(R.id.grid_item_image);
        TextView textView = (TextView)view.findViewById(R.id.grid_item_title);
        layout.setBackgroundColor(Color.parseColor("#567567"));
        layout.setBackgroundResource(R.drawable.dashboard_item_background);
        imageView.setImageResource(item.getImageID());
        textView.setText(item.getTitle());


        return view;
    }
}