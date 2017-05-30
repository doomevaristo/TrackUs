package com.marcosevaristo.tcc001.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.components.BotaoFavorito;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.model.Linha;

import java.util.ArrayList;
import java.util.List;

public class LinhasAdapter extends ArrayAdapter<Linha> {
    private List<Linha> lLinhas = new ArrayList<Linha>();
    private int layoutResId;
    private Context ctx;

    public LinhasAdapter(Context ctx, int layoutResId, List<Linha> lLinhas) {
        super(ctx, layoutResId, lLinhas);
        this.layoutResId = layoutResId;
        this.ctx = ctx;
        this.lLinhas = lLinhas;
    }

    @Override
    public int getCount() {
        return lLinhas.size();
    }

    @Override
    public Linha getItem(int pos) {
        return lLinhas.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LinhaHolder linhaHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResId, parent, false);

            linhaHolder = new LinhaHolder();
            linhaHolder.texto = (TextView)view.findViewById(R.id.linhaBuscadaText);

            view.setTag(linhaHolder);
        } else {
            linhaHolder = (LinhaHolder) view.getTag();
        }

        Linha linha = lLinhas.get(position);
        if(linha != null) {
            linhaHolder.texto.setText(linha.toString());
            linhaHolder.botaoFavorito = setupBotaoFavorito(view, position);
        }

        return view;
    }

    private BotaoFavorito setupBotaoFavorito(View view, int posicao) {
       BotaoFavorito botaoFavorito = (BotaoFavorito) view.findViewById(R.id.botaoFavorito);
       if(botaoFavorito != null) {

           botaoFavorito.setFavorite(lLinhas.get(posicao).ehFavorito(), true);
           botaoFavorito.setPosicao(posicao);
           botaoFavorito.setOnFavoriteChangeListener(new BotaoFavorito.OnFavoriteChangeListener() {
               @Override
               public void onFavoriteChanged(BotaoFavorito buttonView, boolean favorite) {
                    if(favorite) {
                        QueryBuilder.insereFavorito(lLinhas.get(buttonView.getPosicao()));
                    } else {
                        QueryBuilder.deletaFavorito(lLinhas.get(buttonView.getPosicao()));
                    }
               }
           });
       }
        return botaoFavorito;
    }

    private static class LinhaHolder {
        TextView texto;
        BotaoFavorito botaoFavorito;
    }
}
