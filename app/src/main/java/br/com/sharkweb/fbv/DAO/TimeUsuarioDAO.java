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
    private static final String INATIVO = "inativo";
    private static final String POSICAO = "posicao";

    private FBVDAO fbvdao;

    public TimeUsuarioDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(TimeUsuario timeUsuario) {
        ContentValues valores = new ContentValues();

        valores.put(ID_TIME, timeUsuario.getId_time());
        valores.put(ID_USUARIO, timeUsuario.getId_usuario());
        valores.put(INATIVO, timeUsuario.getInativo());
        valores.put(POSICAO, timeUsuario.getPosicao());


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

    public long inativarUsuario(int id_time, int id_usuario) {
        ContentValues valores = new ContentValues();
        valores.put(INATIVO, 1);
        String[] whereArgs = {Integer.toString(id_usuario),Integer.toString(id_time)};

        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID_USUARIO + " = ? AND "+ID_TIME+" = ? ", whereArgs);
        fbvdao.close();
        return retorno;
    }

    public long ativarUsuario(int id_time, int id_usuario) {
        ContentValues valores = new ContentValues();
        valores.put(INATIVO, 0);
        String[] whereArgs = {Integer.toString(id_usuario),Integer.toString(id_time)};

        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID_USUARIO + " = ? AND "+ID_TIME+" = ? ", whereArgs);
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
        ArrayList<TimeUsuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time + " AND "+INATIVO + " = 0  ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<TimeUsuario> selectTimeUsuarioPorIdTimeeIdUsuario(int id_time, int id_usuario) {
        ArrayList<TimeUsuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_TIME + " = " + id_time + " AND " + ID_USUARIO +" = " +id_usuario+ " ORDER BY " + ID, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<TimeUsuario> selectTimeUsuarioPorIdTimeComInativos(int id_time) {
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

    public long excluirTimeUsuarioPorIdUsuario(int id_usuario, int id_time) {
        String[] whereArgs = {Integer.toString(id_usuario),Integer.toString(id_time)};
        long retorno;
        retorno = fbvdao.getReadableDatabase().delete(NOME_TABELA, ID_USUARIO + " = ? AND "+ID_TIME+" = ? ", whereArgs);
        fbvdao.close();
        return retorno;
    }

    private ArrayList<TimeUsuario> cursorToArray(Cursor c) {
        ArrayList<TimeUsuario> timeUsuario = new ArrayList<TimeUsuario>();

        while (c.moveToNext()) {
            timeUsuario.add(new TimeUsuario(c.getInt(0), c.getInt(1), c.getInt(2),c.getInt(3),c.getString(4)));
        }
        return timeUsuario;
    }
}
