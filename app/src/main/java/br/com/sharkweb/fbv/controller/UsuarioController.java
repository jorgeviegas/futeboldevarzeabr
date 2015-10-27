package br.com.sharkweb.fbv.controller;

import android.content.Context;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sharkweb.fbv.DAO.UsuarioDAO;
import br.com.sharkweb.fbv.DAOParse.UsuarioDAOParse;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.model.Usuario;

import com.parse.ParseObject;

/**
 * @author Tiago Klein
 */
public class UsuarioController {

    private UsuarioDAO usuarioDAO;
    private UsuarioDAOParse usuarioDAOParse;

    public UsuarioController(Context context) {
        usuarioDAO = new UsuarioDAO(context);
        usuarioDAOParse = new UsuarioDAOParse(context);
    }


    public long inserir(Usuario usuario, boolean parse) {
        if (parse) {
            return usuarioDAOParse.salvar(usuario);
        } else {
            return usuarioDAO.inserir(usuario);
        }
    }

    public long alterar(Usuario usuario, boolean parse) {
        if (parse) {
            return usuarioDAOParse.salvar(usuario);
        } else {
            return usuarioDAO.alterar(usuario);
        }
    }

    public long favoritarTime(int id, int id_time) {
        return usuarioDAO.favoritarTime(id, id_time);
    }

    public ArrayList<Usuario> selectUsuarios(boolean parse) {
        if (parse) {
            return usuarioDAOParse.buscarUsuarios("", "", 0);
        } else {
            return usuarioDAO.selectUsuarios();
        }
    }

    public ArrayList<Usuario> selectUsuarioPorEmail(String email, boolean parse) {
        if (parse) {
            return usuarioDAOParse.buscarUsuarios("email", email, 1);
        } else {
            return usuarioDAO.selectUsuarioPorEmail(email);
        }
    }

    public ArrayList<Usuario> selectUsuarioPorEmailouApelido(String email, boolean parse) {
        if (validarEmail(email)) {
            if (parse) {
                return usuarioDAOParse.buscarUsuarios("email", email, 1);
            } else {
                return usuarioDAO.selectUsuarioPorEmail(email);
            }
        } else {
            if (parse) {
                return usuarioDAOParse.buscarUsuarios("apelido", email, 1);
            } else {
                return usuarioDAO.selectUsuarioPorApelido(email);
            }
        }
    }

    public ArrayList<Usuario> selectUsuarioPorApelido(String apelido, boolean parse) {
        if (parse) {
            return usuarioDAOParse.buscarUsuarios("apelido", apelido, 1);
        } else {
            return usuarioDAO.selectUsuarioPorApelido(apelido);
        }
    }

    public ArrayList<Usuario> selectUsuarioPorId(int id_usuario, String id_parse) {
        if (!id_parse.isEmpty()) {
            return usuarioDAOParse.selectUsuarioPorId(id_parse);
        } else {
            return usuarioDAO.selectUsuarioPorId(id_usuario);
        }
    }

    public ArrayList<Usuario> selectUsuarioPorIdParse(String id_parse, boolean parse) {
        if (parse) {
            return usuarioDAOParse.selectUsuarioPorId(id_parse);
        } else {
            return usuarioDAO.selectUsuarioPorIdParse(id_parse);
        }
    }

    public void excluirTodosJogadores() {
        usuarioDAO.excluirTodosUsuarios();
    }

    public boolean validarLogin(Usuario usuario, String senha) {
        if (usuario.getSenha().equals(senha)) {
            return true;
        }
        return false;
    }

    public String validarSenha(String senha, String confirmarSenha) {
        if (!senha.equals(confirmarSenha)) {
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