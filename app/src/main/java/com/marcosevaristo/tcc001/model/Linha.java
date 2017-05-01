package com.marcosevaristo.tcc001.model;

import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Linha implements View.OnClickListener{
    private String numero;
    private String titulo;
    private String subtitulo;
    private List<Carro> carros;

    public Linha() {
    }

    public Linha(List<Carro> carros, String numero, String titulo, String subtitulo) {
        this.carros = carros;
        this.numero = numero;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public Linha(String toString) {
        String aux = toString.substring(0, toString.indexOf("-")-1);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public List<Carro> getCarros() {
        return carros;
    }

    public void setCarros(List<Carro> carros) {
        this.carros = carros;
    }

    public List<Linha> converteMapParaListaLinhas(List<Map> lMapLinhas) {
        List<Linha> lLinhas = new ArrayList<>();
        for(Map umItem : lMapLinhas) {
            String numeroAux = null;
            String tituloAux = null;
            String subTituloAux = null;
            String location = null;
            String latitude = null;
            String longitude = null;
            String id = null;
            List<Carro> lCarrosAux = new ArrayList<>();
            for(Object umaKeyAux : umItem.keySet()) {
                String umaKey = umaKeyAux.toString();
                switch(umaKey) {
                    case "numero":
                        numeroAux = umItem.get(umaKeyAux).toString();
                        break;
                    case "titulo":
                        tituloAux = umItem.get(umaKeyAux).toString();
                        break;
                    case "subtitulo":
                        subTituloAux = umItem.get(umaKeyAux).toString();
                        break;
                    case "carros":
                        Collection<Map> mapCarros = ((Map) umItem.get(umaKeyAux)).values();
                        for(Map umCarroMap : mapCarros) {
                            Collection<Map> atributosColl = umCarroMap.values();
                            for(Map umAtributo : atributosColl) {
                                for(Object key : umAtributo.keySet()) {
                                    String keyStr = key.toString();
                                    switch(keyStr) {
                                        case "location":
                                            location = umAtributo.get(key).toString();
                                            break;
                                        case "latitude":
                                            latitude = umAtributo.get(key).toString();
                                            break;
                                        case "longitude":
                                            longitude = umAtributo.get(key).toString();
                                            break;
                                        case "id":
                                            id = umAtributo.get(key).toString();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                lCarrosAux.add(new Carro(id, longitude, latitude, location));
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            lLinhas.add(new Linha(lCarrosAux, numeroAux, tituloAux, subTituloAux));
        }
        return lLinhas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.numero);
        sb.append(" - ");
        sb.append(this.titulo);
        if(this.subtitulo != null) {
            sb.append(" - ");
            sb.append(this.subtitulo);
        }
        return sb.toString();
    }

    @Override
    public void onClick(View v) {

    }
}
