package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Time;
import br.com.sharkweb.fbv.model.TimeUsuario;

public class TimeUsuarioDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "time_usuario";
    private static final String ID = "_id";
    private static final String ID_TIME = "id_time";
    private static final String ID_USUARIO = "id_usuario";

    private FBVDAO fbvdao;

    public TimeUsuarioDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(TimeUsuario timeUsuario) {
        ContentValues valores = new ContentValues();

        valores.put(ID_TIME, timeUsuario.getId_time());
        valores.put(ID_USUARIO, timeUsuario.getId_usuario());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(int id, TimeUsuario timeUsuario) {
        ContentValues valores = new ContentValues();

        valores.put(ID_TIME, timeUsuario.getId_time());
        valores.put(ID_USUARIO, timeUsuario.getId_usuario());

        String[] whereArgs = {Integer.toString(id)};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<TimeUsuario> selectTimeUsuario() {
        ArrayList<TimeUsuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID_TIME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<TimeUsuario> selectTimeUsuarioPorId(int id) {
        ArrayList<TimeUsuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<TimeUsuario> selectTimeUsuarioPorIdTime(int id_time) {
        ArrayList<TimeUsuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time + " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosTimesUsuarios() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    public long excluirTimeUsuarioPorId(int id) {
        String[] whereArgs = {Integer.toString(id)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID + " = ?", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<TimeUsuario> cursorToArray(Cursor c) {
        ArrayList<TimeUsuario> timeUsuario = new ArrayList<TimeUsuario>();
        while (c.moveToNext()) {
            timeUsuario.add(new TimeUsuario(c.getInt(0), c.getInt(1), c.getInt(2)));
        }
        return timeUsuario;
    }
}
