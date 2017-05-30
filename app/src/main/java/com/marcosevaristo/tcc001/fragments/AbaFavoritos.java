package com.marcosevaristo.tcc001.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.adapters.LinhasAdapter;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.dto.ListaLinhasDTO;


public class AbaFavoritos extends Fragment{
    ListView lView;
    LinhasAdapter adapter;
    ListaLinhasDTO lLinhas = new ListaLinhasDTO();

    public AbaFavoritos() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aba_favoritos, container, false);
        setupListLinhas(view);
        return view;
    }

    private void setupListLinhas(View view) {
        lView = (ListView) view.findViewById(R.id.listaLinhasFavoritas);
        lLinhas = new ListaLinhasDTO();
        lLinhas.addLinhas(QueryBuilder.getFavoritos(null));
        adapter = new LinhasAdapter(App.getAppContext(), R.layout.item_dos_favoritos, lLinhas.getlLinhas());
        adapter.notifyDataSetChanged();
        lView.setAdapter(adapter);
    }
}