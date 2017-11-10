package com.marcosevaristo.tcc001.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.adapters.MunicipiosAdapter;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.dto.ListaMunicipiosDTO;
import com.marcosevaristo.tcc001.model.Municipio;
import com.marcosevaristo.tcc001.utils.CollectionUtils;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;

import java.util.List;
import java.util.Map;

public class SelecionaMunicipio extends AppCompatActivity {

    private ListView lMunicipiosView;
    private ProgressBar progressBar;
    private ListaMunicipiosDTO lMunicipios;
    private MunicipiosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_municipio);
        setupToolbar();
        setupListMunicipios();
    }

    private void setupToolbar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    private void setupListMunicipios() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        lMunicipiosView = (ListView) findViewById(R.id.listaMunicipios);
        lMunicipiosView.setAdapter(null);
        lMunicipiosView.setOnItemClickListener(getOnItemClickListenerSelecionaMunicipio());

        List<Municipio> lMunicipiosSalvos = QueryBuilder.getMunicipios(null);
        if(CollectionUtils.isNotEmpty(lMunicipiosSalvos)) {
            lMunicipios = new ListaMunicipiosDTO();
            lMunicipios.addMunicipios(lMunicipiosSalvos);
            setupListAdapter();
        } else {
            FirebaseUtils.getMunicipiosReference().getRef().addListenerForSingleValueEvent(getEventoBuscaMunicipiosFirebase());
        }
    }

    private ValueEventListener getEventoBuscaMunicipiosFirebase() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map<String, Object>> lMapValues = (List<Map<String, Object>>) dataSnapshot.getValue();
                if (lMapValues != null) {
                    lMunicipios = new ListaMunicipiosDTO();
                    lMunicipios.addMunicipios(Municipio.converteListMapParaListaMunicipios(lMapValues));
                    if(CollectionUtils.isNotEmpty(lMunicipios.getlMunicipios())) {
                        QueryBuilder.insereMunicipios(lMunicipios.getlMunicipios());
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
        adapter = new MunicipiosAdapter(R.layout.municipio_item, lMunicipios.getlMunicipios());
        adapter.notifyDataSetChanged();
        lMunicipiosView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private AdapterView.OnItemClickListener getOnItemClickListenerSelecionaMunicipio() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Municipio municipioSelecionado = lMunicipios.getlMunicipios().get(position);
                QueryBuilder.updateMunicipioAtual(municipioSelecionado);
                App.setMunicipio(municipioSelecionado);

                Toast.makeText(App.getAppContext(), App.getAppContext().getString(R.string.municipio_selecionado_sucesso, municipioSelecionado.getNome()), Toast.LENGTH_LONG).show();
                startActivity(new Intent(App.getAppContext(), MainActivity.class));
            }
        };
    }
}
