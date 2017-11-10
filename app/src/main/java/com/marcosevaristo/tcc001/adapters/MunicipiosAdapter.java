package com.marcosevaristo.tcc001.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.components.BotaoFavorito;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.model.Municipio;

import java.util.ArrayList;
import java.util.List;

public class MunicipiosAdapter extends ArrayAdapter<Municipio> {
    private List<Municipio> lMunicipios = new ArrayList<>();
    private int layoutResId;

    public MunicipiosAdapter(int layoutResId, List<Municipio> lMunicipios) {
        super(App.getAppContext(), layoutResId, lMunicipios);
        this.layoutResId = layoutResId;
        this.lMunicipios = lMunicipios;
    }

    @Override
    public int getCount() {
        return lMunicipios.size();
    }

    @Override
    public Municipio getItem(int pos) {
        return lMunicipios.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MunicipioHolder municipioHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) App.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResId, parent, false);
            municipioHolder = new MunicipioHolder();
            municipioHolder.texto = (TextView)view.findViewById(R.id.municipioText);
            municipioHolder.textoMunAtual = (TextView)view.findViewById(R.id.municipioAtualText);

            view.setTag(municipioHolder);
        } else {
            municipioHolder = (MunicipioHolder) view.getTag();
        }

        Municipio municipio = lMunicipios.get(position);
        if(municipio != null) {
            municipioHolder.texto.setText(municipio.getNome());
            if(municipio.isEhMunicipioAtual()) municipioHolder.textoMunAtual.setText(App.getAppContext().getString(R.string.municipioAtualDet));
        }
        return view;
    }

    private static class MunicipioHolder {
        TextView texto;
        TextView textoMunAtual;
    }
}
