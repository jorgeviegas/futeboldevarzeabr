package br.com.sharkweb.fbv.model;

/**
 * Created by Tiago on 19/07/2015.
 */
public class Usuario {

    public int id;
    public String nome;
    public String codigo;
    public String email;
    public String senha;
    public int id_tipo;
    public int id_posicao;
    public int id_time;
    public Time time;

    public Usuario(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.id_posicao = id_posicao;
        this.id_time = id_time;
        this.email = email;
        this.senha = senha;
        this.id_tipo = id_tipo;
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

    public int getId_time() {
        return id_time;
    }

    public void setId_time(int id_time) {
        this.id_time = id_time;
    }

    public int getId_posicao() {
        return id_posicao;
    }

    public void setId_posicao(int id_posicao) {
        this.id_posicao = id_posicao;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId_tipo() {
        return id_tipo;
    }

    public void setId_tipo(int id_tipo) {
        this.id_tipo = id_tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
