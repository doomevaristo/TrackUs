package com.marcosevaristo.tcc001.model;

import java.io.Serializable;
import java.util.Map;

public class Municipio implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idSql;
    private String id;
    private String nome;
    private Map<String, Linha> linhas;
    private boolean ehMunicipioAtual;

    public Municipio(){}

    public Municipio(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getIdSql() {
        return idSql;
    }

    public void setIdSql(Long idSql) {
        this.idSql = idSql;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Map<String, Linha> getLinhas() {
        return linhas;
    }

    public void setLinhas(Map<String, Linha> linhas) {
        this.linhas = linhas;
    }

    public boolean isEhMunicipioAtual() {
        return ehMunicipioAtual;
    }

    public void setEhMunicipioAtual(boolean ehMunicipioAtual) {
        this.ehMunicipioAtual = ehMunicipioAtual;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
