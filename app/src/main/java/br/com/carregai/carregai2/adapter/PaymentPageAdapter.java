package br.com.carregai.carregai2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.carregai.carregai2.activity.PaymentActivity;
import br.com.carregai.carregai2.fragments.PaymentFragment;
import br.com.carregai.carregai2.fragments.ReviewFragment;
import br.com.carregai.carregai2.fragments.ShippingFragment;

public class PaymentPageAdapter extends FragmentPagerAdapter {

    public PaymentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ShippingFragment shippingFragment = new ShippingFragment();
                return shippingFragment;
            case 1:
                PaymentFragment paymentFragment = new PaymentFragment();
                return paymentFragment;
            case 2:
                ReviewFragment reviewFragment = new ReviewFragment();
                return reviewFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PaymentActivity.TOTAL_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return PaymentActivity.SHIPPING_FRAGMENT;

            case 1:
                return PaymentActivity.PAYMENT_FRAGMENT;

            case 2:
                return PaymentActivity.REVIEW_FRAGMENT;

            default:
                return null;
        }
    }
}
