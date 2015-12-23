package br.com.sharkweb.fbv.controller;

import android.app.Dialog;
import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import br.com.sharkweb.fbv.DAO.JogoDAO;
import br.com.sharkweb.fbv.DAO.MovimentoDAO;
import br.com.sharkweb.fbv.Util.Constantes;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.model.Caixa;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.Movimento;
import br.com.sharkweb.fbv.model.Usuario;

public class MovimentoController {

    private MovimentoDAO movimentoDAO;
    private CaixaController caixaControl;
    private UsuarioController userControl;
    private Funcoes funcoes;
    private Context context;

    public MovimentoController(Context context) {
        movimentoDAO = new MovimentoDAO(context);
        caixaControl = new CaixaController(context);
        userControl = new UsuarioController(context);
        funcoes = new Funcoes(context);
        this.context = context;
    }

    public long inserir(Movimento movimento) {
        return movimentoDAO.inserir(movimento);
    }

    public long alterar(Movimento movimento) {
        return movimentoDAO.alterar(movimento);
    }

    public ArrayList<Movimento> selectMovimentos() {
        return movimentoDAO.selectMovimentos();
    }

    public ArrayList<Movimento> selectMovimentosPorId(int id) {
        return movimentoDAO.selectMovimentosPorId(id);
    }

    public ArrayList<Movimento> selectMovimentosPorIdCaixa(int id_caixa) {
        return movimentoDAO.selectMovimentosPorIdCaixa(id_caixa);
    }

    public void criarMovimento(String tipo, final ParseObject time, double valor, String nomeUsuario, String obs) {
        String historico = "";
        if (tipo.equals("R")) {
            historico = "Retirada manual";
            time.put("valorEmCaixa", time.getDouble("valorEmCaixa") - valor);
        } else if (tipo.equals("E")) {
            historico = "Entrada manual";
            time.put("valorEmCaixa", time.getDouble("valorEmCaixa") + valor);
        } else if (tipo.equals("M")) {
            time.put("valorEmCaixa", time.getDouble("valorEmCaixa") + valor);
            historico = "Pgto. Mensalidade";
            if (!nomeUsuario.isEmpty()) {
                historico = historico + ": \n" + nomeUsuario.trim();
            }
        }
        if (!obs.isEmpty()) {
            historico = obs.trim();
        }

        final Dialog progresso = FuncoesParse.showProgressBar(this.context, "Criando movimento...");
        final ParseObject movimento = new ParseObject("movimento");
        movimento.put("time", time);
        movimento.put("historico", historico);
        movimento.put("valor", valor);
        movimento.put("tipo", tipo);
        movimento.put("usuario", ParseUser.getCurrentUser());
        movimento.saveInBackground(new SaveCallback() {
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    time.getRelation("movimentos").add(movimento);
                    time.saveInBackground(new SaveCallback() {
                        public void done(com.parse.ParseException e) {
                            FuncoesParse.dismissProgressBar(progresso);
                            if (e == null) {
                                funcoes.mostrarToast(1);
                            } else {
                                funcoes.mostrarToast(2);
                            }
                        }
                    });

                } else {
                    FuncoesParse.dismissProgressBar(progresso);
                    funcoes.mostrarToast(2);
                }
            }
        });

    }

}