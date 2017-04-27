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
            if(this.findLinhaByNumeroOuTitulo(umaLinha.getNumero().toString()) == null
                    && this.findLinhaByNumeroOuTitulo(umaLinha.getTitulo()) == null) {
                lLinhas.add(umaLinha);
            }
        }
    }

    public List<Linha> getlLinhas() {
        return lLinhas;
    }

    /**
     * Retorna a linha da lista que tenha o número ou título informado como argumento. Retorna null se não encontrar.
     * @param arg
     * @return
     */
    public Linha findLinhaByNumeroOuTitulo(String arg) {
        if(CollectionUtils.isNotEmpty(this.lLinhas)) {
            for(Linha umaLinha : this.lLinhas) {
                if(umaLinha.getNumero().toString().equals(arg) || umaLinha.getTitulo().equals(arg)) {
                    return umaLinha;
                }
            }
        }
        return null;
    }
}
