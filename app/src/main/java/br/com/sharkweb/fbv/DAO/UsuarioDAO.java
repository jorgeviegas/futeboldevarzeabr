package br.com.sharkweb.fbv.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import br.com.sharkweb.fbv.model.Usuario;

public class UsuarioDAO {

    /*Dados da tabela*/
    private static final String NOME_TABELA = "usuario";
    private static final String ID = "_id";
    private static final String CODIGO = "codigo";
    private static final String NOME = "nome";
    private static final String EMAIL = "email";
    private static final String SENHA = "senha";
    private static final String ID_POSICAO = "id_posicao";
    private static final String ID_TIME = "id_time";
    private static final String ID_TIPO = "id_tipo";
    private static final String CELULAR = "celular";
    private static final String APELIDO = "apelido";

    private FBVDAO fbvdao;

    public UsuarioDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, nome);
        valores.put(CODIGO, codigo);
        valores.put(EMAIL, email);
        valores.put(SENHA, senha);
        valores.put(ID_TIPO, id_tipo);
        valores.put(ID_POSICAO, id_posicao);
        valores.put(ID_TIME, id_time);
        valores.put(CELULAR, celular);
        valores.put(APELIDO, apelido);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long inserirComId(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido) {
        ContentValues valores = new ContentValues();
        valores.put(ID, id);
        valores.put(NOME, nome);
        valores.put(CODIGO, codigo);
        valores.put(EMAIL, email);
        valores.put(SENHA, senha);
        valores.put(ID_TIPO, id_tipo);
        valores.put(ID_POSICAO, id_posicao);
        valores.put(ID_TIME, id_time);
        valores.put(CELULAR, celular);
        valores.put(APELIDO, apelido);

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(int id, String nome, String codigo, String email, String senha, int id_tipo, int id_posicao, int id_time, String celular, String apelido) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, nome);
        valores.put(CODIGO, codigo);
        valores.put(EMAIL, email);
        valores.put(SENHA, senha);
        valores.put(ID_TIPO, id_tipo);
        valores.put(ID_POSICAO, id_posicao);
        valores.put(ID_TIME, id_time);
        valores.put(CELULAR, celular);
        valores.put(APELIDO, apelido);

        String[] whereAgrs = {Integer.toString(id)};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID + " = ?", whereAgrs);
        fbvdao.close();
        return retorno;
    }

    public ArrayList<Usuario> selectUsuarios() {
        ArrayList<Usuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Usuario> selectUsuarioPorEmail(String email) {
        ArrayList<Usuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + EMAIL + " =  '" + email + "' ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Usuario> selectUsuarioPorApelido(String apelido) {
        ArrayList<Usuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + APELIDO + " =  '" + apelido + "' ORDER BY " + NOME, null));
        fbvdao.close();
        return c;
    }

    public ArrayList<Usuario> selectUsuarioPorId(int id_usuario) {
        ArrayList<Usuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID + " = " + id_usuario, null));
        fbvdao.close();
        return c;
    }

    public void excluirTodosUsuarios() {
        fbvdao.getReadableDatabase().delete(NOME_TABELA, null, null);
    }

    //id, nome, email,codigo, senha, id_tipo, id_posicao, id_time
    private ArrayList<Usuario> cursorToArray(Cursor c) {
        ArrayList<Usuario> usuario = new ArrayList<Usuario>();
        while (c.moveToNext()) {
            usuario.add(new Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getString(8), c.getString(9)));
        }
        return usuario;
    }
}
