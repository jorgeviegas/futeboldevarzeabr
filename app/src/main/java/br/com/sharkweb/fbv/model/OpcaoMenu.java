package br.com.sharkweb.fbv.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Tiago on 22/08/2015.
 */
public class OpcaoMenu {

    int id;
    String opcao;
    String descricao;
    int iconeOpcao;

    public OpcaoMenu(int id, String opcao, String descricao, int iconeOpcao) {
        this.id = id;
        this.opcao = opcao;
        this.descricao = descricao;
        this.iconeOpcao = iconeOpcao;
    }
    public OpcaoMenu(String opcao, String descricao, int iconeOpcao) {
        this.id = id;
        this.opcao = opcao;
        this.descricao = descricao;
        this.iconeOpcao = iconeOpcao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIconeOpcao() {
        return iconeOpcao;
    }

    public void setIconeOpcao(int iconeOpcao) {
        this.iconeOpcao = iconeOpcao;
    }
}
