package br.com.sharkweb.fbv.model;

import android.content.Context;

import br.com.sharkweb.fbv.controller.TipoUsuarioController;

/**
 * Created by Tiago on 19/07/2015.
 */

public class Usuario {

    private int id;
    private String nome;
    private String codigo;
    private String email;
    private String senha;
    private int id_tipo;
    private int id_posicao;
    private int id_time;
    private String celular;
    private String apelido;
    private Time time;
    private String idParse;

    public Usuario(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido, String id_parse) {
        this.id = id;
        this.nome = nome;
        this.codigo = codigo;
        this.id_posicao = id_posicao;
        this.id_time = id_time;
        this.email = email;
        this.senha = senha;
        this.id_tipo = id_tipo;
        this.celular = celular;
        this.apelido = apelido;
        this.idParse = id_parse;
    }

    public Usuario(String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido, String id_parse) {
        this.idParse = id_parse;
        this.nome = nome;
        this.codigo = codigo;
        this.id_posicao = id_posicao;
        this.id_time = id_time;
        this.email = email;
        this.senha = senha;
        this.id_tipo = id_tipo;
        this.celular = celular;
        this.apelido = apelido;
    }

    public Usuario() {
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

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getIdParse() {
        return idParse;
    }

    public void setIdParse(String idParse) {
        this.idParse = idParse;
    }
}
