package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.UF;

public class UFDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "uf";

    private static final String ID = "_id";
    private static final String UF = "UF";

    private FBVDAO fbvdao;

    public UFDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(UF uf) {
        ContentValues valores = new ContentValues();
        valores.put(UF, uf.getNome());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(UF uf) {
        ContentValues valores = new ContentValues();
        valores.put(UF, uf.getNome());

        String[] whereArgs = {Integer.toString(uf.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<UF> selectUF() {
        ArrayList<UF> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<UF> selectUFPorId(int id) {
        ArrayList<UF> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<UF> selectUFPorDescricao(String uf) {
        ArrayList<UF> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + UF + " = '" + uf + "' ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosUF() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirUFPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<UF> cursorToArray(Cursor c) {
        ArrayList<UF> uf = new ArrayList<UF>();
        while (c.moveToNext()) {
            uf.add(new UF(c.getInt(0), c.getString(1)));
        }
        return uf;
    }
}
