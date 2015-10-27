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
    private static final String ID_PARSE = "id_parse";

    private FBVDAO fbvdao;

    public UsuarioDAO(Context context) {
        fbvdao = FBVDAO.getInstance(context);
    }

    public long inserir(Usuario usuario) {
        ContentValues valores = new ContentValues();
        valores.put(NOME, usuario.getNome().trim());
        valores.put(CODIGO, usuario.getCodigo().trim());
        valores.put(EMAIL, usuario.getEmail().trim());
        valores.put(SENHA, usuario.getSenha().trim());
        valores.put(ID_TIPO, usuario.getId_tipo());
        valores.put(ID_POSICAO, usuario.getId_posicao());
        valores.put(ID_TIME, usuario.getId_time());
        valores.put(CELULAR, usuario.getCelular().trim());
        valores.put(APELIDO, usuario.getApelido().trim());
        valores.put(ID_PARSE, usuario.getIdParse().trim());

        long retorno = fbvdao.getWritableDatabase().insert(NOME_TABELA, null, valores);
        fbvdao.close();
        return retorno;
    }

    public long alterar(Usuario usuario) {
        ContentValues valores = new ContentValues();
        String id_parse_old = usuario.getCodigo().trim();
        valores.put(NOME, usuario.getNome().trim());
        //Campo não utilizado
        valores.put(CODIGO, "");
        valores.put(EMAIL, usuario.getEmail().trim());
        valores.put(SENHA, usuario.getSenha().trim());
        valores.put(ID_TIPO, usuario.getId_tipo());
        valores.put(ID_POSICAO, usuario.getId_posicao());
        valores.put(ID_TIME, usuario.getId_time());
        valores.put(CELULAR, usuario.getCelular().trim());
        valores.put(APELIDO, usuario.getApelido().trim());
        valores.put(ID_PARSE, usuario.getIdParse().trim());

        //Foi utilizado o campo codigo do usuario para guardar o IdParse antigo do objeto a ser atualizado.
        String[] whereAgrs = {id_parse_old};
        int retorno = fbvdao.getWritableDatabase().update(NOME_TABELA, valores, ID_PARSE +" = ? ", whereAgrs);
        fbvdao.close();
        return retorno;
    }

    public long favoritarTime(int id, int id_time) {
        ContentValues valores = new ContentValues();
        valores.put(ID_TIME, id_time);

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

    public ArrayList<Usuario> selectUsuarioPorIdParse(String id_parse) {
        ArrayList<Usuario> c = cursorToArray(fbvdao.getReadableDatabase().rawQuery("SELECT * FROM " + NOME_TABELA + " WHERE " + ID_PARSE + " = " + "'" + id_parse + "'", null));
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
            usuario.add(new Usuario(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getInt(6), c.getInt(7), c.getString(8), c.getString(9),c.getString(10)));
        }
        return usuario;
    }
}
