package br.com.carregai.carregai2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.carregai.carregai2.MainActivity;
import br.com.carregai.carregai2.fragments.OrdersFragment;
import br.com.carregai.carregai2.fragments.ServicesFragment;

/**
 * Created by renan.boni on 09/08/2017.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ServicesFragment reqServices = new ServicesFragment();
                return reqServices;

            case 1:
                OrdersFragment reqOrders = new OrdersFragment();
                return reqOrders;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return MainActivity.TOTAL_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return MainActivity.SERVICES_FRAGMENT;
            case 1:
                return MainActivity.ORDERS_FRAGMENT;
        }
        return null;
    }
}
