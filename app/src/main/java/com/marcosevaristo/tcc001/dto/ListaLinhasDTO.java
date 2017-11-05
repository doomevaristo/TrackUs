package com.marcosevaristo.tcc001.dto;

import com.marcosevaristo.tcc001.model.Linha;
import com.marcosevaristo.tcc001.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ListaLinhasDTO {

    private List<Linha> lLinhas;

    public void addLinhas(List<Linha> lista) {
        if(CollectionUtils.isEmpty(lLinhas)) {
            lLinhas = new ArrayList<>();
        }
        for(Linha umaLinha : lista) {
            if(findLinhaByNumeroOuTitulo(umaLinha.getNumero()) == null
                    && this.findLinhaByNumeroOuTitulo(umaLinha.getTitulo()) == null) {
                lLinhas.add(umaLinha);
            }
        }
    }

    public List<Linha> getlLinhas() {
        return lLinhas;
    }

    private Linha findLinhaByNumeroOuTitulo(String arg) {
        if(CollectionUtils.isNotEmpty(this.lLinhas)) {
            for(Linha umaLinha : this.lLinhas) {
                if(umaLinha.getNumero().equals(arg) || umaLinha.getTitulo().equals(arg)) {
                    return umaLinha;
                }
            }
        }
        return null;
    }
}
