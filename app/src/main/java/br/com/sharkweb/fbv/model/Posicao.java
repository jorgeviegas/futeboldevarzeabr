package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Posicao {

    public int id;
    public String nome;

    public Posicao(int i, String n) {
        this.id = i;
        this.nome = n;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
