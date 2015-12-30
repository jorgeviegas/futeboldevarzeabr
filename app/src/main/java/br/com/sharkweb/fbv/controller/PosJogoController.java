package br.com.sharkweb.fbv.controller;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import br.com.sharkweb.fbv.DAO.PosJogoDAO;
import br.com.sharkweb.fbv.DAO.UFDAO;
import br.com.sharkweb.fbv.PosJogoActivity;
import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.Util.Funcoes;
import br.com.sharkweb.fbv.Util.FuncoesParse;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.PosJogo;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.UF;
import br.com.sharkweb.fbv.model.Usuario;

public class PosJogoController {

    private Funcoes funcoes;
    private boolean PosJogoNovo = false;

    public PosJogoController(Context context) {
        funcoes = new Funcoes(context);
    }

    public void exibirPlacarJogo(final ParseObject jogo, Context context, final TextView tv) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pos_jogo_placar);

        dialog.setTitle("Placar:");
        final TextView tvNomeTime1 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_nometime1);
        final TextView tvNomeTime2 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_nometime2);
        final TextView tvGolsTime1 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_golstime1);
        final TextView tvGolsTime2 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_golstime2);
        final Button btnconfirmar = (Button) dialog.findViewById(R.id.pos_jogo_placar_btnconfirmar);
        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                jogo.put("qtdGolsTime1", Integer.valueOf(tvGolsTime1.getText().toString().trim()));
                jogo.put("qtdGolsTime2", Integer.valueOf(tvGolsTime2.getText().toString().trim()));
                jogo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            funcoes.mostrarToast(1);
                        } else {
                            funcoes.mostrarToast(2);
                        }
                    }
                });

                String placar = tvGolsTime1.getText().toString().trim() +
                        " X " + tvGolsTime2.getText().toString().trim();
                tv.setText(placar);
                dialog.dismiss();
            }
        });

        tvNomeTime1.setText(jogo.getString("nomeTime").toUpperCase().trim().substring(0, 3));
        tvNomeTime2.setText(jogo.getString("nomeTime2").toUpperCase().trim().substring(0, 3));
        tvGolsTime1.setText(String.valueOf(jogo.getInt("qtdGolsTime1")).trim());
        tvGolsTime2.setText(String.valueOf(jogo.getInt("qtdGolsTime2")).trim());

        dialog.show();
    }

    public void exibirFeedBackPosJogoJogador(final ParseObject posJogoUser, final ParseObject jogo, final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pos_jogo_jogador);

        final TextView tvNota = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_nota);
        final TextView tvGols = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_gols);
        final TextView tvCA = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_cartoesamarelo);
        final TextView tvCV = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_cartoesvermelho);

        if (posJogoUser.getClass().getName().equals("com.parse.ParseUser")) {
            dialog.setTitle(posJogoUser.getString("nome").toUpperCase().trim());
            PosJogoNovo = true;
        } else {
            dialog.setTitle(posJogoUser.getString("nomeUsuario").toUpperCase().trim());
            if (posJogoUser.getInt("nota") > 0) {
                tvNota.setText(String.valueOf(posJogoUser.getInt("nota")).trim());
            } else {
                tvNota.setText("0");
            }
            if (posJogoUser.getInt("qtdGol") > 0) {
                tvGols.setText(String.valueOf(posJogoUser.getInt("qtdGol")).trim());
            } else {
                tvGols.setText("0");
            }
            if (posJogoUser.getInt("qtdCartaoAmarelo") > 0) {
                tvCA.setText(String.valueOf(posJogoUser.getInt("qtdCartaoAmarelo")).trim());
            } else {
                tvCA.setText("0");
            }
            if (posJogoUser.getInt("qtdCartaoVermelho") > 0) {
                tvCV.setText(String.valueOf(posJogoUser.getInt("qtdCartaoVermelho")).trim());
            } else {
                tvCV.setText("0");
            }
        }

        final Button btnconfirmar = (Button) dialog.findViewById(R.id.pos_jogo_jogador_btnconfirmar);

        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ParseObject novoPosJogo = new ParseObject("posJogoUser");

                if (!PosJogoNovo) {
                    novoPosJogo.setObjectId(posJogoUser.getObjectId());
                    novoPosJogo.put("nomeUsuario", posJogoUser.getString("nomeUsuario"));
                    novoPosJogo.put("usuario", posJogoUser.get("usuario"));
                } else {
                    novoPosJogo.put("nomeUsuario", posJogoUser.getString("nome"));
                    novoPosJogo.put("usuario", posJogoUser);
                }
                if (!tvNota.getText().toString().trim().isEmpty()) {
                    novoPosJogo.put("nota", Integer.valueOf(tvNota.getText().toString().trim()));
                }
                if (!tvGols.getText().toString().isEmpty()) {
                    novoPosJogo.put("qtdGol", Integer.valueOf(tvGols.getText().toString().trim()));
                }
                if (!tvCA.getText().toString().isEmpty()) {
                    novoPosJogo.put("qtdCartaoAmarelo", Integer.valueOf(tvCA.getText().toString().trim()));
                }
                if (!tvCV.getText().toString().isEmpty()) {
                    novoPosJogo.put("qtdCartaoVermelho", Integer.valueOf(tvCV.getText().toString().trim()));
                }
                novoPosJogo.put("jogo", jogo);
                final Dialog progresso = FuncoesParse.showProgressBar(context, "Salvando...");
                final ParseObject finalNovoPosJogo = novoPosJogo;
                if (novoPosJogo.getObjectId() == null || novoPosJogo.getObjectId().isEmpty()) {
                    PosJogoNovo = true;
                }
                novoPosJogo.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        FuncoesParse.dismissProgressBar(progresso);
                        if (e == null) {
                            funcoes.mostrarToast(1);
                            if (PosJogoNovo) {
                                jogo.getRelation("posJogoUser").add(finalNovoPosJogo);
                                jogo.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e != null) {
                                            jogo.saveEventually();
                                        }
                                    }
                                });
                            }
                        } else {
                            funcoes.mostrarToast(2);
                        }
                    }
                });
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}