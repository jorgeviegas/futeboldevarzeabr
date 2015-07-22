package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TipoUsuarioDAO;
import br.com.sharkweb.fbv.model.TipoUsuario;
/**
 * @author Tiago Klein
 *
 * Classe respons�vel somente pelas regras de neg�cio dos objetos do tipo Login.
 * Intera��es com o usu�rio ou com a tela do dispositivo devem ser implementadas na respectiva View.
 */
public class TipoUsuarioController {

    private TipoUsuarioDAO TipousuarioDAO;
    public TipoUsuarioController(Context context) {
        TipousuarioDAO = new TipoUsuarioDAO(context);
    }

    public long inserir(String tipo) {
        return TipousuarioDAO.inserir(tipo);
    }

    public long inserirComId(int id, String tipo) {
        return TipousuarioDAO.inserirComId(id, tipo);
    }

    public long alterar(int id, String tipo) {
        return TipousuarioDAO.alterar(id, tipo);
    }

    public ArrayList<TipoUsuario> selectTiposUsuarios() {
        return TipousuarioDAO.selectTiposUsuarios();
    }

    public ArrayList<TipoUsuario> selectTiposUsuariosPorId(int id_tipo) {
        return TipousuarioDAO.selectTipoUsuarioPorId(id_tipo);
    }

    public void excluirTodosTiposUsuarios() {
        TipousuarioDAO.excluirTodosTiposUsuarios();
    }

    public void IniciarTiposUsuarios() {
        ArrayList<TipoUsuario> tipo_usario = this.selectTiposUsuarios();
        if (tipo_usario.isEmpty()) {
            this.inserir("Administrador");
            this.inserir("Jogador");
            this.inserir("Juiz");
        }
    }
}