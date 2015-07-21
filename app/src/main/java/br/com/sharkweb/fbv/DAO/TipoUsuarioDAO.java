package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Tipo_usuario;

public class TipoUsuarioDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "tipo_usuario";
    private static final String ID = "_id";
    private static final String TIPO = "tipo";
    private FBVDAO fbvdao;


    public TipoUsuarioDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(String tipo) {
        ContentValues valores = new ContentValues();
        valores.put(TIPO, tipo);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long inserirComId(int id, String tipo) {
        ContentValues valores = new ContentValues();
        valores.put(ID, id);
        valores.put(TIPO, tipo);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(int id, String tipo) {
        ContentValues valores = new ContentValues();
        valores.put(TIPO, tipo);

        String[] whereAgrs = {Integer.toString(id)};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereAgrs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Tipo_usuario> selectTiposUsuarios() {
        ArrayList<Tipo_usuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + TIPO, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Tipo_usuario> selectTipoUsuarioPorId(int id_tipo) {
        ArrayList<Tipo_usuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id_tipo + " ORDER BY " + TIPO, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosTiposUsuarios() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    private ArrayList<Tipo_usuario> cursorToArray(Cursor c) {
        ArrayList<Tipo_usuario> tipo_usuario = new ArrayList<Tipo_usuario>();
        while (c.moveToNext()) {
            tipo_usuario.add(new Tipo_usuario(c.getInt(0), c.getString(1)));
        }
        return tipo_usuario;
    }
}
