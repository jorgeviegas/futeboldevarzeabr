package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.LoginDAO;
import br.com.sharkweb.fbv.model.Login;

/**
 * @author Tiago Klein
 *         <p/>
 *         Classe respons�vel somente pelas regras de neg�cio dos objetos do tipo Login.
 *         Intera��es com o usu�rio ou com a tela do dispositivo devem ser implementadas na respectiva View.
 */
public class LoginController {

    private LoginDAO loginDAO;

    public LoginController(Context context) {
        loginDAO = new LoginDAO(context);
    }

    public long inserir(Login login) {
        return loginDAO.inserir(login);
    }

    public long alterar(Login login) {
        return loginDAO.alterar(login);
    }

    public ArrayList<Login> selecLogin() {
        return loginDAO.selecLogin();
    }

    public ArrayList<Login> selectLoginPorId(int id_usuario) {
        return loginDAO.selectLoginPorId(id_usuario);
    }

    public void excluirTodosLogins() {
        loginDAO.excluirTodosLogins();
    }
}