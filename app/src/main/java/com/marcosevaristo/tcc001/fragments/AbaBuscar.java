package com.marcosevaristo.tcc001.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.UserManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.activities.Mapa;
import com.marcosevaristo.tcc001.activities.SelecionaMunicipio;
import com.marcosevaristo.tcc001.adapters.LinhasAdapter;
import com.marcosevaristo.tcc001.adapters.NumericKeyBoardTransformationMethod;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;
import com.marcosevaristo.tcc001.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AbaBuscar extends Fragment implements View.OnClickListener, EditText.OnEditorActionListener {

    private View view;
    private ListView lView;
    private List<Linha> lLinhas;
    private String ultimaBusca;

    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private Boolean isFabOpen = false;
    private FloatingActionButton fabMenu,fabTrocaMunicipio,fabSearch;

    public AbaBuscar() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.aba_buscar, container, false);
        ultimaBusca = StringUtils.emptyString();
        setupListLinhas(ultimaBusca);
        setupFloatingActionButton(view);
        return view;
    }

    private void setupListLinhas(String argBusca) {
        App.showLoadingDialog(getActivity());

        lView = (ListView) view.findViewById(R.id.listaLinhas);
        lView.setAdapter(null);
        lView.setOnItemClickListener(getOnItemClickListenerOpenMap());

        Query query = FirebaseUtils.getLinhasReference(App.getMunicipio().getId(), null).orderByChild("numero");
        if(StringUtils.isNotBlank(argBusca)){
            query = query.equalTo(argBusca);
        }
        query.addListenerForSingleValueEvent(getEventoBuscaLinhasFirebase());
        ultimaBusca = argBusca;
    }

    private ValueEventListener getEventoBuscaLinhasFirebase() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    lLinhas = new ArrayList<>();
                    for(DataSnapshot umDataSnapshot : dataSnapshot.getChildren()) {
                        String id = umDataSnapshot.getKey();
                        String numero = umDataSnapshot.child("numero").getValue().toString();
                        String titulo = umDataSnapshot.child("titulo").getValue().toString();
                        String subtitulo = umDataSnapshot.child("subtitulo").getValue().toString();

                        Linha umaLinha = new Linha(id, numero, titulo, subtitulo);
                        umaLinha.setEhFavorito(CollectionUtils.isNotEmpty(QueryBuilder.getFavoritos(umaLinha.getId())));
                        umaLinha.setMunicipio(App.getMunicipio());
                        lLinhas.add(umaLinha);
                    }
                    setupListAdapter();
                } else {
                    Toast.makeText(App.getAppContext(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                }
                App.hideLoadingDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                App.hideLoadingDialog();
            }
        };
    }

    private void setupListAdapter() {
        LinhasAdapter adapter = new LinhasAdapter(R.layout.item_da_busca, lLinhas);
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
        fabMenu = (FloatingActionButton) view.findViewById(R.id.fab_menu);
        fabTrocaMunicipio = (FloatingActionButton) view.findViewById(R.id.fab_troca_municipio);
        fabSearch = (FloatingActionButton) view.findViewById(R.id.fab_search);

        fab_open = AnimationUtils.loadAnimation(App.getAppContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(App.getAppContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(App.getAppContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(App.getAppContext(),R.anim.rotate_backward);

        fabMenu.setOnClickListener(this);
        fabTrocaMunicipio.setOnClickListener(this);
        fabSearch.setOnClickListener(this);
    }

    public void atualizaBusca(boolean executaBusca) {
        EditText editText = (EditText) view.findViewById(R.id.etBusca);
        editText.setVisibility(View.GONE);
        editText.setText(StringUtils.emptyString());

        if(executaBusca) {
            lLinhas = new ArrayList<>();
            lView = null;
            setupListLinhas(ultimaBusca);
        } else {
            if(CollectionUtils.isNotEmpty(lLinhas)) {
                List<Linha> lFavoritos;
                for(Linha umaLinha : lLinhas) {
                    lFavoritos = QueryBuilder.getFavoritos(umaLinha.getId());
                    umaLinha.setEhFavorito(CollectionUtils.isNotEmpty(lFavoritos));
                }
            }
        }
        setupListAdapter();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        animateFAB();
        switch (id){
            case R.id.fab_menu:
                break;
            case R.id.fab_search:
                TextInputEditText busca = (TextInputEditText) getActivity().findViewById(R.id.etBusca);
                InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                ((TextInputLayout)busca.getParent().getParent()).setVisibility(View.VISIBLE);
                busca.setOnEditorActionListener(this);
                busca.requestFocus();
                busca.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                busca.setTransformationMethod(new NumericKeyBoardTransformationMethod());
                busca.setTypeface(Typeface.SANS_SERIF);
                imm.showSoftInput(busca, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.fab_troca_municipio:
                startActivityForResult(new Intent(App.getAppContext(), SelecionaMunicipio.class),0);
                break;
        }
    }

    public void animateFAB(){
        if(isFabOpen){
            fabMenu.startAnimation(rotate_backward);
            fabSearch.startAnimation(fab_close);
            fabTrocaMunicipio.startAnimation(fab_close);

            fabSearch.setClickable(false);
            fabTrocaMunicipio.setClickable(false);
            isFabOpen = false;
        } else {
            fabMenu.startAnimation(rotate_forward);
            fabSearch.startAnimation(fab_open);
            fabTrocaMunicipio.startAnimation(fab_open);

            fabSearch.setClickable(true);
            fabTrocaMunicipio.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_NEXT
                || actionId == EditorInfo.IME_ACTION_DONE) {
            setupListLinhas(v.getText().toString());
            return true;
        }
        return false;
    }
}