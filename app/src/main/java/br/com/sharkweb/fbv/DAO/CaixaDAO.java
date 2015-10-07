package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Caixa;

public class CaixaDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "caixa";
    // private static final String NOME_TABELA_VINCULO = "time_usuario";

    private static final String ID = "_id";
    private static final String ID_TIME = "id_time";
    private static final String SALDO = "saldo";
    private static final String VISIVEL = "visivel";

    private FBVDAO fbvdao;

    public CaixaDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Caixa caixa) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, caixa.getId_time());
        valores.put(SALDO, caixa.getSaldo());
        valores.put(VISIVEL, caixa.getVisivel());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Caixa caixa) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, caixa.getId_time());
        valores.put(SALDO, caixa.getSaldo());
        valores.put(VISIVEL, caixa.getVisivel());

        String[] whereArgs = {Integer.toString(caixa.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Caixa> selectCaixas() {
        ArrayList<Caixa> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID_TIME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Caixa> selectCaixaPorId(int id) {
        ArrayList<Caixa> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Caixa> selectCaixasPorIdTime(int id_time) {
        ArrayList<Caixa> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosCaixas() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirCaixaPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<Caixa> cursorToArray(Cursor c) {
        ArrayList<Caixa> caixas = new ArrayList<Caixa>();
        while (c.moveToNext()) {
            caixas.add(new Caixa(c.getInt(0),c.getInt(1),c.getDouble(2),c.getInt(3)));
        }
        return caixas;
    }
}
