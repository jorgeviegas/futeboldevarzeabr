package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Login;

public class LoginDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "login";
    private static final String ID = "_id";
    private static final String ID_USUARIO = "id_usuario";
    private FBVDAO fbvdao;

    public LoginDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(int id_usuario) {
        ContentValues valores = new ContentValues();
        valores.put(ID_USUARIO, id_usuario);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long inserirComId(int id, int id_usuario) {
        ContentValues valores = new ContentValues();
        valores.put(ID, id);
        valores.put(ID_USUARIO, id_usuario);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(int id, int id_usuario) {
        ContentValues valores = new ContentValues();
        valores.put(ID_USUARIO, id_usuario);

        String[] whereAgrs = {Integer.toString(id)};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereAgrs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Login> selecLogin() {
        ArrayList<Login> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + ID_USUARIO, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Login> selectLoginPorId(int id_login) {
        ArrayList<Login> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id_login + " ORDER BY " + ID_USUARIO, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosLogins() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    private ArrayList<Login> cursorToArray(Cursor c) {
        ArrayList<Login> login = new ArrayList<Login>();
        while (c.moveToNext()) {
            login.add(new Login(c.getInt(0), c.getInt(1)));
        }
        return login;
    }
}
