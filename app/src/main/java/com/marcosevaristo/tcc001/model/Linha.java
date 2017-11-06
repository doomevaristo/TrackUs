package com.marcosevaristo.tcc001.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Linha implements Serializable {
    private Long idSql;
    private Municipio municipio;
    private String numero;
    private String titulo;
    private String subtitulo;
    private List<Carro> carros;
    private boolean ehFavorito = false;

    private static final long serialVersionUID = 1L;

    public Linha() {
    }

    public Linha(List<Carro> carros, String numero, String titulo, String subtitulo) {
        this.carros = carros;
        this.numero = numero;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public Long getIdSql() {
        return idSql;
    }

    public void setIdSql(Long idSql) {
        this.idSql = idSql;
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

    public static List<Linha> converteMapParaListaLinhas(Map<String, Object> lMapLinhas) {
        List<Linha> lLinhas = new ArrayList<>();
        Linha linhaAux = new Linha();
        boolean resultadoUnico = false;
        for(String umaKeyAux : lMapLinhas.keySet()) {
            if(lMapLinhas.get(umaKeyAux) instanceof Map) {
                Map<String, Object> umaLinha = (Map<String, Object>) lMapLinhas.get(umaKeyAux);
                linhaAux = new Linha();
                for(String umAttr : umaLinha.keySet()) {
                    montaUmAtributoDaLinha(linhaAux, umAttr, umaLinha.get(umAttr));
                }
                lLinhas.add(linhaAux);
            } else {
                resultadoUnico = true;
                montaUmAtributoDaLinha(linhaAux, umaKeyAux, lMapLinhas.get(umaKeyAux));
            }
        }
        if(resultadoUnico) lLinhas.add(linhaAux);
        return lLinhas;
    }

    private static void montaUmAtributoDaLinha(Linha linhaAux, String atributo, Object valor) {
        switch(atributo) {
            case "numero":
                linhaAux.setNumero(valor.toString());
                break;
            case "titulo":
                linhaAux.setTitulo(valor.toString());
                break;
            case "subtitulo":
                linhaAux.setSubtitulo(valor.toString());
                break;
            case "carros":
                Collection<Map> mapCarros = ((Map) valor).values();
                List<Carro> lCarrosAux = new ArrayList<>();
                for(Map umCarroMap : mapCarros) {
                    Carro carroAux = new Carro();
                    for(Object umKey : umCarroMap.keySet()) {
                        String umKeyStr = umKey.toString();
                        switch (umKeyStr) {
                            case "location":
                                carroAux.setLocation(umCarroMap.get(umKey).toString());
                                break;
                            case "latitude":
                                carroAux.setLatitude(umCarroMap.get(umKey).toString());
                                break;
                            case "longitude":
                                carroAux.setLongitude(umCarroMap.get(umKey).toString());
                                break;
                            case "id":
                                carroAux.setId(umCarroMap.get(umKey).toString());
                                break;
                            default:
                                break;
                        }
                    }
                    lCarrosAux.add(carroAux);
                }
                linhaAux.setCarros(lCarrosAux);
                break;
            default:
                break;
        }
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

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public boolean ehFavorito() {
        return ehFavorito;
    }

    public void setEhFavorito(boolean ehFavorito) {
        this.ehFavorito = ehFavorito;
    }
}
