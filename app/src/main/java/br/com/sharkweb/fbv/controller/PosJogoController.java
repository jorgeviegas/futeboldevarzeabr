package br.com.sharkweb.fbv.controller;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.sharkweb.fbv.DAO.PosJogoDAO;
import br.com.sharkweb.fbv.DAO.UFDAO;
import br.com.sharkweb.fbv.PosJogoActivity;
import br.com.sharkweb.fbv.R;
import br.com.sharkweb.fbv.model.Jogo;
import br.com.sharkweb.fbv.model.PosJogo;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;
import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.UF;
import br.com.sharkweb.fbv.model.Usuario;

public class PosJogoController {

    private PosJogoDAO posJogoDAO;
    private PosJogoUsuariosController posJogoUsers;
    private TimeController timeControl;
    private JogoController jogoControl;
    private UsuarioController userControl;

    public PosJogoController(Context context) {
        posJogoDAO = new PosJogoDAO(context);
        posJogoUsers = new PosJogoUsuariosController(context);
        timeControl = new TimeController(context);
        jogoControl = new JogoController(context);
        userControl = new UsuarioController(context);
    }

    public long inserir(PosJogo posJogo) {
        return posJogoDAO.inserir(posJogo);
    }

    public long alterar(PosJogo posJogo) {
        return posJogoDAO.alterar(posJogo);
    }

    public long excluirPosJogoPorId(int id) {
        return posJogoDAO.excluirPosJogoPorId(id);
    }

    public ArrayList<PosJogo> selectPosJogo() {
        return posJogoDAO.selectPosJogo();
    }

    public ArrayList<PosJogo> selectPosJogoPorId(int id) {
        return posJogoDAO.selectPosJogoPorId(id);
    }

    public ArrayList<PosJogo> selectPosJogoPorIdJogo(int id) {
        return posJogoDAO.selectPosJogoPorIdJogo(id);
    }

    public void excluirTodosPosJogo() {
        posJogoDAO.excluirTodosPosJogo();
    }

    public void excluirPosJogoPorIdJogo(int id) {
        Long ret = posJogoDAO.excluirPosJogoPorIdJogo(id);
        if (ret != null && ret > 0) {
            posJogoUsers.excluirPosJogoUsuariosPorIdPosJogo(Integer.valueOf(String.valueOf(ret).trim()));
        }
    }

    public void exibirPlacarJogo(final PosJogo posJogo, Context context, final TextView tv) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pos_jogo_placar);
        Jogo jogo = jogoControl.selectJogoPorId(posJogo.getId_jogo()).get(0);

        Time time1 = timeControl.selectTimePorId(jogo.getId_time()).get(0);
        Time time2 = timeControl.selectTimePorId(jogo.getId_time2()).get(0);

        dialog.setTitle("Placar:");
        final TextView tvNomeTime1 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_nometime1);
        final TextView tvNomeTime2 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_nometime2);
        final TextView tvGolsTime1 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_golstime1);
        final TextView tvGolsTime2 = (TextView) dialog.findViewById(R.id.pos_jogo_placar_golstime2);
        final Button btnconfirmar = (Button) dialog.findViewById(R.id.pos_jogo_placar_btnconfirmar);
        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                posJogo.setQtd_gol_time1(Integer.valueOf(tvGolsTime1.getText().toString().trim()));
                posJogo.setQtd_gol_time2(Integer.valueOf(tvGolsTime2.getText().toString().trim()));
                String placar = String.valueOf(posJogo.getQtd_gol_time1()).trim() +
                        " X " + String.valueOf(posJogo.getQtd_gol_time2()).trim();
                tv.setText(placar);
                dialog.dismiss();
            }
        });

        tvNomeTime1.setText(time1.getNome().toUpperCase().trim().substring(0, 3));
        tvNomeTime2.setText(time2.getNome().toUpperCase().trim().substring(0, 3));
        tvGolsTime1.setText(String.valueOf(posJogo.getQtd_gol_time1()).trim());
        tvGolsTime2.setText(String.valueOf(posJogo.getQtd_gol_time2()).trim());

        dialog.show();
    }

    public void exibirFeedBackPosJogoJogador(final PosJogoUsuarios posJogoUser, Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_pos_jogo_jogador);
        Usuario user = userControl.selectUsuarioPorId(posJogoUser.getId_usuario()).get(0);

        dialog.setTitle(user.getNome().toUpperCase().trim());
        final TextView tvNota = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_nota);
        final TextView tvGols = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_gols);
        final TextView tvCA = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_cartoesamarelo);
        final TextView tvCV = (TextView) dialog.findViewById(R.id.pos_jogo_jogador_cartoesvermelho);

        if (posJogoUser.getNota() > 0) {
            tvNota.setText(String.valueOf(posJogoUser.getNota()).trim());
        }
        if (posJogoUser.getQtd_gol() > 0) {
            tvGols.setText(String.valueOf(posJogoUser.getQtd_gol()).trim());
        }
        if (posJogoUser.getQtd_cartao_amarelo() > 0) {
            tvCA.setText(String.valueOf(posJogoUser.getQtd_cartao_amarelo()).trim());
        }
        if (posJogoUser.getQtd_cartao_vermelho() > 0) {
            tvCV.setText(String.valueOf(posJogoUser.getQtd_cartao_vermelho()).trim());
        }

        final Button btnconfirmar = (Button) dialog.findViewById(R.id.pos_jogo_jogador_btnconfirmar);
        btnconfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!tvNota.getText().toString().trim().isEmpty()) {
                    posJogoUser.setNota(Integer.valueOf(tvNota.getText().toString().trim()));
                }
                if (!tvGols.getText().toString().isEmpty()) {
                    posJogoUser.setQtd_gol(Integer.valueOf(tvGols.getText().toString().trim()));
                }
                if (!tvCA.getText().toString().isEmpty()) {
                    posJogoUser.setQtd_cartao_amarelo(Integer.valueOf(tvCA.getText().toString().trim()));
                }
                if (!tvCV.getText().toString().isEmpty()) {
                    posJogoUser.setQtd_cartao_vermelho(Integer.valueOf(tvCV.getText().toString().trim()));
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}