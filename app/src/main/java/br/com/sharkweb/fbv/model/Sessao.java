package br.com.sharkweb.fbv.model;

import com.parse.ParseObject;

/**
 * Created by Tiago on 18/12/2015.
 */
public class Sessao {

    private int id;
    private ParseObject objeto;
    private String nomeObjeto;

    public Sessao(int id, ParseObject objeto, String nomeObjeto) {
        this.id = id;
        this.objeto = objeto;
        this.nomeObjeto = nomeObjeto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParseObject getObjeto() {
        return objeto;
    }

    public void setObjeto(ParseObject objeto) {
        this.objeto = objeto;
    }

    public String getNomeObjeto() {
        return nomeObjeto;
    }

    public void setNomeObjeto(String nomeObjeto) {
        this.nomeObjeto = nomeObjeto;
    }
}
