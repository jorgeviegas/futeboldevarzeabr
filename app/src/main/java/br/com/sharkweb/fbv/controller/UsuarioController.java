package br.com.sharkweb.fbv.controller;

import android.content.Context;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    public long inserir(String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido) {
        return usuarioDAO.inserir(nome, codigo, email, senha, id_tipo, id_posicao, id_time, celular, apelido);
    }

    public long inserirComId(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido) {
        return usuarioDAO.inserirComId(id, nome, codigo, email, senha, id_tipo, id_posicao, id_time, celular, apelido);
    }

    public long alterar(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido) {
        return usuarioDAO.alterar(id, nome, codigo, email, senha, id_tipo, id_posicao, id_time, celular, apelido);
    }

    public long favoritarTime(int id, int id_time){
        return usuarioDAO.favoritarTime(id, id_time);
    }

    public ArrayList<Usuario> selectUsuarios() {
        return usuarioDAO.selectUsuarios();
    }

    public ArrayList<Usuario> selectUsuarioPorEmail(String email) {
        return usuarioDAO.selectUsuarioPorEmail(email);
    }

    public ArrayList<Usuario> selectUsuarioPorEmailouApelido(String email) {
        if (validarEmail(email)){
            return usuarioDAO.selectUsuarioPorEmail(email);
        }else{
            return usuarioDAO.selectUsuarioPorApelido(email);
        }
    }

    public ArrayList<Usuario> selectUsuarioPorApelido(String apelido) {
        return usuarioDAO.selectUsuarioPorApelido(apelido);
    }

    public ArrayList<Usuario> selectUsuarioPorId(int id_usuario) {
        return usuarioDAO.selectUsuarioPorId(id_usuario);
    }

    public void inicializarUsuarios(){
        if (selectUsuarios().isEmpty()){
            inserir("Tiago Klein","","kleintiagomail@gmail.com","tiago",1,1,0,"5194303838","kleintiago");
            inserir("Jorge Viegas","","jorgematheusv@gmail.com","jorge",1,1,0,"0000000000","joegeviegas");
        }
    }

    public void excluirTodosJogadores() {
        usuarioDAO.excluirTodosUsuarios();
    }

    public boolean validarLogin(String email, String senha) {

        ArrayList<Usuario> logins2 = selectUsuarios();

        ArrayList<Usuario> logins = new ArrayList<>();

        if (validarEmail(email)){
           logins = selectUsuarioPorEmail(email);
        }else{
            logins = selectUsuarioPorApelido(email);
        }

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

    public boolean validarEmail(String email) {
        if ((email == null) || (email.trim().length() == 0))
            return false;

        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
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