package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TimeDAO;
import br.com.sharkweb.fbv.DAO.TimeUsuarioDAO;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;
import br.com.sharkweb.fbv.model.Usuario;

public class TimeUsuarioController {

    private TimeUsuarioDAO timeUsuarioDAO;

    public TimeUsuarioController(Context context) {
        timeUsuarioDAO = new TimeUsuarioDAO(context);
    }

    public long inserir(TimeUsuario timeUsuario) {
        return timeUsuarioDAO.inserir(timeUsuario);
    }

    public long alterar(int id, TimeUsuario timeUsuario) {
        return timeUsuarioDAO.alterar(id, timeUsuario);
    }

    public long excluirTimeUsuarioPorId(int id) {
        return timeUsuarioDAO.excluirTimeUsuarioPorId(id);
    }

    public long excluirTimeUsuarioPorIdUsuario(int id_usuario, int id_time) {
        return timeUsuarioDAO.excluirTimeUsuarioPorIdUsuario(id_usuario, id_time);
    }

    public long inativarUsuario(int id_time,int id_usuario) {
        return timeUsuarioDAO.inativarUsuario(id_time, id_usuario);
    }

    public long ativarUsuario(int id_time,int id_usuario) {
        return timeUsuarioDAO.ativarUsuario(id_time, id_usuario);
    }

    public long tornarAdmin(int id_time,int id_usuario) {
        return timeUsuarioDAO.tornarAdmin(id_time, id_usuario);
    }

    public ArrayList<TimeUsuario> selectTimeUsuario() {
        return timeUsuarioDAO.selectTimeUsuario();
    }

    public ArrayList<TimeUsuario> selectTimeUsuarioPorId(int id) {
        return timeUsuarioDAO.selectTimeUsuarioPorId(id);
    }
    public ArrayList<TimeUsuario> selectTimeUsuarioPorIdTime(int id_time) {
        return timeUsuarioDAO.selectTimeUsuarioPorIdTime(id_time);
    }

    public ArrayList<TimeUsuario> selectTimeUsuarioPorIdTimeeIdUsuario(int id_time, int id_usuario) {
        return timeUsuarioDAO.selectTimeUsuarioPorIdTimeeIdUsuario(id_time, id_usuario);
    }


    public ArrayList<TimeUsuario> selectTimeUsuarioPorIdTimeComInativos(int id_time) {
        return timeUsuarioDAO.selectTimeUsuarioPorIdTimeComInativos(id_time);
    }

    public void excluirTodosTimesUsuarios() {
        timeUsuarioDAO.excluirTodosTimesUsuarios();
    }


}