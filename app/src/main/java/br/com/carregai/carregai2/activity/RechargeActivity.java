package br.com.carregai.carregai2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import br.com.carregai.carregai2.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.goodiebag.carouselpicker.CarouselPicker;

public class RechargeActivity extends AppCompatActivity {

    @BindView(R.id.carousel)
    CarouselView mCarousel;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private int[] drawables = {R.drawable.cartao_01, R.drawable.cartao_02,
            R.drawable.cartao_03, R.drawable.cartao_04, R.drawable.cartao_05,
            R.drawable.cartao_06, R.drawable.cartao_07};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Comprar recarga");

        mCarousel.setPageCount(drawables.length);

        mCarousel.setImageListener(imageListener);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(drawables[position]);
        }
    };
}
