package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.PosJogo;
import br.com.sharkweb.fbv.model.UF;

public class PosJogoDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "pos_jogo";

    private static final String ID = "_id";
    private static final String ID_JOGO = "id_jogo";
    private static final String QTD_GOL_TIME1 = "qtd_gol_time1";
    private static final String QTD_GOL_TIME2 = "qtd_gol_time2";

    private FBVDAO fbvdao;

    public PosJogoDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(PosJogo posJogo) {
        ContentValues valores = new ContentValues();
        valores.put(ID_JOGO, posJogo.getId_jogo());
        valores.put(QTD_GOL_TIME1, posJogo.getQtd_gol_time1());
        valores.put(QTD_GOL_TIME2, posJogo.getQtd_gol_time2());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(PosJogo posJogo) {
        ContentValues valores = new ContentValues();
        valores.put(ID_JOGO, posJogo.getId_jogo());
        valores.put(QTD_GOL_TIME1, posJogo.getQtd_gol_time1());
        valores.put(QTD_GOL_TIME2, posJogo.getQtd_gol_time2());

        String[] whereArgs = {Integer.toString(posJogo.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<PosJogo> selectPosJogo() {
        ArrayList<PosJogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID_JOGO, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<PosJogo> selectPosJogoPorId(int id) {
        ArrayList<PosJogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<PosJogo> selectPosJogoPorIdJogo(int id_jogo) {
        ArrayList<PosJogo> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_JOGO + " = " + id_jogo + " ORDER BY " + ID_JOGO, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosPosJogo() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirPosJogoPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public long excluirPosJogoPorIdJogo(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<PosJogo> cursorToArray(Cursor c) {
        ArrayList<PosJogo> posJogo = new ArrayList<PosJogo>();
        while (c.moveToNext()) {
            posJogo.add(new PosJogo(c.getInt(0), c.getInt(1),c.getInt(2),c.getInt(3)));
        }
        return posJogo;
    }
}
