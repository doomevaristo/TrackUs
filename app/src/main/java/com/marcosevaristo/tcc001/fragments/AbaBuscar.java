package com.marcosevaristo.tcc001.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.activities.Mapa;
import com.marcosevaristo.tcc001.adapters.LinhasAdapter;
import com.marcosevaristo.tcc001.adapters.NumericKeyBoardTransformationMethod;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.dto.ListaLinhasDTO;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;
import com.marcosevaristo.tcc001.utils.StringUtils;

import java.util.List;
import java.util.Map;


public class AbaBuscar extends Fragment {

    private View view;
    private ListView lView;
    private LinhasAdapter adapter;
    private ListaLinhasDTO lLinhas = new ListaLinhasDTO();
    private ProgressBar progressBar;

    public AbaBuscar() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.aba_buscar, container, false);
        setupListLinhas("");
        setupFloatingActionButton(view);
        return view;
    }

    private void setupListLinhas(String argBusca) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        lView = (ListView) view.findViewById(R.id.listaLinhas);
        lView.setAdapter(null);
        lView.setOnItemClickListener(getOnItemClickListenerOpenMap());

        List<Linha> lLinhasSalvas = QueryBuilder.getLinhas(argBusca);
        if(CollectionUtils.isNotEmpty(lLinhasSalvas)) {
            lLinhas = new ListaLinhasDTO();
            lLinhas.addLinhas(lLinhasSalvas);
            setupListAdapter();
        } else {
            FirebaseUtils.getLinhasReference().child(argBusca).getRef().addListenerForSingleValueEvent(getEventoBuscaLinhasFirebase());
        }
        progressBar.setVisibility(View.GONE);
    }

    private ValueEventListener getEventoBuscaLinhasFirebase() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> mapValues = (Map<String, Object>) dataSnapshot.getValue();
                if (mapValues != null) {
                    lLinhas = new ListaLinhasDTO();
                    lLinhas.addLinhas(Linha.converteMapParaListaLinhas(mapValues));
                    if(CollectionUtils.isNotEmpty(lLinhas.getlLinhas())) {
                        for(Linha umaLinha : lLinhas.getlLinhas()) {
                            umaLinha.setMunicipio(App.getMunicipio());
                        }
                        QueryBuilder.insereLinhas(lLinhas.getlLinhas());
                    }
                    setupListAdapter();
                } else {
                    Toast.makeText(App.getAppContext(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        };
    }

    private void setupListAdapter() {
        adapter = new LinhasAdapter(R.layout.item_da_busca, lLinhas.getlLinhas());
        adapter.notifyDataSetChanged();
        lView.setAdapter(adapter);
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

    private void setupFloatingActionButton(View view) {
         view.findViewById(R.id.fab_search).setOnClickListener(getOnClickListenerFAB());
    }

    private View.OnClickListener getOnClickListenerFAB() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView busca = (TextView) view.findViewById(R.id.etBusca);
                InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(busca.getVisibility() == View.GONE) {
                    exibeComponenteDeBusca(busca, imm);
                } else {
                    String arg = busca.getText().toString();
                    if(StringUtils.isNotBlank(arg)) {
                        setupListLinhas(busca.getText().toString());
                        escondeComponenteDeBusca(busca, imm);
                    }
                }
            }

            private void exibeComponenteDeBusca(TextView busca, InputMethodManager imm) {
                busca.setVisibility(View.VISIBLE);
                busca.requestFocus();
                busca.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                busca.setTransformationMethod(new NumericKeyBoardTransformationMethod());
                busca.setTypeface(Typeface.SANS_SERIF);
                imm.showSoftInput(busca, InputMethodManager.SHOW_IMPLICIT);
            }

            private void escondeComponenteDeBusca(TextView busca, InputMethodManager imm) {
                busca.setText("");
                busca.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(busca.getWindowToken(), 0);
            }
        };
    }
}