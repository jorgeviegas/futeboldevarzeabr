package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Time;

public class TimeDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "time";
    private static final String NOME_TABELA_VINCULO = "time_usuario";

    private static final String ID = "_id";
    private static final String NOME = "nome";
    private static final String CIDADE = "cidade";
    private static final String ID_UF = "id_uf";
    private FBVDAO fbvdao;

    public TimeDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Time time) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, time.getNome());
        valores.put(CIDADE, time.getCidade());
        valores.put(ID_UF, time.getId_uf());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Time time) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, time.getNome());
        valores.put(CIDADE, time.getCidade());
        valores.put(ID_UF, time.getId_uf());

        String[] whereArgs = {Integer.toString(time.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Time> selectTime() {
        ArrayList<Time> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Time> selectTimePorId(int id) {
        ArrayList<Time> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Time> selectTimePorIdUsuario(int id_usuario) {
        ArrayList<Time> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID +" in (select id_time from time_usuario where id_usuario = " +id_usuario+")  ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosTimes() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirTimePorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<Time> cursorToArray(Cursor c) {
        ArrayList<Time> times = new ArrayList<Time>();
        while (c.moveToNext()) {
            times.add(new Time(c.getInt(0), c.getString(1), c.getString(2),c.getInt(3)));
        }
        return times;
    }
}
