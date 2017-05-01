package com.marcosevaristo.tcc001.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.dto.ListaLinhasDTO;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AbaBuscar extends Fragment {

    ListView lView;
    ListaLinhasDTO lLinhas = new ListaLinhasDTO();
    Query queryRef;
    List<ValueEventListener> lEventos = new ArrayList<>();
    ArrayAdapter<Linha> adapter;

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
        if(getArguments() != null && getArguments().size() > 0) {
            String argBusca = getArguments().getString("argBusca");
            setupListLinhas(argBusca);
        }
        return view;
    }

    private void setupListLinhas(String argBusca) {
        adicionaListeners(argBusca);

    }

    private void adicionaListeners(String arg) {
        queryRef = FirebaseUtils.getLinhasReference().child(arg).getRef();
        ValueEventListener evento = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map> mapValues = (List<Map>) dataSnapshot.getValue();
                if(mapValues != null) {
                    listToMap(mapValues);

                    lView = (ListView) getActivity().findViewById(R.id.listaLinhas);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                }

                /*Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                Map<String, Object> mapValues = new HashMap<>();
                while(iterable.iterator().hasNext()) {
                    DataSnapshot dataSnapshot1 = iterable.iterator().next();
                    mapValues.put(dataSnapshot1.getKey(), dataSnapshot1.getValue());
                }
                if(mapValues.size() > 0) {
                    List<Linha> lLinhasAux = new ArrayList<>();
                    lLinhasAux.add(new Linha((List<Carro>) mapValues.get("carros"), (Integer) mapValues.get("numero"),
                            (String) mapValues.get("titulo"), (String) mapValues.get("subtitulo")));
                    lLinhas.addLinhas(lLinhasAux);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

            private void listToMap(List<Map> mapValues) {
                List<Linha> lLinhasAux = new ArrayList<>();
                //lLinhasAux.add(linha);
                lLinhas.addLinhas(lLinhasAux);
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, lLinhas.getlLinhas());
                lView.setAdapter(adapter);
            }
        };

        lEventos.add(evento);
        queryRef.addListenerForSingleValueEvent(evento);
    }

    private void removeListeners() {
        if(CollectionUtils.isNotEmpty(lEventos)) {
            for(ValueEventListener umEvento : lEventos) {
                queryRef.removeEventListener(umEvento);
            }
            lEventos = new ArrayList<>();
        }
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

                    setupListLinhas(busca.getText().toString());
                    busca.setText("");
                }
            }
        });
    }
}