package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Login;

public class LoginDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "login";
    private static final String ID = "_id";
    private static final String ID_PARSE = "id_parse";
    private static final String NOME = "nome";
    private static final String EMAIL = "email";
    private static final String ID_TIPO = "id_tipo";
    private static final String ID_TIME = "id_time";
    private static final String CELULAR = "celular";
    private static final String APELIDO = "apelido";
    private static final String SENHA = "senha";

    private FBVDAO fbvdao;

    public LoginDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Login login) {
        ContentValues valores = new ContentValues();
        valores.put(ID_PARSE, login.getIdParse().trim());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Login login) {
        ContentValues valores = new ContentValues();
        valores.put(ID_PARSE, login.getIdParse());


        String[] whereAgrs = {Integer.toString(login.getId())};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " >= ?", whereAgrs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Login> selecLogin() {
        ArrayList<Login> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA , null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Login> selectLoginPorId(int id_login) {
        ArrayList<Login> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id_login + " ORDER BY " + ID_PARSE, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosLogins() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    private ArrayList<Login> cursorToArray(Cursor c) {
        ArrayList<Login> login = new ArrayList<Login>();
        while (c.moveToNext()) {
            //int id, String idParse, String nome, String email, int id_tipo, int id_time, String celular, String apelido
            login.add(new Login(c.getInt(0), c.getString(1)));
        }
        return login;
    }
}
