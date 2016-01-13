package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Posicao;

public class PosicaoDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "posicao";
    private static final String ID = "_id";
    private static final String NOME = "nome";
    private static final String ABREVIATURA = "abreviatura";
    private FBVDAO fbvdao;


    public PosicaoDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(String nome, String abreviatura) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, nome);
        valores.put(ABREVIATURA, abreviatura);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long inserirComId(int id, String nome) {
        ContentValues valores = new ContentValues();
        valores.put(ID, id);
        valores.put(NOME, nome);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(int id, String nome) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, nome);

        String[] whereAgrs = {Integer.toString(id)};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereAgrs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Posicao> selectPosicoes() {
        ArrayList<Posicao> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Posicao> selectPosicaoPorId(int id_posicao) {
        ArrayList<Posicao> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id_posicao + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Posicao> selectPosicaoPorCodigo(String abreviatura) {
        ArrayList<Posicao> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ABREVIATURA + " = '" + abreviatura + "' ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodasPosicoes() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    private ArrayList<Posicao> cursorToArray(Cursor c) {
        ArrayList<Posicao> posicao = new ArrayList<Posicao>();
        while (c.moveToNext()) {
            posicao.add(new Posicao(c.getInt(0), c.getString(1), c.getString(2)));
        }
        return posicao;
    }
}
