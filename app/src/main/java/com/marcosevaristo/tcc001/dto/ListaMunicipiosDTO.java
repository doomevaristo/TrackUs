package com.marcosevaristo.tcc001.dto;

import com.marcosevaristo.tcc001.model.Municipio;
import com.marcosevaristo.tcc001.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ListaMunicipiosDTO {

    private List<Municipio> lMunicipios;

    public void addMunicipios(List<Municipio> lista) {
        if(CollectionUtils.isEmpty(lMunicipios)) {
            lMunicipios = new ArrayList<>();
        }
        for(Municipio umMunicipio : lista) {
            if(findMunicipioById(umMunicipio.getId()) == null) {
                lMunicipios.add(umMunicipio);
            }
        }
    }

    public List<Municipio> getlMunicipios() {
        return lMunicipios;
    }

    private Municipio findMunicipioById(Long arg) {
        if(CollectionUtils.isNotEmpty(this.lMunicipios)) {
            for(Municipio umMunicipio : this.lMunicipios) {
                if(umMunicipio.getId().equals(arg)) {
                    return umMunicipio;
                }
            }
        }
        return null;
    }
}
