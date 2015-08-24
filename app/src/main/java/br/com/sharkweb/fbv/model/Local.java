package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Local {

    private int id;
    private String nome;
    private String endereco;
    private int numero;
    private String cidade;
    private int id_uf;

    public Local(int id, String nome,String endereco, int numero, String cidade, int id_uf) {
        this.nome = nome;
        this.cidade = cidade;
        this.id_uf = id_uf;
        this.id = id;
        this.endereco = endereco;
        this.numero = numero;
    }

    public Local(String nome,String endereco,int numero, String cidade, int id_uf) {
        this.nome = nome;
        this.cidade = cidade;
        this.id_uf = id_uf;
        this.endereco = endereco;
        this.numero = numero;
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getId_uf() {
        return id_uf;
    }

    public void setId_uf(int id_uf) {
        this.id_uf = id_uf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
