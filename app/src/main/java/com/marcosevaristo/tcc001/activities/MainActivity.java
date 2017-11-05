package com.marcosevaristo.tcc001.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.adapters.ViewPagerAdapter;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.fragments.AbaBuscar;
import com.marcosevaristo.tcc001.fragments.AbaFavoritos;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setMunicipio(QueryBuilder.getMunicipioAtual());
        if(App.getMunicipio() == null) {
            startActivity(new Intent(App.getAppContext(), SelecionaMunicipio.class));
        } else {
            FirebaseUtils.startReferenceLinhas();
            setContentView(R.layout.activity_main);
            setupToolbar();
            setupTabLayout();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void setupToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void setupTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.abaBuscar));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.abaFavoritos));

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AbaBuscar(), getString(R.string.abaBuscar));
        adapter.addFragment(new AbaFavoritos(), getString(R.string.abaFavoritos));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(getOnPageChangeListener(adapter));
    }

    private ViewPager.OnPageChangeListener getOnPageChangeListener(final ViewPagerAdapter adapter) {
        return new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0) {
                    AbaBuscar abaBuscar = (AbaBuscar) adapter.getItem(position);
                    EditText editText = (EditText) abaBuscar.getView().findViewById(R.id.etBusca);
                    editText.setVisibility(View.GONE);
                    editText.setText("");
                } else if(position == 1) {
                    AbaFavoritos abaFavoritos = (AbaFavoritos) adapter.getItem(position);
                    abaFavoritos.atualizaFavoritos(abaFavoritos.getView());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }
}
