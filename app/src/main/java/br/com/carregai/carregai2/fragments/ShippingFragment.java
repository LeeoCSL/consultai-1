package br.com.carregai.carregai2.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.CarouselViewPager;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.activity.Payment2Activity;

public class ShippingFragment extends Fragment {

    private View view;

    private ListView mValues;

    private Button mContinue;
    private Button mOtherValue;

    private LinearLayout mBilheteUnico;

    private String[] values = {"R$ 10,00", "R$ 20,00", "R$ 50,00"};

    private Payment2Activity mPaymentActivity;

    private CarouselView mCarousel;

    int[] sampleImages = {R.drawable.cartao_01,
            R.drawable.cartao_02, R.drawable.cartao_03, R.drawable.cartao_04, R.drawable.cartao_05};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bilhete_unico_layout, container, false);

        mValues = (ListView) view.findViewById(R.id.lv_values);
        mContinue = (Button) view.findViewById(R.id.btn_continue);
        mOtherValue = (Button) view.findViewById(R.id.btn_other_value);
        mBilheteUnico = (LinearLayout) view.findViewById(R.id.bilhete_unico_ll);

        mCarousel = (CarouselView) view.findViewById(R.id.carousel);
        mCarousel.setPageCount(sampleImages.length);


        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        };
        mCarousel.setImageListener(imageListener);

        mPaymentActivity = (Payment2Activity)getActivity();

        ArrayAdapter<String> ad = new ArrayAdapter<String>(getContext(), R.layout.value_item_list, R.id.tv_value, values);
        mValues.setAdapter(ad);

        mValues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Payment2Activity.mValue.setVisibility(View.VISIBLE);
                Payment2Activity.mValue.setText(values[position]);
                mPaymentActivity.handlerToPayment();
            }
        });

        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBilheteUnico.setVisibility(View.GONE);
                mContinue.setVisibility(View.GONE);
                mOtherValue.setVisibility(View.VISIBLE);
                mCarousel.setVisibility(View.GONE);
                mValues.setVisibility(View.VISIBLE);
            }
        });

        mOtherValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaymentActivity.handlerToPayment();
            }
        });

        return view;
    }
}
