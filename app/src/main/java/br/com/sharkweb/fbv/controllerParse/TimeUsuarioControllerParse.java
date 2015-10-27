package br.com.sharkweb.fbv.controllerParse;

import android.content.Context;

import com.parse.ParseObject;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.TimeUsuarioDAO;
import br.com.sharkweb.fbv.DAOParse.TimeUsuarioDAOParse;
import br.com.sharkweb.fbv.model.TimeUsuario;

public class TimeUsuarioControllerParse {

    private TimeUsuarioDAOParse timeUsuarioDAOParse;

    public TimeUsuarioControllerParse(Context context) {
        timeUsuarioDAOParse = new TimeUsuarioDAOParse(context);
    }

    public ParseObject inserir(TimeUsuario timeUsuario) {
        return timeUsuarioDAOParse.salvar(timeUsuario);
    }

    public ArrayList<TimeUsuario> buscarTimesUsuario(String id_usuario){
        return timeUsuarioDAOParse.buscarTimesUsuario(id_usuario);
    }


}