package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.PosJogoDAO;
import br.com.sharkweb.fbv.DAO.PosJogoUsuarioDAO;
import br.com.sharkweb.fbv.model.PosJogo;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;

public class PosJogoUsuariosController {

    private PosJogoUsuarioDAO posJogoUsuariosDAO;

    public PosJogoUsuariosController(Context context) {
        posJogoUsuariosDAO = new PosJogoUsuarioDAO(context);
    }

    public long inserir(PosJogoUsuarios posJogoUsuarios) {
        return posJogoUsuariosDAO.inserir(posJogoUsuarios);
    }
    public long veriricarEinserir(PosJogoUsuarios posJogoUsuarios) {

       ArrayList<PosJogoUsuarios> teste = selectPosJogoUsuarios();

        if (!selectPosJogoUsuariosPorPosjogoUsuario(posJogoUsuarios.getId_pos_jogo(),
                posJogoUsuarios.getId_usuario()).isEmpty()){

            return posJogoUsuariosDAO.alterarPorPosJogoUsuario(posJogoUsuarios);
        }else {
            return posJogoUsuariosDAO.inserir(posJogoUsuarios);
        }
    }

    public long alterar(PosJogoUsuarios posJogoUsuarios) {
        return posJogoUsuariosDAO.alterar(posJogoUsuarios);
    }

    public long excluirPosJogoUsuariosPorId(int id) {
        return posJogoUsuariosDAO.excluirPosJogoUsuariosPorId(id);
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuarios() {
        return posJogoUsuariosDAO.selectPosJogoUsuarios();
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuariosPorId(int id) {
        return posJogoUsuariosDAO.selectPosJogoUsuariosPorId(id);
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuariosPorIdPosJogo(int id) {
        return posJogoUsuariosDAO.selectPosJogoUsuariosPorIdPosJogo(id);
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuariosPorPosjogoUsuario(int id_pos_jogo,int id_usuario) {
        return posJogoUsuariosDAO.selectPosJogoUsuariosPorPosjogoUsuario(id_pos_jogo, id_usuario);
    }

    public void excluirTodosPosJogoUsuarios() {
        posJogoUsuariosDAO.excluirTodosPosJogoUsuarios();
    }

    public void excluirPosJogoUsuariosPorIdPosJogo(int id) {
        posJogoUsuariosDAO.excluirPosJogoUsuariosPorIdPosJogo(id);
    }
}