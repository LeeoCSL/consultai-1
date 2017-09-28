package br.com.carregai.carregai2.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.carregai.carregai2.R;
import br.com.carregai.carregai2.adapter.BankItemAdapter;
import br.com.carregai.carregai2.adapter.VerticalSpaceItemDecoration;
import br.com.carregai.carregai2.interfaces.OnItemBankClickListener;
import br.com.carregai.carregai2.model.Bank;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.goodiebag.carouselpicker.CarouselPicker;

public class PaymentFragment extends Fragment implements OnItemBankClickListener {

    private List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();

    private CarouselPicker carouselPicker;

    private Button mContinue;

    private View view;

    private RecyclerView mBanks;

    private List<Bank> banks = new ArrayList<>();

    private BankItemAdapter mAdapter;

    private static final int VERTICAL_ITEM_SPACE = 12;

    private View transferenceUser;
    private LinearLayout btnTransferenceUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.payment_layout, container, false);

        carouselPicker = (CarouselPicker)view.findViewById(R.id.carousel);
        mContinue = (Button)view.findViewById(R.id.btn_continue);

        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.credit_card));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.credit_card));
        imageItems.add(new CarouselPicker.DrawableItem(R.drawable.credit_card));

        banks.add(new Bank("Banco do Brasil", R.drawable.bb_icon, "#FFF300", "#000000"));
        banks.add(new Bank("Santander", R.drawable.santander_logo, "#E52520", "#FFFFFF"));
        banks.add(new Bank("Itaú", R.drawable.itau_logo, "#2E3192", "#FFFFFF"));
        banks.add(new Bank("Caixa Econômica", R.drawable.logo_caixa, "#FF5484", "#000000"));
        banks.add(new Bank("Bradesco", R.drawable.bradesco_logo, "#CD072F", "#FFFFFF"));

        mBanks = (RecyclerView) view.findViewById(R.id.transference_layout);

        mAdapter = new BankItemAdapter(banks, getActivity(), this);
        mBanks.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        mBanks.setAdapter(mAdapter);

        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(getContext(), imageItems, 0);

        final View creditCard = view.findViewById(R.id.credit_card_layout);
        transferenceUser = view.findViewById(R.id.transference_user_layout);
        //final View transference = view.findViewById(R.id.transference_layout);
        btnTransferenceUser = (LinearLayout) view.findViewById(R.id.btn_transference_user);

        final Button btnBack = (Button) btnTransferenceUser.findViewById(R.id.btn_back);
        final Button btnConfirm = (Button) btnTransferenceUser.findViewById(R.id.btn_confirm);

        carouselPicker.setAdapter(imageAdapter);

        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnTransferenceUser.setVisibility(View.GONE);
                        mBanks.setVisibility(View.VISIBLE);
                        transferenceUser.setVisibility(View.GONE);
                        creditCard.setVisibility(View.GONE);
                    }
                });

                switch (position){
                    case 0:

                        break;
                    case 1:
                        mBanks.setVisibility(View.GONE);
                        creditCard.setVisibility(View.VISIBLE);
                        transferenceUser.setVisibility(View.GONE);
                        btnTransferenceUser.setVisibility(View.GONE);
                        mContinue.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mBanks.setVisibility(View.VISIBLE);
                        creditCard.setVisibility(View.GONE);
                        btnTransferenceUser.setVisibility(View.GONE);
                        mContinue.setVisibility(View.GONE);

/*                        final LinearLayout btnBB = (LinearLayout) transference.findViewById(R.id.banco_brasil);

                        btnBB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mBanks.setVisibility(View.GONE);
                                transferenceUser.setVisibility(View.VISIBLE);
                                btnTransferenceUser.setVisibility(View.VISIBLE);
                            }
                        });*/
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }


    @Override
    public void onItemBankClick(Bank bank) {
        mBanks.setVisibility(View.GONE);
        transferenceUser.setVisibility(View.VISIBLE);
        btnTransferenceUser.setVisibility(View.VISIBLE);
    }
}
