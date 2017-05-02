package com.marcosevaristo.tcc001.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.marcosevaristo.tcc001.fragments.AbaBuscar;
import com.marcosevaristo.tcc001.fragments.AbaFavoritos;

public class Pager extends FragmentStatePagerAdapter {

    private int tabCount;

    public Pager(FragmentManager fm) {
        super(fm);
    }

    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                AbaBuscar abaBuscar = new AbaBuscar();
                return abaBuscar;
            case 1:
                AbaFavoritos abaFavoritos = new AbaFavoritos();
                return abaFavoritos;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
