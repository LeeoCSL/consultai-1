package br.com.carregai.carregai2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import br.com.carregai.carregai2.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.goodiebag.carouselpicker.CarouselPicker;

public class CardNumberHelpActivity extends AppCompatActivity {

    @BindView(R.id.carousel)
    CarouselPicker mCarousel;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_number_help);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Número do cartão");

        List<CarouselPicker.PickerItem> imgs = new ArrayList<>();
        imgs.add(new CarouselPicker.DrawableItem(R.drawable.cartao_anhangabau_numero_480));
        imgs.add(new CarouselPicker.DrawableItem(R.drawable.cartao_comum_numero_480));
        imgs.add(new CarouselPicker.DrawableItem(R.drawable.cartao_estacionamento_numero_480));
        imgs.add(new CarouselPicker.DrawableItem(R.drawable.cartao_estudante_numero_480));
        imgs.add(new CarouselPicker.DrawableItem(R.drawable.cartao_ibirapuera_numero_480));
        imgs.add(new CarouselPicker.DrawableItem(R.drawable.cartao_masp_numero_480));

        CarouselPicker.CarouselViewAdapter adapter = new CarouselPicker.CarouselViewAdapter(this, imgs, 0);
        mCarousel.setAdapter(adapter);
    }
}
