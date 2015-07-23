package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.UsuarioDAO;
import br.com.sharkweb.fbv.model.Usuario;

/**
 * @author Tiago Klein
 *
 * Classe respons�vel somente pelas regras de neg�cio dos objetos do tipo Login.
 * Intera��es com o usu�rio ou com a tela do dispositivo devem ser implementadas na respectiva View.
 */
public class UsuarioController {

    private UsuarioDAO usuarioDAO;
    public UsuarioController(Context context) {
        usuarioDAO = new UsuarioDAO(context);
    }


    public long inserir(String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time) {
        return usuarioDAO.inserir(nome, codigo, email, senha, id_tipo, id_posicao, id_time);
    }

    public long inserirComId(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time) {
        return usuarioDAO.inserirComId(id, nome, codigo, email, senha, id_tipo, id_posicao, id_time);
    }

    public long alterar(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time) {
        return usuarioDAO.alterar(id, nome, codigo, email, senha, id_tipo, id_posicao, id_time);
    }

    public ArrayList<Usuario> selectUsuarios() {
        return usuarioDAO.selectUsuarios();
    }

    public ArrayList<Usuario> selectUsuarioPorEmail(String email) {
        return usuarioDAO.selectUsuarioPorEmail(email);
    }

    public ArrayList<Usuario> selectUsuarioPorId(int id_usuario) {
        return usuarioDAO.selectUsuarioPorId(id_usuario);
    }

    public void excluirTodosJogadores() {
        usuarioDAO.excluirTodosUsuarios();
    }

    public boolean validarLogin(String email, String senha) {

        ArrayList<Usuario> logins = selectUsuarioPorEmail(email);
        ArrayList<Usuario> login2 = selectUsuarios();

        if (logins.size() > 0) {
            Usuario login = logins.get(0);
            if (login.getSenha().equals(senha)) {
                return true;
            }
        }

        return false;
    }

    public String validarSenha(String senha,String confirmarSenha){
        if (!senha.equals(confirmarSenha)){
            return "Senha e Confirmar senha devem ser iguais.";
        }
        return "";
    }
    /*public String validarEAlterarNovaSenha(Login loginAtual, String senhaAntiga, String novaSenha, String repetirNovaSenha){

        if(!novaSenha.equals(repetirNovaSenha))
            return "Nova senha e Repetir nova senha devem ser iguais.";
        if(!loginAtual.getSenha().equals(senhaAntiga))
            return "Senha antiga incorreta.";
        if(loginAtual.getSenha().equals(novaSenha))
            return "Senha antiga e Nova senha s�o iguais.";

        alterar(loginAtual.getId(), loginAtual.getUsuario(), novaSenha);

        return "ok";
    }*/
}