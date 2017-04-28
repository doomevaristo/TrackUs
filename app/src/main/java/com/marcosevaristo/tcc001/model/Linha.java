package com.marcosevaristo.tcc001.model;

import android.view.View;

import java.util.List;

public class Linha implements View.OnClickListener{
    private Integer numero;
    private String titulo;
    private String subtitulo;
    private List<Carro> carros;

    public Linha() {
    }

    public Linha(List<Carro> carros, Integer numero, String titulo, String subtitulo) {
        this.carros = carros;
        this.numero = numero;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public Linha(String toString) {
        String aux = toString.substring(0, toString.indexOf("-")-1);
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
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

    /*public List<Carro> getCarros() {
        return carros;
    }

    public void setCarros(List<Carro> carros) {
        this.carros = carros;
    }*/

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
