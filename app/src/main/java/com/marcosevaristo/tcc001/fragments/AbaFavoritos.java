package com.marcosevaristo.tcc001.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.marcosevaristo.tcc001.app.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.activities.Mapa;
import com.marcosevaristo.tcc001.adapters.LinhasAdapter;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.model.Linha;

import java.util.ArrayList;
import java.util.List;


public class AbaFavoritos extends Fragment{
    private View view;
    private ListView lView;
    private List<Linha> lLinhas;

    public AbaFavoritos() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.aba_favoritos, container, false);
        setupListLinhas();
        return view;
    }

    private void setupListLinhas() {
        lView = (ListView) view.findViewById(R.id.listaLinhasFavoritas);
        lView.setOnItemClickListener(getOnItemClickListenerOpenMap());
        lLinhas = new ArrayList<>();
        lLinhas.addAll(QueryBuilder.getFavoritos(null));
        setupListAdapter();
    }

    private void setupListAdapter() {
        LinhasAdapter adapter = new LinhasAdapter(R.layout.item_dos_favoritos, lLinhas);
        adapter.notifyDataSetChanged();
        lView.setAdapter(adapter);
    }

    public void atualizaFavoritos() {
        setupListLinhas();
    }

    private AdapterView.OnItemClickListener getOnItemClickListenerOpenMap() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(App.getAppContext(), Mapa.class);
                Bundle bundleAux = new Bundle();
                bundleAux.putSerializable("linha", (Linha)parent.getItemAtPosition(position));
                intent.putExtras(bundleAux);
                startActivity(intent);
            }
        };
    }
}