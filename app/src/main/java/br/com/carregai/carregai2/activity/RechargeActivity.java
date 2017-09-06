package br.com.carregai.carregai2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.adapter.DashGridViewAdapter;
import br.com.carregai.carregai2.adapter.DashboardGridViewAdapter;
import br.com.carregai.carregai2.model.DashItem;
import br.com.carregai.carregai2.model.DashboardItem;
import br.com.carregai.carregai2.utils.Utility;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.goodiebag.carouselpicker.CarouselPicker;

public class RechargeActivity extends AppCompatActivity {

    @BindView(R.id.carousel)
    CarouselView mCarousel;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private GridView GridView;

    private List<DashItem> Itens;

    public static final int btn190 = 0;
    public static final int btn380 = 1;
    public static final int btn500 = 2;
    private static final int btn680 = 3;
    private static final int btn760 = 4;
    private static final int btn2000 = 5;
    public static final int btn5000 = 6;
    public static final int btnOutro = 7;

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
        getItensList();
        GridView = (GridView) findViewById(R.id.grid_recharge);
        GridView.setAdapter(new DashGridViewAdapter(RechargeActivity.this, 1, Itens));
        GridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case btn190:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;
                    case btn380:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;
                    case btn500:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;

                    case btn680:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;

                    case btn760:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;
                    case btn2000:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;
                    case btn5000:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;
                    case btnOutro:
                        Utility.makeText(RechargeActivity.this, "//TODO");
                        break;


                }
            }
        });




    }

    private void getItensList() {
        Itens = new ArrayList<>();

        Itens.add(new DashItem(R.drawable.ic_btn_190));
        Itens.add(new DashItem(R.drawable.ic_btn_380));
        Itens.add(new DashItem(R.drawable.ic_btn_500));
        Itens.add(new DashItem(R.drawable.ic_btn_680));
        Itens.add(new DashItem(R.drawable.ic_btn_760));
        Itens.add(new DashItem(R.drawable.ic_btn_200));
        Itens.add(new DashItem(R.drawable.ic_btn_5000));
        Itens.add(new DashItem(R.drawable.ic_outro_valor));

    }



    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(drawables[position]);
        }
    };
}
