package com.marcosevaristo.tcc001.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.activities.Mapa;
import com.marcosevaristo.tcc001.adapters.MyArrayAdapter;
import com.marcosevaristo.tcc001.dto.ListaLinhasDTO;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AbaBuscar extends Fragment {

    ListView lView;
    Query queryRefNum;
    Query queryRefTitulo;
    MyArrayAdapter adapter;
    ListaLinhasDTO lLinhas = new ListaLinhasDTO();
    List<ValueEventListener> lEventos = new ArrayList<>();
    ProgressBar progressBar;

    public AbaBuscar() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.aba_buscar, container, false);
        setupFloatingActionButton(view);
        return view;
    }

    private void setupListLinhas(String argBusca) {
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lView = (ListView) getActivity().findViewById(R.id.listaLinhas);
        lView.setAdapter(null);
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Mapa.class);
                Bundle bundleAux = new Bundle();
                bundleAux.putSerializable("linha", (Linha)parent.getItemAtPosition(position));
                intent.putExtras(bundleAux);
                startActivity(intent);
            }
        });
        queryRefNum = FirebaseUtils.getLinhasReference().child(argBusca).getRef();
        //queryRefTitulo = FirebaseUtils.getLinhasReference();
        ValueEventListener evento = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map mapValues = (Map) dataSnapshot.getValue();
                if (mapValues != null) {
                    lLinhas = new ListaLinhasDTO();
                    lLinhas.addLinhas(Linha.converteMapParaListaLinhas(mapValues));
                    adapter = new MyArrayAdapter(lLinhas.getArrayListLinhas(), getActivity());
                    adapter.notifyDataSetChanged();
                    lView.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        };

        lEventos.add(evento);
        queryRefNum.addListenerForSingleValueEvent(evento);
        //queryRefTitulo.addListenerForSingleValueEvent(evento);
    }

    private void setupFloatingActionButton(View view) {
        final FloatingActionButton fabSearch = (FloatingActionButton) view.findViewById(R.id.fab_search);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView busca = (TextView) getActivity().findViewById(R.id.etBusca);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(busca.getVisibility() == View.GONE) {
                    busca.setVisibility(View.VISIBLE);
                    busca.requestFocus();
                    imm.showSoftInput(busca, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    String arg = busca.getText().toString();
                    if(!arg.equals("")) {
                        setupListLinhas(busca.getText().toString());
                        busca.setText("");
                    }
                }
            }
        });
    }
}