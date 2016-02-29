package br.com.sharkweb.fbv.controller;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.sharkweb.fbv.DAO.UsuarioDAO;
import br.com.sharkweb.fbv.DAOParse.UsuarioDAOParse;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.model.Usuario;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * @author Tiago Klein
 */
public class UsuarioController {

    private Funcoes funcoes;

    public UsuarioController(Context context) {
        funcoes = new Funcoes(context);
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

    public void desvincularUsuarioDoTime(ParseObject objectTime) {
        ParseUser.getCurrentUser().getRelation("times").remove(objectTime);
        ArrayList<String> configsTimes = (ArrayList<String>) ParseUser.getCurrentUser().get("configTimes");
        if (configsTimes != null && configsTimes.size() > 0) {
            for (int i = 0; i < configsTimes.size(); i++) {
                Object object = (Object) configsTimes.get(i);
                String time = ((ArrayList<String>) object).get(0);
                if (objectTime.getObjectId().trim().equals(time)) {
                    //configsTimes.remove(i);
                    configsTimes.remove(object);
                    ParseUser.getCurrentUser().add("configTimes", configsTimes);
                    ParseUser.getCurrentUser().saveEventually(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                //funcoes.mostrarToast(1);
                            } else {
                                //funcoes.mostrarToast(2);
                            }
                        }
                    });
                }
            }
        }
    }

    public void alterarConfigTime(String objectIdTime, String itemAlterarao, String valor) {
        ArrayList<String> configsTimes = (ArrayList<String>) ParseUser.getCurrentUser().get("configTimes");
        if (configsTimes != null && configsTimes.size() > 0) {
            for (int i = 0; i < configsTimes.size(); i++) {
                Object object = (Object) configsTimes.get(i);
                String time = ((ArrayList<String>) object).get(0);
                if (objectIdTime.trim().equals(time)) {
                    try {
                        configsTimes.remove(i);
                        String inativo = ((ArrayList<String>) object).get(1);
                        String tipo_user = ((ArrayList<String>) object).get(1);
                        if (itemAlterarao.equals("inativo")) {
                            ((ArrayList<String>) object).set(0, time);
                            ((ArrayList<String>) object).set(1, valor);
                            ((ArrayList<String>) object).set(2, tipo_user);
                            configsTimes.add(i, object.toString());
                        } else if (itemAlterarao.equals("tipoUsuario")) {
                            ((ArrayList<String>) object).set(0, time);
                            ((ArrayList<String>) object).set(1, inativo);
                            ((ArrayList<String>) object).set(2, valor);
                            configsTimes.add(i, object.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ParseUser.getCurrentUser().add("configTimes", configsTimes);
                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                funcoes.mostrarToast(1);
                            } else {
                                funcoes.mostrarToast(2);
                            }
                        }
                    });
                }
            }
        }
    }

    public void atualizarEstatisticas() {
        ParseQuery query = new ParseQuery("posJogoUser");
        query.whereEqualTo("usuario", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {

                } else {

                }
            }
        });
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