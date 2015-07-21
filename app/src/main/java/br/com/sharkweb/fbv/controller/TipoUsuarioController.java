package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TipoUsuarioDAO;
import br.com.sharkweb.fbv.model.Tipo_usuario;

/**
 * @author Tiago Klein
 *         <p/>
 *         Classe responsável somente pelas regras de negócio dos objetos do tipo Login.
 *         Interações com o usuário ou com a tela do dispositivo devem ser implementadas na respectiva View.
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

    public ArrayList<Tipo_usuario> selectTiposUsuarios() {
        return TipousuarioDAO.selectTiposUsuarios();
    }

    public ArrayList<Tipo_usuario> selectTiposUsuariosPorId(int id_tipo) {
        return TipousuarioDAO.selectTipoUsuarioPorId(id_tipo);
    }

    public void excluirTodosTiposUsuarios() {
        TipousuarioDAO.excluirTodosTiposUsuarios();
    }

    public void IniciarTiposUsuarios() {
        ArrayList<Tipo_usuario> tipo_usario = this.selectTiposUsuarios();
        if (tipo_usario.isEmpty()) {
            this.inserir("Administrador");
            this.inserir("Jogador");
            this.inserir("Juiz");
        }
    }
}