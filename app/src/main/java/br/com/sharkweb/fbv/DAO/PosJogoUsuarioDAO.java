package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.PosJogo;
import br.com.sharkweb.fbv.model.PosJogoUsuarios;

public class PosJogoUsuarioDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "pos_jogo_usuario";

    private static final String ID = "_id";
    private static final String ID_POS_JOGO = "id_pos_jogo";
    private static final String ID_USUARIO = "id_usuario";
    private static final String QTD_GOL = "qtd_gol";
    private static final String QTD_CARTAO_AMARELO = "qtd_cartao_amarelo";
    private static final String QTD_CARTAO_VERMELHO = "qtd_cartao_vermelho";
    private static final String NOTA = "nota";

    private FBVDAO fbvdao;

    public PosJogoUsuarioDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(PosJogoUsuarios posJogoUsuarios) {
        ContentValues valores = new ContentValues();
        valores.put(ID_POS_JOGO, posJogoUsuarios.getId_pos_jogo());
        valores.put(ID_USUARIO, posJogoUsuarios.getId_usuario());
        valores.put(QTD_GOL, posJogoUsuarios.getQtd_gol());
        valores.put(QTD_CARTAO_AMARELO, posJogoUsuarios.getQtd_cartao_amarelo());
        valores.put(QTD_CARTAO_VERMELHO, posJogoUsuarios.getQtd_cartao_vermelho());
        valores.put(NOTA, posJogoUsuarios.getNota());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }


    public long alterar(PosJogoUsuarios posJogoUsuarios) {
        ContentValues valores = new ContentValues();
        valores.put(ID_POS_JOGO, posJogoUsuarios.getId_pos_jogo());
        valores.put(ID_USUARIO, posJogoUsuarios.getId_usuario());
        valores.put(QTD_GOL, posJogoUsuarios.getQtd_gol());
        valores.put(QTD_CARTAO_AMARELO, posJogoUsuarios.getQtd_cartao_amarelo());
        valores.put(QTD_CARTAO_VERMELHO, posJogoUsuarios.getQtd_cartao_vermelho());
        valores.put(NOTA, posJogoUsuarios.getNota());

        String[] whereArgs = {Integer.toString(posJogoUsuarios.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public long alterarPorPosJogoUsuario(PosJogoUsuarios posJogoUsuarios) {
        ContentValues valores = new ContentValues();
        valores.put(ID_POS_JOGO, posJogoUsuarios.getId_pos_jogo());
        valores.put(ID_USUARIO, posJogoUsuarios.getId_usuario());
        valores.put(QTD_GOL, posJogoUsuarios.getQtd_gol());
        valores.put(QTD_CARTAO_AMARELO, posJogoUsuarios.getQtd_cartao_amarelo());
        valores.put(QTD_CARTAO_VERMELHO, posJogoUsuarios.getQtd_cartao_vermelho());
        valores.put(NOTA, posJogoUsuarios.getNota());

        String[] whereArgs = {Integer.toString(posJogoUsuarios.getId_pos_jogo()),
                Integer.toString(posJogoUsuarios.getId_usuario())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID_POS_JOGO + " = ? AND "+ID_USUARIO+" = ? ", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuarios() {
        ArrayList<PosJogoUsuarios> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID_POS_JOGO, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuariosPorId(int id) {
        ArrayList<PosJogoUsuarios> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuariosPorIdPosJogo(int id_pos_jogo) {
        ArrayList<PosJogoUsuarios> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_POS_JOGO + " = " + id_pos_jogo + " ORDER BY " + ID_POS_JOGO, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<PosJogoUsuarios> selectPosJogoUsuariosPorPosjogoUsuario(int id_pos_jogo, int id_usuario) {
        ArrayList<PosJogoUsuarios> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_POS_JOGO + " = " + id_pos_jogo +
                " AND "+ID_USUARIO + " = " + id_usuario + " ORDER BY " + ID_POS_JOGO, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosPosJogoUsuarios() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirPosJogoUsuariosPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public long excluirPosJogoUsuariosPorIdPosJogo(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<PosJogoUsuarios> cursorToArray(Cursor c) {
        ArrayList<PosJogoUsuarios> posJogoUsuarios = new ArrayList<PosJogoUsuarios>();
        while (c.moveToNext()) {
            posJogoUsuarios.add(new PosJogoUsuarios(c.getInt(0), c.getInt(1),c.getInt(2),c.getInt(3),c.getInt(4),c.getInt(5),c.getInt(6)));
        }
        return posJogoUsuarios;
    }
}
