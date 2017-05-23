package com.marcosevaristo.tcc001.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.components.BotaoFavorito;

import java.util.ArrayList;

public class MyArrayAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;



    public MyArrayAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView listItemText = (TextView)view.findViewById(R.id.linhaBuscadaText);
        listItemText.setText(list.get(position));

        setupBotaoFavorito(view);

        return view;
    }

    private void setupBotaoFavorito(View view) {
        BotaoFavorito botaoFavorito = (BotaoFavorito) view.findViewById(R.id.botaoFavorito);
        botaoFavorito.setFavorite(true, true);
        botaoFavorito.setOnFavoriteChangeListener(new BotaoFavorito.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(BotaoFavorito buttonView, boolean favorite) {

            }
        });
    }
}
