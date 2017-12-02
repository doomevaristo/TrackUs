package com.marcosevaristo.tcc001.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.marcosevaristo.tcc001.App;
import com.marcosevaristo.tcc001.R;
import com.marcosevaristo.tcc001.adapters.MunicipiosAdapter;
import com.marcosevaristo.tcc001.database.QueryBuilder;
import com.marcosevaristo.tcc001.model.Municipio;
import com.marcosevaristo.tcc001.utils.FirebaseUtils;
import com.marcosevaristo.tcc001.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SelecionaMunicipio extends AppCompatActivity {

    private ListView lMunicipiosView;
    private List<Municipio> lMunicipios;

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
        App.showLoadingDialog(this);

        lMunicipiosView = (ListView) findViewById(R.id.listaMunicipios);
        lMunicipiosView.setAdapter(null);
        lMunicipiosView.setOnItemClickListener(getOnItemClickListenerSelecionaMunicipio());

        FirebaseUtils.getMunicipiosReference(null).addValueEventListener(getEventoBuscaMunicipiosFirebase());
    }

    private ValueEventListener getEventoBuscaMunicipiosFirebase() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    lMunicipios = new ArrayList<>();
                    for(DataSnapshot umDataSnapshot : dataSnapshot.getChildren()) {
                        String id = umDataSnapshot.getKey();
                        String nome = StringUtils.toStringSecure(umDataSnapshot.child("nome").getValue());

                        Municipio municipio = new Municipio(id, nome);
                        if(App.getMunicipio() != null && municipio.getId().equals(App.getMunicipio().getId())) {
                            municipio.setEhMunicipioAtual(true);
                        }
                        lMunicipios.add(municipio);
                    }
                    setupListAdapter();
                } else {
                    Toast.makeText(App.getAppContext(), R.string.nenhum_resultado, Toast.LENGTH_LONG).show();
                }
                App.hideLoadingDialog();
                App.toast(R.string.hint_seleciona_municipio);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                App.hideLoadingDialog();
                App.toast(R.string.hint_seleciona_municipio);
            }
        };
    }

    private void setupListAdapter() {
        MunicipiosAdapter adapter = new MunicipiosAdapter(R.layout.municipio_item, lMunicipios);
        adapter.notifyDataSetChanged();
        lMunicipiosView.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener getOnItemClickListenerSelecionaMunicipio() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Municipio municipioSelecionado = lMunicipios.get(position);
                QueryBuilder.updateMunicipioAtual(municipioSelecionado);
                App.setMunicipio(municipioSelecionado);

                Toast.makeText(App.getAppContext(), App.getAppContext().getString(R.string.municipio_selecionado_sucesso, municipioSelecionado.getNome()), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
            }
        };
    }
}
